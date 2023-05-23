package com.android.burdacontractor.di

import com.android.burdacontractor.core.domain.usecase.AuthInteractor
import com.android.burdacontractor.core.domain.usecase.AuthUseCase
import com.android.burdacontractor.core.domain.usecase.LogisticInteractor
import com.android.burdacontractor.core.domain.usecase.LogisticUseCase
import com.android.burdacontractor.core.domain.usecase.StorageInteractor
import com.android.burdacontractor.core.domain.usecase.StorageUseCase
import com.android.burdacontractor.core.domain.usecase.SuratJalanInteractor
import com.android.burdacontractor.core.domain.usecase.SuratJalanUseCase
import com.android.burdacontractor.core.domain.usecase.TourismInteractor
import com.android.burdacontractor.core.domain.usecase.TourismUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    @ViewModelScoped
    abstract fun provideTourismUseCase(tourismInteractor: TourismInteractor): TourismUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideSuratJalanUseCase(suratJalanInteractor: SuratJalanInteractor): SuratJalanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideLogisticUseCase(logisticInteractor: LogisticInteractor): LogisticUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideStorageUseCase(storageInteractor: StorageInteractor): StorageUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideAuthUseCase(authInteractor: AuthInteractor): AuthUseCase
}
