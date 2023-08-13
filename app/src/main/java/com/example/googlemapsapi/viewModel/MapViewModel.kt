package com.example.googlemapsapi.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CoordinatesModel
import com.example.domain.usecase.SaveCoordinatesUseCase
import com.example.domain.model.PointData
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import javax.inject.Inject

@HiltViewModel
class MapViewModel  @Inject constructor(
    private val saveUseCase: SaveCoordinatesUseCase
) : ViewModel() {

    fun saveTest(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val test = CoordinatesModel(latitude = latitude, longitude = longitude)
            saveUseCase.execute(test)

        }
    }

    private val TAG = "MapViewModel" // Добавлен тег для логирования

    private val serverPort = 49153 // Здесь укажите порт, на котором будет запущен сервер
    // LiveData для уведомления об успешной загрузке файла
    private val _fileLoadedLiveData = MutableLiveData<Unit>()
    val fileLoadedLiveData: LiveData<Unit> = _fileLoadedLiveData
     fun startTcpServer() {
        viewModelScope.launch(Dispatchers.IO) {
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
                        // If parsing as array fails, try parsing as object
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

