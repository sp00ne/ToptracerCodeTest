package com.farzonestudios.toptracer.ui.main.domain

import com.farzonestudios.toptracer.data.User
import com.farzonestudios.toptracer.ui.main.data.MainRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class LoginUserUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    suspend fun loginUser(userName: String, password: String): Result<Unit> = runCatching {
        mainRepository.doLogin(userName, password)
    }
}