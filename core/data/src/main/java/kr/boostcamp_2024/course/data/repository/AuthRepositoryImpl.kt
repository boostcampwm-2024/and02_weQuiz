package kr.boostcamp_2024.course.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val weQuizDataStore: DataStore<Preferences>
) : AuthRepository {
    override suspend fun storeUserKey(userKey: String): Result<Unit> {
        return runCatching {
            weQuizDataStore.edit { settings ->
                settings[USER_KEY] = userKey
            }
        }
    }

    override suspend fun getUserKey(): Result<String?> {
        return runCatching {
            weQuizDataStore.data.map { preferences ->
                preferences[USER_KEY] ?: throw IllegalStateException("User key is not stored")
            }.firstOrNull()
        }
    }

    companion object {
        private val USER_KEY = stringPreferencesKey("user_key")
    }
}