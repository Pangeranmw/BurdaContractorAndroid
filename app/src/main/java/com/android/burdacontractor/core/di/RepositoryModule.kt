package com.android.burdacontractor.core.di

import com.android.burdacontractor.core.data.LogisticRepository
import com.android.burdacontractor.core.data.StorageRepository
import com.android.burdacontractor.core.data.SuratJalanRepository
import com.android.burdacontractor.core.domain.repository.ILogisticRepository
import com.android.burdacontractor.core.domain.repository.IStorageRepository
import com.android.burdacontractor.core.domain.repository.ISuratJalanRepository
import com.android.burdacontractor.presentation.service.location.DefaultLocationClient
import com.android.burdacontractor.presentation.service.location.LocationClient
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
    abstract fun provideLogisticRepository(logisticRepository: LogisticRepository): ILogisticRepository

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