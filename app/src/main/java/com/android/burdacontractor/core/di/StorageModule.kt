package com.android.burdacontractor.core.di

import android.content.Context
import com.android.burdacontractor.core.data.source.local.storage.SessionManager
import com.android.burdacontractor.presentation.test.locationtracker.location_service.location_client.DefaultLocationClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager = SessionManager(context)
    @Singleton
    @Provides
    fun provideDefaultLocationClient(@ApplicationContext context : Context): DefaultLocationClient =
        DefaultLocationClient(
            context,
            LocationServices.getFusedLocationProviderClient(context)
        )
}