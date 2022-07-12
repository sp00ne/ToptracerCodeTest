package com.farzonestudios.toptracer.ui.details.data

import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {
    @GET("random")
    suspend fun getRandomGif(@Query("api_key") apiKey: String): GifRootSchema
}