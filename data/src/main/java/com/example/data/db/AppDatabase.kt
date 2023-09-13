package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.dao.CoordinatesDao
import com.example.domain.model.CoordinatesModel

@Database(entities = [CoordinatesModel::class,], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun nameDao(): CoordinatesDao

}
