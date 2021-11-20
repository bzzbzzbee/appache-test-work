package com.example.appachetestwork.di

import com.example.appachetestwork.DrawViewHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DrawViewHelperModule {
    @Provides
    fun provideDrawViewHelper(): DrawViewHelper = DrawViewHelper()
}