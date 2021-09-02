package com.example.appachetestwork.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrawingAppRepository @Inject constructor(private val drawingDbDao: DrawingDbDao){

}