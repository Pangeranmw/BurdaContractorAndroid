package com.android.burdacontractor.di

import com.android.burdacontractor.core.domain.usecase.LogisticInteractor
import com.android.burdacontractor.core.domain.usecase.LogisticUseCase
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
}
