package com.example.googlemapsapi.util

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class JsonDataReceiver : DataReceiver {
    override suspend fun receiveData(inputStream: InputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        return buildString {
            bufferedReader.useLines { lines ->
                lines.forEach { line ->
                    append(line)
                }
            }
        }
    }
}