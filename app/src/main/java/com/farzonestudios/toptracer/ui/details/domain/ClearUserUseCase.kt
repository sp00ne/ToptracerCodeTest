package com.farzonestudios.toptracer.ui.details.domain

import com.farzonestudios.toptracer.ui.details.data.DetailsRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ClearUserUseCase @Inject constructor(
    private val detailsRepository: DetailsRepository
) {
    suspend operator fun invoke() = detailsRepository.clearUser()
}