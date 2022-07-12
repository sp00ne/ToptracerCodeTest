package com.farzonestudios.toptracer.di

import androidx.viewbinding.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    companion object {
        const val GIPHY_CLIENT = "giphy_client"
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .create()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = with(OkHttpClient.Builder()) {
        if (BuildConfig.DEBUG) {
            addInterceptor(httpLoggingInterceptor)
        }
        connectTimeout(NetworkConstants.durationConnectionTimeout)
        readTimeout(NetworkConstants.durationReadWriteTimeout)
        writeTimeout(NetworkConstants.durationReadWriteTimeout)
        build()
    }

    @Provides
    @Named(GIPHY_CLIENT)
    @Singleton
    fun provideGiphyRetrofitClient(httpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.giphy.com/v1/gifs/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
}

object NetworkConstants {
    val durationConnectionTimeout: Duration = Duration.ofSeconds(15L)
    val durationReadWriteTimeout: Duration = Duration.ofSeconds(20L)
}