package com.example.appachetestwork.data

import androidx.room.Dao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrawingAppRepository @Inject constructor(drawingDbDao: Dao)