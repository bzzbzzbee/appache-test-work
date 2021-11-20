package com.example.appachetestwork.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appachetestwork.data.entities.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface DrawingDbDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(project: Project): Long

    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE id IN(:id)")
    fun getProjectById(id: Long): Project?

    @Query("SELECT COUNT(id) FROM projects")
    suspend fun countProjects(): Long
}