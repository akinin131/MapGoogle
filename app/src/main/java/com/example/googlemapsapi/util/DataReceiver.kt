package com.example.googlemapsapi.util

import java.io.InputStream

interface DataReceiver {
    suspend fun receiveData(inputStream: InputStream): String
}