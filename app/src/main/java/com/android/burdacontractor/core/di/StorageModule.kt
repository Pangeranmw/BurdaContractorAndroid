package com.android.burdacontractor.core.di

import android.content.Context
import com.android.burdacontractor.core.data.source.local.storage.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {
    @Provides
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager = SessionManager(context)
}