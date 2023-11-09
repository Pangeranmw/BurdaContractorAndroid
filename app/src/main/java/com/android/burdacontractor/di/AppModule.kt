package com.android.burdacontractor.di

import com.android.burdacontractor.core.domain.usecase.GetCityByProvinceInteractor
import com.android.burdacontractor.core.domain.usecase.GetCityByProvinceUseCase
import com.android.burdacontractor.core.domain.usecase.GetDistanceMatrixInteractor
import com.android.burdacontractor.core.domain.usecase.GetDistanceMatrixUseCase
import com.android.burdacontractor.core.domain.usecase.GetLocationFromAddressInteractor
import com.android.burdacontractor.core.domain.usecase.GetLocationFromAddressUseCase
import com.android.burdacontractor.core.domain.usecase.GetLocationFromCoordinateInteractor
import com.android.burdacontractor.core.domain.usecase.GetLocationFromCoordinateUseCase
import com.android.burdacontractor.core.domain.usecase.GetProvinceInteractor
import com.android.burdacontractor.core.domain.usecase.GetProvinceUseCase
import com.android.burdacontractor.core.domain.usecase.LogisticFirebaseFirebaseInteractor
import com.android.burdacontractor.core.domain.usecase.LogisticFirebaseUseCase
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
import com.android.burdacontractor.feature.gudang.domain.usecase.AddGudangInteractor
import com.android.burdacontractor.feature.gudang.domain.usecase.AddGudangUseCase
import com.android.burdacontractor.feature.gudang.domain.usecase.DeleteGudangInteractor
import com.android.burdacontractor.feature.gudang.domain.usecase.DeleteGudangUseCase
import com.android.burdacontractor.feature.gudang.domain.usecase.GetActiveDeliveryOrderByGudangInteractor
import com.android.burdacontractor.feature.gudang.domain.usecase.GetActiveDeliveryOrderByGudangUseCase
import com.android.burdacontractor.feature.gudang.domain.usecase.GetActiveSuratJalanByGudangInteractor
import com.android.burdacontractor.feature.gudang.domain.usecase.GetActiveSuratJalanByGudangUseCase
import com.android.burdacontractor.feature.gudang.domain.usecase.GetAllGudangInteractor
import com.android.burdacontractor.feature.gudang.domain.usecase.GetAllGudangUseCase
import com.android.burdacontractor.feature.gudang.domain.usecase.GetGudangByIdInteractor
import com.android.burdacontractor.feature.gudang.domain.usecase.GetGudangByIdUseCase
import com.android.burdacontractor.feature.gudang.domain.usecase.GetGudangProvinsiInteractor
import com.android.burdacontractor.feature.gudang.domain.usecase.GetGudangProvinsiUseCase
import com.android.burdacontractor.feature.gudang.domain.usecase.GetStatistikDeliveryOrderByGudangInteractor
import com.android.burdacontractor.feature.gudang.domain.usecase.GetStatistikDeliveryOrderByGudangUseCase
import com.android.burdacontractor.feature.gudang.domain.usecase.GetStatistikSuratJalanByGudangInteractor
import com.android.burdacontractor.feature.gudang.domain.usecase.GetStatistikSuratJalanByGudangUseCase
import com.android.burdacontractor.feature.gudang.domain.usecase.UpdateGudangInteractor
import com.android.burdacontractor.feature.gudang.domain.usecase.UpdateGudangUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.AddKendaraanInteractor
import com.android.burdacontractor.feature.kendaraan.domain.usecase.AddKendaraanUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.CancelReturnKendaraanInteractor
import com.android.burdacontractor.feature.kendaraan.domain.usecase.CancelReturnKendaraanUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.DeleteKendaraanInteractor
import com.android.burdacontractor.feature.kendaraan.domain.usecase.DeleteKendaraanUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.DeletePengendaraInteractor
import com.android.burdacontractor.feature.kendaraan.domain.usecase.DeletePengendaraUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetActiveDeliveryOrderByKendaraanInteractor
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetActiveDeliveryOrderByKendaraanUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetActiveSuratJalanByKendaraanInteractor
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetActiveSuratJalanByKendaraanUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetAllKendaraanInteractor
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetAllKendaraanUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetKendaraanByIdInteractor
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetKendaraanByIdUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetKendaraanByLogisticInteractor
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetKendaraanByLogisticUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetKendaraanGudangInteractor
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetKendaraanGudangUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.ReturnKendaraanInteractor
import com.android.burdacontractor.feature.kendaraan.domain.usecase.ReturnKendaraanUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.UpdateKendaraanInteractor
import com.android.burdacontractor.feature.kendaraan.domain.usecase.UpdateKendaraanUseCase
import com.android.burdacontractor.feature.logistic.domain.usecase.GetAllLogisticInteractor
import com.android.burdacontractor.feature.logistic.domain.usecase.GetAllLogisticUseCase
import com.android.burdacontractor.feature.logistic.domain.usecase.GetLogisticActiveSjDoLocationInteractor
import com.android.burdacontractor.feature.logistic.domain.usecase.GetLogisticActiveSjDoLocationUseCase
import com.android.burdacontractor.feature.logistic.domain.usecase.GetLogisticByIdInteractor
import com.android.burdacontractor.feature.logistic.domain.usecase.GetLogisticByIdUseCase
import com.android.burdacontractor.feature.perusahaan.domain.usecase.AddPerusahaanInteractor
import com.android.burdacontractor.feature.perusahaan.domain.usecase.AddPerusahaanUseCase
import com.android.burdacontractor.feature.perusahaan.domain.usecase.DeletePerusahaanInteractor
import com.android.burdacontractor.feature.perusahaan.domain.usecase.DeletePerusahaanUseCase
import com.android.burdacontractor.feature.perusahaan.domain.usecase.GetActiveDeliveryOrderByPerusahaanInteractor
import com.android.burdacontractor.feature.perusahaan.domain.usecase.GetActiveDeliveryOrderByPerusahaanUseCase
import com.android.burdacontractor.feature.perusahaan.domain.usecase.GetAllPerusahaanInteractor
import com.android.burdacontractor.feature.perusahaan.domain.usecase.GetAllPerusahaanUseCase
import com.android.burdacontractor.feature.perusahaan.domain.usecase.GetPerusahaanByIdInteractor
import com.android.burdacontractor.feature.perusahaan.domain.usecase.GetPerusahaanByIdUseCase
import com.android.burdacontractor.feature.perusahaan.domain.usecase.GetPerusahaanProvinsiInteractor
import com.android.burdacontractor.feature.perusahaan.domain.usecase.GetPerusahaanProvinsiUseCase
import com.android.burdacontractor.feature.perusahaan.domain.usecase.GetStatistikDeliveryOrderByPerusahaanInteractor
import com.android.burdacontractor.feature.perusahaan.domain.usecase.GetStatistikDeliveryOrderByPerusahaanUseCase
import com.android.burdacontractor.feature.perusahaan.domain.usecase.UpdatePerusahaanInteractor
import com.android.burdacontractor.feature.perusahaan.domain.usecase.UpdatePerusahaanUseCase
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
    abstract fun provideLogisticFirebaseUseCase(logisticFirebaseInteractor: LogisticFirebaseFirebaseInteractor): LogisticFirebaseUseCase

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
    abstract fun provideDeleteSuratJalanUseCase(deleteSuratJalanInteractor: DeleteSuratJalanInteractor): DeleteSuratJalanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetAllSuratJalanUseCase(getAllSuratJalanInteractor: GetAllSuratJalanInteractor): GetAllSuratJalanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetStatistikMenungguSuratJalanUseCase(
        getStatistikMenungguSuratJalanInteractor: GetStatistikMenungguSuratJalanInteractor
    ): GetStatistikMenungguSuratJalanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetSomeActiveSuratJalanUseCase(getSomeActiveSuratJalanInteractor: GetSomeActiveSuratJalanInteractor): GetSomeActiveSuratJalanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetCountActiveSuratJalanUseCase(getCountActiveSuratJalanInteractor: GetCountActiveSuratJalanInteractor): GetCountActiveSuratJalanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetAllSuratJalanDalamPerjalananByUserUseCase(
        getAllSuratJalanDalamPerjalananByUserInteractor: GetAllSuratJalanDalamPerjalananByUserInteractor
    ): GetAllSuratJalanDalamPerjalananByUserUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetSuratJalanByIdUseCase(getSuratJalanByIdInteractor: GetSuratJalanByIdInteractor): GetSuratJalanByIdUseCase

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
    abstract fun provideGetAllDeliveryOrderDalamPerjalananByUserUseCase(
        getAllDeliveryOrderDalamPerjalananByUserInteractor: GetAllDeliveryOrderDalamPerjalananByUserInteractor
    ): GetAllDeliveryOrderDalamPerjalananByUserUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideAddDeliveryOrderStepOneUseCase(addDeliveryOrderStepOneUseCaseInteractor: AddDeliveryOrderStepOneInteractor): AddDeliveryOrderStepOneUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideAddDeliveryOrderStepTwoUseCase(addDeliveryOrderStepTwoUseCaseInteractor: AddDeliveryOrderStepTwoInteractor): AddDeliveryOrderStepTwoUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideUpdateDeliveryOrderStepOneUseCase(
        updateDeliveryOrderStepOneUseCaseInteractor: UpdateDeliveryOrderStepOneInteractor
    ): UpdateDeliveryOrderStepOneUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideUpdateDeliveryOrderStepTwoUseCase(
        updateDeliveryOrderStepTwoUseCaseInteractor: UpdateDeliveryOrderStepTwoInteractor
    ): UpdateDeliveryOrderStepTwoUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideDeleteDeliveryOrderUseCase(deleteDeliveryOrderUseCaseInteractor: DeleteDeliveryOrderInteractor): DeleteDeliveryOrderUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideDeletePreOrderUseCase(deletePreOrderUseCaseInteractor: DeletePreOrderInteractor): DeletePreOrderUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideSendDeliveryOrderUseCase(sendDeliveryOrderUseCaseInteractor: SendDeliveryOrderInteractor): SendDeliveryOrderUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideMarkCompleteDeliveryOrderUseCase(markCompleteDeliveryOrderUseCaseInteractor: MarkCompleteDeliveryOrderInteractor): MarkCompleteDeliveryOrderUseCase

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

    @Binds
    @ViewModelScoped
    abstract fun provideGetKendaraanByIdUseCase(getKendaraanByIdInteractor: GetKendaraanByIdInteractor): GetKendaraanByIdUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetKendaraanGudangUseCase(getKendaraanGudangInteractor: GetKendaraanGudangInteractor): GetKendaraanGudangUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetAllKendaraanUseCase(getAllKendaraanInteractor: GetAllKendaraanInteractor): GetAllKendaraanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetActiveDeliveryOrderByKendaraanUseCase(
        getActiveDeliveryOrderByKendaraanInteractor: GetActiveDeliveryOrderByKendaraanInteractor
    ): GetActiveDeliveryOrderByKendaraanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetActiveSuratJalanByKendaraanUseCase(
        getActiveSuratJalanByKendaraanInteractor: GetActiveSuratJalanByKendaraanInteractor
    ): GetActiveSuratJalanByKendaraanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideAddKendaraanUseCase(addKendaraanInteractor: AddKendaraanInteractor): AddKendaraanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideUpdateKendaraanUseCase(updateKendaraanInteractor: UpdateKendaraanInteractor): UpdateKendaraanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideDeleteKendaraanUseCase(deleteKendaraanInteractor: DeleteKendaraanInteractor): DeleteKendaraanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideDeletePengendaraUseCase(deletePengendaraInteractor: DeletePengendaraInteractor): DeletePengendaraUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideReturnKendaraanUseCase(returnKendaraanInteractor: ReturnKendaraanInteractor): ReturnKendaraanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideCancelReturnKendaraanUseCase(cancelReturnKendaraanInteractor: CancelReturnKendaraanInteractor): CancelReturnKendaraanUseCase


    // PERUSAHAAN USE CASE
    @Binds
    @ViewModelScoped
    abstract fun provideGetPerusahaanByIdUseCase(getPerusahaanByIdInteractor: GetPerusahaanByIdInteractor): GetPerusahaanByIdUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetPerusahaanProvinsiUseCase(getPerusahaanProvinsiInteractor: GetPerusahaanProvinsiInteractor): GetPerusahaanProvinsiUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetAllPerusahaanUseCase(getAllPerusahaanInteractor: GetAllPerusahaanInteractor): GetAllPerusahaanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetActiveDeliveryOrderByPerusahaanUseCase(
        getActiveDeliveryOrderByPerusahaanInteractor: GetActiveDeliveryOrderByPerusahaanInteractor
    ): GetActiveDeliveryOrderByPerusahaanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideAddPerusahaanUseCase(addPerusahaanInteractor: AddPerusahaanInteractor): AddPerusahaanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideUpdatePerusahaanUseCase(updatePerusahaanInteractor: UpdatePerusahaanInteractor): UpdatePerusahaanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideDeletePerusahaanUseCase(deletePerusahaanInteractor: DeletePerusahaanInteractor): DeletePerusahaanUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetStatistikDeliveryOrderByPerusahaanUseCase(
        getStatistikDeliveryOrderByPerusahaanInteractor: GetStatistikDeliveryOrderByPerusahaanInteractor
    ): GetStatistikDeliveryOrderByPerusahaanUseCase


    // GUDANG USE CASE
    @Binds
    @ViewModelScoped
    abstract fun provideGetGudangByIdUseCase(getGudangByIdInteractor: GetGudangByIdInteractor): GetGudangByIdUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetGudangProvinsiUseCase(getGudangProvinsiInteractor: GetGudangProvinsiInteractor): GetGudangProvinsiUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetAllGudangUseCase(getAllGudangInteractor: GetAllGudangInteractor): GetAllGudangUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetActiveDeliveryOrderByGudangUseCase(
        getActiveDeliveryOrderByGudangInteractor: GetActiveDeliveryOrderByGudangInteractor
    ): GetActiveDeliveryOrderByGudangUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetActiveSuratJalanByGudangUseCase(getActiveSuratJalanByGudangInteractor: GetActiveSuratJalanByGudangInteractor): GetActiveSuratJalanByGudangUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideAddGudangUseCase(addGudangInteractor: AddGudangInteractor): AddGudangUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideUpdateGudangUseCase(updateGudangInteractor: UpdateGudangInteractor): UpdateGudangUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideDeleteGudangUseCase(deleteGudangInteractor: DeleteGudangInteractor): DeleteGudangUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetStatistikDeliveryOrderByGudangUseCase(
        getStatistikDeliveryOrderByGudangInteractor: GetStatistikDeliveryOrderByGudangInteractor
    ): GetStatistikDeliveryOrderByGudangUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetStatistikSuratJalanByGudangUseCase(
        getStatistikSuratJalanByGudangInteractor: GetStatistikSuratJalanByGudangInteractor
    ): GetStatistikSuratJalanByGudangUseCase

    // LOGISTIC USE CASE
    @Binds
    @ViewModelScoped
    abstract fun provideGetAllLogisticUseCase(getAllLogisticInteractor: GetAllLogisticInteractor): GetAllLogisticUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetLogisticByIdUseCase(getLogisticByIdInteractor: GetLogisticByIdInteractor): GetLogisticByIdUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetLogisticActiveSjDoLocationUseCase(getLogisticActiveSjDoLocationInteractor: GetLogisticActiveSjDoLocationInteractor): GetLogisticActiveSjDoLocationUseCase

    // DAERAH USE CASE
    @Binds
    @ViewModelScoped
    abstract fun provideGetProvinceUseCase(getProvinceInteractor: GetProvinceInteractor): GetProvinceUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetCityByProvinceUseCase(getCityByProvinceInteractor: GetCityByProvinceInteractor): GetCityByProvinceUseCase

    // GEO CODER USE CASE
    @Binds
    @ViewModelScoped
    abstract fun provideGetLocationFromCoordinateUseCase(getLocationFromCoordinateInteractor: GetLocationFromCoordinateInteractor): GetLocationFromCoordinateUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetLocationFromAddressUseCase(getLocationFromAddressInteractor: GetLocationFromAddressInteractor): GetLocationFromAddressUseCase

}
