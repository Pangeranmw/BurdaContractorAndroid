package com.android.burdacontractor.core.di

import com.android.burdacontractor.BuildConfig
import com.android.burdacontractor.core.data.source.remote.network.AksesBarangService
import com.android.burdacontractor.core.data.source.remote.network.ApiService
import com.android.burdacontractor.core.data.source.remote.network.AuthService
import com.android.burdacontractor.core.data.source.remote.network.BarangService
import com.android.burdacontractor.core.data.source.remote.network.DeliveryOrderService
import com.android.burdacontractor.core.data.source.remote.network.GudangService
import com.android.burdacontractor.core.data.source.remote.network.KendaraanService
import com.android.burdacontractor.core.data.source.remote.network.PeminjamanService
import com.android.burdacontractor.core.data.source.remote.network.PengembalianService
import com.android.burdacontractor.core.data.source.remote.network.PerusahaanService
import com.android.burdacontractor.core.data.source.remote.network.PreOrderService
import com.android.burdacontractor.core.data.source.remote.network.ProyekService
import com.android.burdacontractor.core.data.source.remote.network.SuratJalanService
import com.android.burdacontractor.core.data.source.remote.network.UserService
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideApiService(client: OkHttpClient): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideAuthService(client: OkHttpClient): AuthService {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(AuthService::class.java)
    }
    @Provides
    fun provideUserService(client: OkHttpClient): UserService {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(UserService::class.java)
    }
    @Provides
    fun provideBarangService(client: OkHttpClient): BarangService {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(BarangService::class.java)
    }
    @Provides
    fun provideDeliveryOrderService(client: OkHttpClient): DeliveryOrderService {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(DeliveryOrderService::class.java)
    }
    @Provides
    fun provideProyekService(client: OkHttpClient): ProyekService {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ProyekService::class.java)
    }
    @Provides
    fun providePerusahaanService(client: OkHttpClient): PerusahaanService {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(PerusahaanService::class.java)
    }
    @Provides
    fun provideGudangService(client: OkHttpClient): GudangService {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(GudangService::class.java)
    }
    @Provides
    fun providePeminjamanService(client: OkHttpClient): PeminjamanService {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(PeminjamanService::class.java)
    }
    @Provides
    fun providePengembalianService(client: OkHttpClient): PengembalianService {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(PengembalianService::class.java)
    }
    @Provides
    fun provideAksesBarangService(client: OkHttpClient): AksesBarangService {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(AksesBarangService::class.java)
    }
    @Provides
    fun provideKendaraanService(client: OkHttpClient): KendaraanService {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(KendaraanService::class.java)
    }
    @Provides
    fun providePreOrderService(client: OkHttpClient): PreOrderService {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(PreOrderService::class.java)
    }
    @Provides
    fun provideSuratJalanService(client: OkHttpClient): SuratJalanService {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(SuratJalanService::class.java)
    }
}