package com.example.googlemapsapi.di.data

import android.content.Context
import androidx.room.Room
import com.example.data.dao.CoordinatesDao
import com.example.data.db.AppDatabase
import com.example.data.repository.CoordinatesRepositoryImpl
import com.example.data.storage.CoordinatesInterface
import com.example.data.storage.CoordinatesRelease
import com.example.domain.repository.CoordinatesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideCoordinatesDao(appDatabase: AppDatabase): CoordinatesDao {
        return appDatabase.nameDao()
    }

    @Provides
    fun provideCoordinatesInterface(coordinatesDao: CoordinatesDao): CoordinatesInterface {
        return CoordinatesRelease(coordinatesDao)
    }

    @Provides
    fun provideCoordinatesRepository(coordinatesInterface: CoordinatesInterface): CoordinatesRepository {
        return CoordinatesRepositoryImpl(coordinatesInterface)
    }
}