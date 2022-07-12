package com.farzonestudios.toptracer.ui.details.data

import com.farzonestudios.toptracer.di.NetworkModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DetailsDataModule {

    @Provides
    @Singleton
    fun provideGiphyApi(@Named(NetworkModule.GIPHY_CLIENT) retrofit: Retrofit): GiphyApi =
        retrofit.create(GiphyApi::class.java)
}