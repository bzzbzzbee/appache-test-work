package com.example.appachetestwork.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appachetestwork.PROJECT_TABLE_NAME

@Entity(
    tableName = PROJECT_TABLE_NAME
)
data class Project(
    val name: String,
    val projectUri: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}