package com.example.appachetestwork.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appachetestwork.data.entities.Project

@Database(
    entities = [Project::class],
    version = 1,
    exportSchema = false
)

abstract class DrawingDataBase : RoomDatabase() {
    abstract fun drawingDbDao(): DrawingDbDao
}