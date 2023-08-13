package com.example.googlemapsapi.di.data

import android.content.Context
import androidx.room.Room
import com.example.data.dao.CoordinatesDao
import com.example.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideCoordinatesDao(appDatabase: AppDatabase): CoordinatesDao {
        return appDatabase.nameDao()
    }
}
