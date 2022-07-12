package com.farzonestudios.toptracer.ui.details.domain

import com.farzonestudios.toptracer.data.User
import com.farzonestudios.toptracer.data.UserStore
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@ViewModelScoped
class ListenToUserUseCase @Inject constructor(
    private val userStore: UserStore
) {
    operator fun invoke(): Flow<User?> = userStore.userFlow
}