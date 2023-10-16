package com.android.burdacontractor.di

import com.android.burdacontractor.core.domain.usecase.GetDistanceMatrixInteractor
import com.android.burdacontractor.core.domain.usecase.GetDistanceMatrixUseCase
import com.android.burdacontractor.core.domain.usecase.LogisticInteractor
import com.android.burdacontractor.core.domain.usecase.LogisticUseCase
import com.android.burdacontractor.core.domain.usecase.StorageInteractor
import com.android.burdacontractor.core.domain.usecase.StorageUseCase
import com.android.burdacontractor.feature.auth.domain.usecase.LoginInteractor
import com.android.burdacontractor.feature.auth.domain.usecase.LoginUseCase
import com.android.burdacontractor.feature.auth.domain.usecase.LoginWithPinInteractor
import com.android.burdacontractor.feature.auth.domain.usecase.LoginWithPinUseCase
import com.android.burdacontractor.feature.auth.domain.usecase.LogoutInteractor
import com.android.burdacontractor.feature.auth.domain.usecase.LogoutUseCase
import com.android.burdacontractor.feature.auth.domain.usecase.RegisterInteractor
import com.android.burdacontractor.feature.auth.domain.usecase.RegisterUseCase
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.*
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetKendaraanByLogisticInteractor
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetKendaraanByLogisticUseCase
import com.android.burdacontractor.feature.profile.domain.usecase.GetUserByTokenInteractor
import com.android.burdacontractor.feature.profile.domain.usecase.GetUserByTokenUseCase
import com.android.burdacontractor.feature.profile.domain.usecase.UploadTtdInteractor
import com.android.burdacontractor.feature.profile.domain.usecase.UploadTtdUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.*
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
    abstract fun provideGetDistanceMatrixUseCase(getDistanceMatrixInteractor: GetDistanceMatrixInteractor): GetDistanceMatrixUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideLogisticUseCase(logisticInteractor: LogisticInteractor): LogisticUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideStorageUseCase(storageInteractor: StorageInteractor): StorageUseCase

    // AUTH USE CASE
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

    // SURAT JALAN USE CASE
    @Binds
    @ViewModelScoped
    abstract fun provideGetAllSuratJalanUseCase(getAllSuratJalanInteractor: GetAllSuratJalanInteractor): GetAllSuratJalanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetStatistikMenungguSuratJalanUseCase(getStatistikMenungguSuratJalanInteractor: GetStatistikMenungguSuratJalanInteractor): GetStatistikMenungguSuratJalanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetSomeActiveSuratJalanUseCase(getSomeActiveSuratJalanInteractor: GetSomeActiveSuratJalanInteractor): GetSomeActiveSuratJalanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetCountActiveSuratJalanUseCase(getCountActiveSuratJalanInteractor: GetCountActiveSuratJalanInteractor): GetCountActiveSuratJalanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetAllSuratJalanDalamPerjalananByUserUseCase(getAllSuratJalanDalamPerjalananByUserInteractor: GetAllSuratJalanDalamPerjalananByUserInteractor): GetAllSuratJalanDalamPerjalananByUserUseCase

    // DELIVERY ORDER USE CASE
    @Binds
    @ViewModelScoped
    abstract fun provideGetAllDeliveryOrderUseCase(getAllDeliveryOrderInteractor: GetAllDeliveryOrderInteractor): GetAllDeliveryOrderUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideUploadFotoBuktiDeliveryOrderUseCase(uploadFotoBuktiDeliveryOrderInteractor: UploadFotoBuktiDeliveryOrderInteractor): UploadFotoBuktiDeliveryOrderUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetSomeActiveDeliveryOrderUseCase(getSomeActiveDeliveryOrderInteractor: GetSomeActiveDeliveryOrderInteractor): GetSomeActiveDeliveryOrderUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetDeliveryOrderByIdUseCase(getDeliveryOrderByIdInteractor: GetDeliveryOrderByIdInteractor): GetDeliveryOrderByIdUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetCountActiveDeliveryOrderUseCase(getCountActiveDeliveryOrderInteractor: GetCountActiveDeliveryOrderInteractor): GetCountActiveDeliveryOrderUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetAllDeliveryOrderDalamPerjalananByUserUseCase(getAllDeliveryOrderDalamPerjalananByUserInteractor: GetAllDeliveryOrderDalamPerjalananByUserInteractor): GetAllDeliveryOrderDalamPerjalananByUserUseCase

    // USER USE CASE
    @Binds
    @ViewModelScoped
    abstract fun provideGetUserByTokenUseCase(getUserByTokenInteractor: GetUserByTokenInteractor): GetUserByTokenUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideUploadTtdUseCase(uploadTtInteractor: UploadTtdInteractor): UploadTtdUseCase

    // KENDARAAN USE CASE
    @Binds
    @ViewModelScoped
    abstract fun provideGetKendaraanByLogisticUseCase(getKendaraanByLogisticInteractor: GetKendaraanByLogisticInteractor): GetKendaraanByLogisticUseCase

}
