package com.android.burdacontractor.core.di

import android.content.Context
import android.net.ConnectivityManager
import com.android.burdacontractor.BuildConfig
import com.android.burdacontractor.core.data.source.remote.network.AksesBarangService
import com.android.burdacontractor.core.data.source.remote.network.BarangService
import com.android.burdacontractor.core.data.source.remote.network.DistanceMatrixService
import com.android.burdacontractor.core.data.source.remote.network.GudangService
import com.android.burdacontractor.core.data.source.remote.network.PeminjamanService
import com.android.burdacontractor.core.data.source.remote.network.PengembalianService
import com.android.burdacontractor.core.data.source.remote.network.PerusahaanService
import com.android.burdacontractor.core.data.source.remote.network.ProyekService
import com.android.burdacontractor.feature.auth.data.source.remote.network.AuthService
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.network.DeliveryOrderService
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.network.PreOrderService
import com.android.burdacontractor.feature.kendaraan.data.source.remote.network.KendaraanService
import com.android.burdacontractor.feature.profile.data.source.remote.network.UserService
import com.android.burdacontractor.feature.suratjalan.data.source.remote.network.SuratJalanService
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideFirebaseReference(): DatabaseReference {
        return Firebase.database.getReference("logistic")
    }

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            })
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    @Provides
    fun provideDistanceMatrixService(client: OkHttpClient): DistanceMatrixService{
        val retrofit = Retrofit.Builder()
        .baseUrl("http://router.project-osrm.org/route/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        return retrofit.create(DistanceMatrixService::class.java)
    }
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit{
        return Retrofit.Builder()
        .baseUrl("${BuildConfig.BASE_URL}/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    }

    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }
    @Provides
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
    @Provides
    fun provideBarangService(retrofit: Retrofit): BarangService {
        return retrofit.create(BarangService::class.java)
    }
    @Provides
    fun provideDeliveryOrderService(retrofit: Retrofit): DeliveryOrderService {
        return retrofit.create(DeliveryOrderService::class.java)
    }
    @Provides
    fun provideProyekService(retrofit: Retrofit): ProyekService {
        return retrofit.create(ProyekService::class.java)
    }
    @Provides
    fun providePerusahaanService(retrofit: Retrofit): PerusahaanService {
        return retrofit.create(PerusahaanService::class.java)
    }
    @Provides
    fun provideGudangService(retrofit: Retrofit): GudangService {
        return retrofit.create(GudangService::class.java)
    }
    @Provides
    fun providePeminjamanService(retrofit: Retrofit): PeminjamanService {
        return retrofit.create(PeminjamanService::class.java)
    }
    @Provides
    fun providePengembalianService(retrofit: Retrofit): PengembalianService {
        return retrofit.create(PengembalianService::class.java)
    }
    @Provides
    fun provideAksesBarangService(retrofit: Retrofit): AksesBarangService {
        return retrofit.create(AksesBarangService::class.java)
    }
    @Provides
    fun provideKendaraanService(retrofit: Retrofit): KendaraanService {
        return retrofit.create(KendaraanService::class.java)
    }
    @Provides
    fun providePreOrderService(retrofit: Retrofit): PreOrderService {
        return retrofit.create(PreOrderService::class.java)
    }
    @Provides
    fun provideSuratJalanService(retrofit: Retrofit): SuratJalanService {
        return retrofit.create(SuratJalanService::class.java)
    }
}