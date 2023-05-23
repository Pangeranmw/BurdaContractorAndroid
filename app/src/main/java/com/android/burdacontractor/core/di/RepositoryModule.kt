package com.android.burdacontractor.core.di

import com.android.burdacontractor.core.data.LogisticRepository
import com.android.burdacontractor.core.data.SuratJalanRepository
import com.android.burdacontractor.presentation.test.TourismRepository
import com.android.burdacontractor.core.domain.repository.ILogisticRepository
import com.android.burdacontractor.core.domain.repository.ISuratJalanRepository
import com.android.burdacontractor.core.domain.repository.ITourismRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class, DatabaseModule::class, StorageModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideSuratJalanRepository(suratJalanRepository: SuratJalanRepository): ISuratJalanRepository

    @Binds
    abstract fun provideTourismRepository(tourismRepository: TourismRepository): ITourismRepository

    @Binds
    abstract fun provideLogisticRepository(logisticRepository: LogisticRepository): ILogisticRepository

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