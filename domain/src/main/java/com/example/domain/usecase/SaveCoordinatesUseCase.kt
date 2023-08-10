package com.example.domain.usecase

import com.example.domain.model.CoordinatesModel
import com.example.domain.repository.CoordinatesRepository
import javax.inject.Inject

class SaveCoordinatesUseCase @Inject constructor(private val coordinatesRepository: CoordinatesRepository) {
    suspend fun execute(params: CoordinatesModel) {
        return coordinatesRepository.addCoordinates(coordinates = params)
    }
}