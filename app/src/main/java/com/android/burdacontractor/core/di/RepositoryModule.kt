package com.android.burdacontractor.core.di

import com.android.burdacontractor.core.data.LogisticRepository
import com.android.burdacontractor.core.data.StorageRepository
import com.android.burdacontractor.core.domain.repository.ILogisticRepository
import com.android.burdacontractor.core.domain.repository.IStorageRepository
import com.android.burdacontractor.core.service.location.DefaultLocationClient
import com.android.burdacontractor.core.service.location.LocationClient
import com.android.burdacontractor.feature.auth.data.source.AuthRepository
import com.android.burdacontractor.feature.auth.domain.repository.IAuthRepository
import com.android.burdacontractor.feature.deliveryorder.data.DeliveryOrderRepository
import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import com.android.burdacontractor.feature.kendaraan.data.source.KendaraanRepository
import com.android.burdacontractor.feature.kendaraan.domain.repository.IKendaraanRepository
import com.android.burdacontractor.feature.profile.data.UserRepository
import com.android.burdacontractor.feature.profile.domain.repository.IUserRepository
import com.android.burdacontractor.feature.suratjalan.data.SuratJalanRepository
import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, StorageModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideSuratJalanRepository(suratJalanRepository: SuratJalanRepository): ISuratJalanRepository

    @Binds
    abstract fun provideUserRepository(userRepository: UserRepository): IUserRepository
    @Binds
    abstract fun provideDeliveryOrderRepository(userRepository: DeliveryOrderRepository): IDeliveryOrderRepository

    @Binds
    abstract fun provideLogisticRepository(logisticRepository: LogisticRepository): ILogisticRepository

    @Binds
    abstract fun provideAuthRepository(authRepository: AuthRepository): IAuthRepository

    @Binds
    abstract fun provideKendaraanRepository(kendaraanRepository: KendaraanRepository): IKendaraanRepository

    @Binds
    abstract fun provideStorageRepository(storageRepository: StorageRepository): IStorageRepository

    @Singleton
    @Binds
    abstract fun providesLocationClient(defaultLocationClient: DefaultLocationClient): LocationClient

//    @Binds
//    abstract fun provideDeliveryOrderRepository(deliveryOrderRepository: DeliveryOrderRepository): IDeliveryOrderRepository
//
//    @Binds
//    abstract fun provideUserRepository(userRepository: UserRepository): IUserRepository
//
//    @Binds
//    abstract fun providePeminjamanRepository(peminjamanRepository: PeminjamanRepository): IPeminjamanRepository
//
//    @Binds
//    abstract fun providePengembalianRepository(pengembalianRepository: PengembalianRepository): IPengembalianRepository
//
//    @Binds
//    abstract fun providePengembalianRepository(peminjamanRepository: PengembalianRepository): IPengembalianRepository
}