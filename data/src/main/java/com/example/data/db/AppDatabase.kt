package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.dao.CoordinatesDao
import com.example.domain.model.CoordinatesModel
import dagger.hilt.EntryPoint

@Database(entities = [CoordinatesModel::class,], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun nameDao(): CoordinatesDao




}
