package kr.boostcamp_2024.course.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.domain.repository.AuthRepository
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
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val studyGroupRepository: StudyGroupRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState
        .onStart {
            loadCurrentUser()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), MainUiState())

    fun loadCurrentUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            authRepository.getUserKey()
                .onSuccess { currentUserId ->

                    userRepository.getUser(currentUserId)
                        .onSuccess { currentUser ->
                            loadStudyGroups(currentUser)
                        }
                        .onFailure {
                            Log.e("MainViewModel", "Failed to load user", it)
                            _uiState.update { it.copy(isLoading = false, errorMessage = "유저 로드에 실패했습니다.") }
                        }
                }
                .onFailure {
                    Log.e("MainViewModel", "Failed to load current user", it)
                    _uiState.update { it.copy(isLoading = false, errorMessage = "로그인한 유저가 없습니다.") }
                }

        }
    }

    private fun loadStudyGroups(currentUser: User) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
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
    }

    fun editStudyGroup(studyGroupId: String) {

    }

    fun leaveStudyGroup(studyGroupId: String) {

    }

    fun shownErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
