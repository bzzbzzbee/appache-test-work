package com.example.appachetestwork.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.appachetestwork.data.DrawingAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawingScreenViewModel @Inject constructor(private val repository: DrawingAppRepository) :
    ViewModel() {
    val countProjects = repository.countProjects()
    fun getProject(id: String) = repository.getProjectById(id)
}