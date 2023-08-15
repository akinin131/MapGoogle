package com.example.googlemapsapi.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.CoordinatesModel
import com.example.domain.model.PointData
import com.example.domain.usecase.SaveCoordinatesUseCase
import com.example.googlemapsapi.notification.createNotification
import com.example.googlemapsapi.util.DataReceiver
import com.example.googlemapsapi.util.FileLoadedReceiver
import com.example.googlemapsapi.util.JsonDataReceiver
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ServerSocket
import java.net.Socket
import javax.inject.Inject

@AndroidEntryPoint
class FileSendingService : Service() {

    @Inject
    lateinit var saveUseCase: SaveCoordinatesUseCase
    private val fileLoadedReceiver = FileLoadedReceiver()

    private val dataReceiver: DataReceiver = JsonDataReceiver()

    private val TAG = "FileSendingService"
    private val serverPort = 49153

    private val _fileLoadedLiveData = MutableLiveData<Unit>()
    val fileLoadedLiveData: LiveData<Unit> = _fileLoadedLiveData

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification(this)
        startForeground(NOTIFICATION_ID, notification)
        return START_STICKY
    }

    private val NOTIFICATION_ID = 1

    override fun onCreate() {
        super.onCreate()
        startTcpServer()
        registerReceiver(fileLoadedReceiver, IntentFilter("com.yourapp.FILE_LOADED"))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(fileLoadedReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startTcpServer() {
        GlobalScope.launch(Dispatchers.IO) {
            val serverSocket = ServerSocket(serverPort)
            Log.d(TAG, "Server started on port $serverPort")

            while (true) {
                try {
                    val socket = serverSocket.accept()
                    handleConnection(socket)
                    socket.close()
                } catch (e: Exception) {
                    Log.e(TAG, "Error: ${e.message}", e)
                }
            }
        }
    }

    private suspend fun handleConnection(socket: Socket) {
        val address = socket.inetAddress.hostAddress
        Log.d(TAG, "Accepted connection from: $address")

        val inputStream = withContext(Dispatchers.IO) {
            socket.getInputStream()
        }
        val jsonData = dataReceiver.receiveData(inputStream)
        Log.d(TAG, "Received data: $jsonData")

        processReceivedData(jsonData)
    }
    private suspend fun processReceivedData(jsonData: String) {
        val gson = Gson()

        try {
            val locationDataList = gson.fromJson(jsonData, Array<PointData>::class.java)
            processLocationDataList(locationDataList)
        } catch (e: JsonSyntaxException) {
            try {
                val locationData = gson.fromJson(jsonData, PointData::class.java)
                processSingleLocationData(locationData)
            } catch (e: JsonSyntaxException) {
                Log.e(TAG, "Error parsing JSON data: ${e.message}", e)
            }
        }
    }

    private suspend fun processLocationDataList(locationDataList: Array<PointData>) {
        locationDataList.forEach { locationData ->
            val coordinatesModel = CoordinatesModel(
                latitude = locationData.point.latitude.toDouble(),
                longitude = locationData.point.longitude.toDouble()
            )

            saveUseCase.execute(coordinatesModel)
        }

        sendFileLoadedBroadcast()
    }

     private suspend fun processSingleLocationData(locationData: PointData) {
        val coordinatesModel = CoordinatesModel(
            latitude = locationData.point.latitude.toDouble(),
            longitude = locationData.point.longitude.toDouble()
        )

        saveUseCase.execute(coordinatesModel)
        sendFileLoadedBroadcast()
    }

    private fun sendFileLoadedBroadcast() {
        val fileLoadedIntent = Intent("com.yourapp.FILE_LOADED")
        sendBroadcast(fileLoadedIntent)
        _fileLoadedLiveData.postValue(Unit)
    }
}
