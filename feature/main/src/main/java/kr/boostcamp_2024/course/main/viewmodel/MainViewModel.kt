package kr.boostcamp_2024.course.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import javax.inject.Inject

data class MainUiState(
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val studyGroups: List<StudyGroup> = emptyList(),
    val errorMessage: String? = null,
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val studyGroupRepository: StudyGroupRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadStudyGroups()
    }

    private fun loadStudyGroups() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // TODO: getCurrentUser
            userRepository.getUser("M2PzD8bxVaDAwNrLhr6E")
                .onSuccess { currentUser ->
                    val studyGroupIds = currentUser.studyGroups

                    studyGroupRepository.getStudyGroups(studyGroupIds)
                        .onSuccess { studyGroups ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    currentUser = currentUser,
                                    studyGroups = studyGroups,
                                )
                            }
                        }
                        .onFailure {
                            Log.e("MainViewModel", "Failed to load study groups", it)
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = "스터디 그룹 로드에 실패했습니다.",
                                )
                            }
                        }
                }
                .onFailure {
                    Log.e("MainViewModel", "Failed to load user", it)
                    _uiState.update { it.copy(isLoading = false, errorMessage = "유저 로드에 실패했습니다.") }
                }
        }
    }

    fun shownErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
