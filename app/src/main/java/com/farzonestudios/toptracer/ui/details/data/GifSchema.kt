package com.farzonestudios.toptracer.ui.details.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * A container of backend data from giphy
 */
data class GifRootSchema(
    @SerializedName("data") val data: GifSchema
)

data class GifSchema(
    @SerializedName("title") val title: String,
    @SerializedName("images") val images: ImagesSchema
)

data class ImagesSchema(
    @SerializedName("original") val originalSize: ImageSchema
)


data class ImageSchema(
    @Expose @SerializedName("url") val url: String
)
