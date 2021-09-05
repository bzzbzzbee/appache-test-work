package com.example.appachetestwork.data

import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrawingAppRepository @Inject constructor(private val drawingDbDao: DrawingDbDao) {
    fun countProjects() = runBlocking { drawingDbDao.countProjects() }
    fun getProjectById(id: String) = drawingDbDao.getProjectById(id.toLong())
    fun getAllProjects() = drawingDbDao.getAllProjects()
}