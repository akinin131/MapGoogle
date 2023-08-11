package com.example.domain.repository

import com.example.domain.model.CoordinatesModel

interface CoordinatesRepository {

    suspend fun addCoordinates(coordinates: CoordinatesModel)

    suspend fun getCoordinates(): List<CoordinatesModel>
}