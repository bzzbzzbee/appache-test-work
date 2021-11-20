package com.example.appachetestwork.ui

import androidx.lifecycle.ViewModel
import com.example.appachetestwork.paint.DrawViewHelper
import com.example.appachetestwork.data.DrawingAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawingScreenViewModel @Inject constructor(
    private val repository: DrawingAppRepository,
    private val helper: DrawViewHelper
) :
    ViewModel() {
    val countProjects = repository.countProjects()
    val drawViewHelper: DrawViewHelper
        get() = helper

    fun getProject(id: String) = repository.getProjectById(id)

}