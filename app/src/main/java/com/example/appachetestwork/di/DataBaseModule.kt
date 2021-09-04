package com.example.appachetestwork.di

import android.content.Context
import androidx.room.Room
import com.example.appachetestwork.DATABASE_NAME
import com.example.appachetestwork.data.DrawingDataBase
import com.example.appachetestwork.data.DrawingDbDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataBaseModule {

    @Singleton
    @Provides
    fun provideDrawingDatabase(@ApplicationContext context: Context): DrawingDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            DrawingDataBase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideEmployeesDao(drawingDataBase: DrawingDataBase): DrawingDbDao {
        return drawingDataBase.drawingDbDao()
    }
}