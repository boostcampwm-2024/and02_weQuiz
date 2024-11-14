package kr.boostcamp_2024.course.study

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.StudyGroupCreationInfo
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import javax.inject.Inject

data class CreateStudyUiState(
    val name: String = "",
    val description: String = "",
    val maxUserNum: Int = 0,
    val currentUserId: String? = null,
    val isCreateStudySuccess: Boolean = false,
    val snackBarMessage: String? = null,
) { // TODO 로그인 기능 구현 후 ownerId 수정
    val isCreateStudyButtonEnabled: Boolean
        get() = name.isNotBlank() && maxUserNum > 0
}

@HiltViewModel
class CreateStudyViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val studyGroupRepository: StudyGroupRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateStudyUiState())
    val uiState = _uiState
        .onStart {
            loadCurrentUserId()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), CreateStudyUiState())

    fun loadCurrentUserId() {
        viewModelScope.launch {

            authRepository.getUserKey()
                .onSuccess { currentUser ->

                    _uiState.update { it.copy(currentUserId = currentUser) }
                }
                .onFailure {
                    Log.e("MainViewModel", "Failed to load current user", it)
                    _uiState.update { it.copy(snackBarMessage = "로그인한 유저가 없습니다.") }
                }

        }
    }

    fun createStudyGroupClick() {
        viewModelScope.launch {
            uiState.value.currentUserId?.let { id ->
                val studyGroupCreationInfo = StudyGroupCreationInfo(
                    name = uiState.value.name,
                    description = uiState.value.description.takeIf { it.isNotBlank() },
                    maxUserNum = uiState.value.maxUserNum,
                    ownerId = id,
                )

                addStudyGroup(id, studyGroupCreationInfo)
            }
        }
    }

    fun addStudyGroup(currentUserId: String, studyGroupCreationInfo: StudyGroupCreationInfo) {
        viewModelScope.launch {
            studyGroupRepository.addStudyGroup(studyGroupCreationInfo)
                .onSuccess { studyId ->
                    Log.d("addStudyGroupResult", "성공, $studyId)")
                    addStudyGroupToUser(currentUserId, studyId)

                }.onFailure { throwable ->
                    Log.d("errorMessage", "${throwable.message}")
                    _uiState.update { it.copy(snackBarMessage = "스터디 그룹 생성 실패") }
                }
        }
    }

    fun addStudyGroupToUser(currentUserId: String, studyId: String) {
        viewModelScope.launch {
            userRepository.addStudyGroupToUser(currentUserId, studyId)
                .onSuccess { result ->
                    Log.d("addStudyGroupToUserResult", "성공, $result)")
                    _uiState.update { it.copy(isCreateStudySuccess = true) }
                }
                .onFailure { throwable ->
                    Log.d("addStudyGroupToUserResult", "실패, ${throwable.message})")
                    _uiState.update { it.copy(snackBarMessage = "스터디 그룹 생성 실패") }
                }
        }
    }

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onOptionSelected(option: Int) {
        val maxUserNum = option + 1
        _uiState.update { it.copy(maxUserNum = maxUserNum) }
    }

    fun onSnackBarShown() {
        _uiState.update { it.copy(snackBarMessage = null) }
    }

}
