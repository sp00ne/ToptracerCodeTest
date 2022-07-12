package com.farzonestudios.toptracer.ui.details.data

import com.farzonestudios.toptracer.BuildConfig
import com.farzonestudios.toptracer.data.UserStore
import com.farzonestudios.toptracer.di.IoDispatcher
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class DetailsRepository @Inject constructor(
    private val api: GiphyApi,
    private val userStore: UserStore,
    @IoDispatcher private val workDispatcher: CoroutineDispatcher
) {
    suspend fun getRandomGif(): Result<Gif> = withContext(workDispatcher) {
        runCatching {
            api.getRandomGif(BuildConfig.GIPHY_KEY).toGif()
        }
    }

    suspend fun clearUser() = withContext(workDispatcher) {
        userStore.setUser(null)
    }
}