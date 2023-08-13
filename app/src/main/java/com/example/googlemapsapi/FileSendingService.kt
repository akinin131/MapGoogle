package com.example.googlemapsapi

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
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import javax.inject.Inject


@AndroidEntryPoint
class FileSendingService : Service() {

    @Inject
    lateinit var saveUseCase: SaveCoordinatesUseCase

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intentFilter = IntentFilter("com.yourapp.FILE_SENDING")

        val notification = createNotification(this)
        startForeground(NOTIFICATION_ID, notification)

        return START_STICKY
    }
    private val NOTIFICATION_CHANNEL_ID = "FileSendingChannel"
    private val NOTIFICATION_ID = 1
    override fun onCreate() {
        super.onCreate()
        startTcpServer()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private val TAG = "MapViewModel"

    private val serverPort = 49153

    private val _fileLoadedLiveData = MutableLiveData<Unit>()
    val fileLoadedLiveData: LiveData<Unit> = _fileLoadedLiveData

    @OptIn(DelicateCoroutinesApi::class)
    private fun startTcpServer() {
        GlobalScope.launch(Dispatchers.IO) {
            val serverSocket = ServerSocket(serverPort)
            Log.d(TAG, "Server started on port $serverPort")
            while (true) {
                try {
                    val socket = serverSocket.accept()
                    Log.d(TAG, "Accepted connection from: ${socket.inetAddress.hostAddress}")
                    val inputStream = socket.getInputStream()
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))

                    val jsonData = buildString {
                        bufferedReader.useLines { lines ->
                            lines.forEach { line ->
                                append(line)
                            }
                        }
                    }

                    Log.d(TAG, "Received data: $jsonData")

                    val gson = Gson()
                    try {
                        val locationDataList = gson.fromJson(jsonData, Array<PointData>::class.java)

                        locationDataList.forEach { locationData ->
                            val coordinatesModel = CoordinatesModel(
                                latitude = locationData.point.latitude.toDouble(),
                                longitude = locationData.point.longitude.toDouble()
                            )

                            saveUseCase.execute(coordinatesModel)
                        }
                        _fileLoadedLiveData.postValue(Unit)

                    } catch (e: JsonSyntaxException) {
                        try {
                            val locationData = gson.fromJson(jsonData, PointData::class.java)

                            val coordinatesModel = CoordinatesModel(
                                latitude = locationData.point.latitude.toDouble(),
                                longitude = locationData.point.longitude.toDouble()
                            )

                            saveUseCase.execute(coordinatesModel)
                        } catch (e: JsonSyntaxException) {
                            Log.e(TAG, "Error parsing JSON data: ${e.message}", e)
                        }
                    }

                    socket.close()
                } catch (e: Exception) {
                    Log.e(TAG, "Error: ${e.message}", e)
                }
            }
        }
    }
}

