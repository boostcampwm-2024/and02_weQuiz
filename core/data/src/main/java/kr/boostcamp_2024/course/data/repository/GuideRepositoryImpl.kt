package kr.boostcamp_2024.course.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kr.boostcamp_2024.course.domain.repository.GuideRepository
import javax.inject.Inject


class GuideRepositoryImpl @Inject constructor(
    private val weQuizDataStore: DataStore<Preferences>,
) : GuideRepository {

    override suspend fun getGuideStatus(): Result<Boolean> =
        runCatching {
            val guideStatus = weQuizDataStore.data.map { preferences ->
                preferences[GUIDE_STATUS_KEY] ?: false
            }.firstOrNull()
            requireNotNull(guideStatus)
        }

    override suspend fun setGuideStatus(status: Boolean): Result<Unit> = runCatching {
        weQuizDataStore.edit { settings ->
            settings[GUIDE_STATUS_KEY] = status
        }
    }

    companion object {
        private val GUIDE_STATUS_KEY = booleanPreferencesKey("guide_status")
    }
}

