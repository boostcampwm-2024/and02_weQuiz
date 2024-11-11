package kr.boostcamp_2024.course.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import javax.inject.Inject

data class MainUiState(
    val user: User? = null,
    val studyGroups: List<StudyGroup> = emptyList(),
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val studyGroupRepository: StudyGroupRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStudyGroups()
    }

    private fun loadStudyGroups() {
        viewModelScope.launch {
            userRepository.getUser("M2PzD8bxVaDAwNrLhr6E")  // TODO: getCurrentUser
                .onSuccess { currentUser ->
                    _uiState.update { it.copy(user = currentUser) }

                    val studyGroupIds = currentUser.studyGroups

                    studyGroupRepository.getStudyGroup(studyGroupIds)
                        .onSuccess { studyGroups ->
                            _uiState.update { it.copy(studyGroups = studyGroups) }
                        }
                        .onFailure {
                            Log.e("MainViewModel", "Failed to load study groups", it)
                            /* error */
                        }
                }
                .onFailure {
                    Log.e("MainViewModel", "Failed to load user", it)
                    /* error */
                }
        }
    }
}