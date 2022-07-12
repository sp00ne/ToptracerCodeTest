package com.farzonestudios.toptracer.ui.details.data

data class Gif(
    val url: String,
    val description: String
)

fun GifRootSchema.toGif() = Gif(
    url = data.images.originalSize.url,
    description = data.title
)