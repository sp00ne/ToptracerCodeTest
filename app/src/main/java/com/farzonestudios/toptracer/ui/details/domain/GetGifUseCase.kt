package com.farzonestudios.toptracer.ui.details.domain

import com.farzonestudios.toptracer.ui.details.data.DetailsRepository
import com.farzonestudios.toptracer.ui.details.data.Gif
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetGifUseCase @Inject constructor(
    private val repository: DetailsRepository
) {
    suspend operator fun invoke(): Result<Gif> = repository.getRandomGif()
}