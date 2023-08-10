package com.example.googlemapsapi.di.domain

import com.example.domain.repository.CoordinatesRepository
import com.example.domain.usecase.SaveCoordinatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {
    @Provides
    fun provideSaveCoordinatesUseCase(coordinatesRepository: CoordinatesRepository): SaveCoordinatesUseCase {
        return SaveCoordinatesUseCase(coordinatesRepository)
    }
}