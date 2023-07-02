package com.android.burdacontractor.di

import com.android.burdacontractor.core.domain.usecase.AuthInteractor
import com.android.burdacontractor.core.domain.usecase.AuthUseCase
import com.android.burdacontractor.core.domain.usecase.LogisticInteractor
import com.android.burdacontractor.core.domain.usecase.LogisticUseCase
import com.android.burdacontractor.core.domain.usecase.StorageInteractor
import com.android.burdacontractor.core.domain.usecase.StorageUseCase
import com.android.burdacontractor.core.domain.usecase.SuratJalanInteractor
import com.android.burdacontractor.core.domain.usecase.SuratJalanUseCase
import com.android.burdacontractor.feature.auth.domain.usecase.LoginInteractor
import com.android.burdacontractor.feature.auth.domain.usecase.LoginUseCase
import com.android.burdacontractor.feature.auth.domain.usecase.LoginWithPinInteractor
import com.android.burdacontractor.feature.auth.domain.usecase.LoginWithPinUseCase
import com.android.burdacontractor.feature.auth.domain.usecase.LogoutInteractor
import com.android.burdacontractor.feature.auth.domain.usecase.LogoutUseCase
import com.android.burdacontractor.feature.auth.domain.usecase.RegisterInteractor
import com.android.burdacontractor.feature.auth.domain.usecase.RegisterUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetAllSuratJalanInteractor
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetAllSuratJalanUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

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
    @ViewModelScoped
    abstract fun provideStorageUseCase(storageInteractor: StorageInteractor): StorageUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideAuthUseCase(authInteractor: AuthInteractor): AuthUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideLoginUseCase(loginInteractor: LoginInteractor): LoginUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideLogoutUseCase(logoutInteractor: LogoutInteractor): LogoutUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideRegisterUseCase(registerInteractor: RegisterInteractor): RegisterUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideLoginWithPinUseCase(loginWithPinInteractor: LoginWithPinInteractor): LoginWithPinUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetAllSuratJalanUseCase(getAllSuratJalanInteractor: GetAllSuratJalanInteractor): GetAllSuratJalanUseCase

}
