package com.example.data.storage

import com.example.data.dao.CoordinatesDao
import com.example.domain.model.CoordinatesModel
import javax.inject.Inject

class CoordinatesRelease @Inject constructor(private val coordinatesDao: CoordinatesDao):CoordinatesInterface {
    override suspend fun addCoordinates(coordinatesModel: CoordinatesModel) {
        return coordinatesDao.insert(coordinatesModel)
    }

    override suspend fun getCoordinates(): List<CoordinatesModel> {
        return coordinatesDao.getAllNotes()
    }
}