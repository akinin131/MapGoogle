package com.example.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "CoordinatesModel", indices = [Index(value = ["latitude", "longitude"], unique = true)])
data class CoordinatesModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double
)
