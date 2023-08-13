package com.example.googlemapsapi.di.data

import com.example.data.dao.CoordinatesDao
import com.example.data.repository.CoordinatesRepositoryImpl
import com.example.data.storage.CoordinatesInterface
import com.example.data.storage.CoordinatesRelease
import com.example.domain.repository.CoordinatesRepository
import com.example.domain.usecase.SaveCoordinatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideCoordinatesInterface(coordinatesDao: CoordinatesDao): CoordinatesInterface {
        return CoordinatesRelease(coordinatesDao)
    }


    @Provides
    fun provideCoordinatesRepository(coordinatesInterface: CoordinatesInterface): CoordinatesRepository {
        return CoordinatesRepositoryImpl(coordinatesInterface)
    }
}