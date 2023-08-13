package com.example.domain.model

import com.google.gson.annotations.SerializedName


data class PointData(
    @SerializedName("point") val point: Point
)
