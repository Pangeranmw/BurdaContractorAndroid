package com.android.burdacontractor.core.di

import android.content.Context
import android.net.ConnectivityManager
import com.android.burdacontractor.BuildConfig
import com.android.burdacontractor.core.data.source.remote.network.DaerahService
import com.android.burdacontractor.core.data.source.remote.network.DistanceMatrixService
import com.android.burdacontractor.core.data.source.remote.network.GeoCoderService
import com.android.burdacontractor.core.data.source.remote.network.PlaceService
import com.android.burdacontractor.feature.auth.data.source.remote.network.AuthService
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.network.DeliveryOrderService
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.network.PreOrderService
import com.android.burdacontractor.feature.gudang.data.source.remote.network.GudangService
import com.android.burdacontractor.feature.kendaraan.data.source.remote.network.KendaraanService
import com.android.burdacontractor.feature.perusahaan.data.source.remote.network.PerusahaanService
import com.android.burdacontractor.feature.profile.data.source.remote.network.UserService
import com.android.burdacontractor.feature.proyek.data.source.remote.network.LogisticService
import com.android.burdacontractor.feature.suratjalan.data.source.remote.network.SuratJalanService
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
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
            .addInterceptor(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            )
            .connectTimeout(0, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideDistanceMatrixService(client: OkHttpClient): DistanceMatrixService {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://router.project-osrm.org/route/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(DistanceMatrixService::class.java)
    }

    @Provides
    fun providePlaceService(client: OkHttpClient): PlaceService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://places.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(PlaceService::class.java)
    }

    @Provides
    fun provideGeoCoderService(client: OkHttpClient): GeoCoderService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(GeoCoderService::class.java)
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
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
    fun provideDeliveryOrderService(retrofit: Retrofit): DeliveryOrderService {
        return retrofit.create(DeliveryOrderService::class.java)
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
    fun provideLogisticService(retrofit: Retrofit): LogisticService {
        return retrofit.create(LogisticService::class.java)
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

    @Provides
    fun provideDaerahService(retrofit: Retrofit): DaerahService {
        return retrofit.create(DaerahService::class.java)
    }
}