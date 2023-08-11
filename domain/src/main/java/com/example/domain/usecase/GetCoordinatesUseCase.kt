package com.example.domain.usecase

import com.example.domain.model.CoordinatesModel
import com.example.domain.repository.CoordinatesRepository
import javax.inject.Inject

class GetCoordinatesUseCase @Inject constructor(private val coordinatesRepository: CoordinatesRepository) {
    suspend fun execute(): List<CoordinatesModel> {
        return coordinatesRepository.getCoordinates()
    }
}