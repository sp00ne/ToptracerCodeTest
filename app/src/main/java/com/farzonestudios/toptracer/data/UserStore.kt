package com.farzonestudios.toptracer.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val userKey = stringPreferencesKey("user")
    }

    val userFlow: Flow<User?> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            // Get our show completed value, defaulting to false if not set:
            preferences[userKey]?.takeUnless { it.isBlank() }?.let {
                User(it)
            }
        }

    suspend fun setUser(user: User?) {
        dataStore.edit {
            if (user != null) {
                it[userKey] = user.username
            } else {
                it.remove(userKey)
            }
        }
    }
}