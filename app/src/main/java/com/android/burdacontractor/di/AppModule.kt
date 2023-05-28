package com.android.burdacontractor.di

import android.content.Context
import com.android.burdacontractor.core.domain.usecase.AuthInteractor
import com.android.burdacontractor.core.domain.usecase.AuthUseCase
import com.android.burdacontractor.core.domain.usecase.LogisticInteractor
import com.android.burdacontractor.core.domain.usecase.LogisticUseCase
import com.android.burdacontractor.core.domain.usecase.StorageInteractor
import com.android.burdacontractor.core.domain.usecase.StorageUseCase
import com.android.burdacontractor.core.domain.usecase.SuratJalanInteractor
import com.android.burdacontractor.core.domain.usecase.SuratJalanUseCase
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    @ViewModelScoped
    abstract fun provideSuratJalanUseCase(suratJalanInteractor: SuratJalanInteractor): SuratJalanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideLogisticUseCase(logisticInteractor: LogisticInteractor): LogisticUseCase

    @Binds
    abstract fun provideStorageUseCase(storageInteractor: StorageInteractor): StorageUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideAuthUseCase(authInteractor: AuthInteractor): AuthUseCase

}
