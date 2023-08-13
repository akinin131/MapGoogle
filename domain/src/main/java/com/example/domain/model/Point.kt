package com.example.domain.model

import com.google.gson.annotations.SerializedName

data class Point(
    @SerializedName("lat") val latitude: String,
    @SerializedName("long") val longitude: String
)
