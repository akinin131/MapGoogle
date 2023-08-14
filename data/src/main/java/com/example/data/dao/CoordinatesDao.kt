package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.model.CoordinatesModel

@Dao
interface CoordinatesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(noteModel: CoordinatesModel)
    @Query("SELECT * FROM CoordinatesModel")
    fun getAllNotes(): List<CoordinatesModel>
}