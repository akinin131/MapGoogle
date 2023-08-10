package com.example.data.storage

import com.example.domain.model.CoordinatesModel

interface CoordinatesInterface {
    suspend fun addCoordinates(coordinatesModel: CoordinatesModel)

    suspend fun getCoordinates(): List<CoordinatesModel>
}