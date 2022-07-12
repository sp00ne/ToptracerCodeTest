package com.farzonestudios.toptracer.ui.main.data

import com.farzonestudios.toptracer.data.User
import com.farzonestudios.toptracer.data.UserStore
import com.farzonestudios.toptracer.di.IoDispatcher
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class MainRepository @Inject constructor(
    private val userStore: UserStore,
    @IoDispatcher private val workDispatcher: CoroutineDispatcher
) {
    suspend fun doLogin(username: String, password: String): User {
        withContext(workDispatcher) {
            // Do some network operation, fake a delay
            delay(200L)
            if (password != "password") {
                throw LoginException("wrong password!")
            }
            userStore.setUser(User(username))
        }
        return User(username)
    }
}

class LoginException(causeOfError: String) : Exception("Unable to login due to $causeOfError")