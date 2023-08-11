package com.example.data.repository

import com.example.data.storage.CoordinatesInterface
import com.example.domain.model.CoordinatesModel
import com.example.domain.repository.CoordinatesRepository
import javax.inject.Inject

class CoordinatesRepositoryImpl @Inject constructor (private val coordinatesInterface: CoordinatesInterface) : CoordinatesRepository {
    override suspend fun addCoordinates(coordinates: CoordinatesModel) {
        return coordinatesInterface.addCoordinates(coordinates)
    }

    override suspend fun getCoordinates(): List<CoordinatesModel> {
        return coordinatesInterface.getCoordinates()
    }
}