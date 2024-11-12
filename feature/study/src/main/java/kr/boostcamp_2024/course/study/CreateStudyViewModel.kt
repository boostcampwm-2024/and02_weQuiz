package kr.boostcamp_2024.course.study

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.StudyGroupCreationInfo
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import javax.inject.Inject

data class CreateStudyUiState(
    val name: String = "",
    val description: String = "",
    val maxUserNum: Int = 0,
    val ownerId: String = "M2PzD8bxVaDAwNrLhr6E", /*TODO 로그인 기능 구현 후 수정*/
    val isCreateStudySuccess: Boolean = false,
    val snackBarMessage: String? = null
)

@HiltViewModel
class CreateStudyViewModel @Inject constructor(
    private val studyGroupRepository: StudyGroupRepository
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(CreateStudyUiState())
    val uiState = _uiState.asStateFlow()

    private val _selectedOption: MutableStateFlow<String> = MutableStateFlow("")
    val selectedOption: StateFlow<String> = _selectedOption

    fun onCreateStudyGroupClick(
        onCreateGroupSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val studyGroupRequest = StudyGroupCreationInfo(
                name = uiState.value.name,
                description = uiState.value.description,
                maxUserNum = uiState.value.maxUserNum,
                ownerId = uiState.value.ownerId
            )
            val addStudyGroupResult = studyGroupRepository.addStudyGroup(studyGroupRequest)
            addStudyGroupResult
                .onSuccess { studyId ->
                    _uiState.value = uiState.value.copy(isCreateStudySuccess = true)
                    Log.d(
                        "addStudyGroupResult",
                        "성공, $studyId)"
                    )
                    val addStudyGroupToUserResult =
                        studyGroupRepository.addStudyGroupToUser(uiState.value.ownerId, studyId)
                    addStudyGroupToUserResult
                        .onSuccess { result ->
                            Log.d(
                                "addStudyGroupToUserResult",
                                "성공, $result)"
                            )
                            onCreateGroupSuccess()
                            /*TODO Toast 추가*/
                        }
                        .onFailure { throwable ->
                            Log.d(
                                "addStudyGroupToUserResult",
                                "실패, ${throwable.message})"
                            )
                            /*TODO 스낵바*/
                            _uiState.update {
                                it.copy(
                                    isCreateStudySuccess = false,
                                    snackBarMessage = "스터디 그룹 생성 실패"
                                )
                            }
                        }
                }.onFailure { throwable ->
                    Log.d("errorMessage", "${throwable.message}")
                    /*TODO 스낵바*/
                    _uiState.update {
                        it.copy(
                            isCreateStudySuccess = false,
                            snackBarMessage = "스터디 그룹 생성 실패"
                        )
                    }
                }
        }
    }

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onOptionSelected(option: String) {
        _selectedOption.value = option
        val maxUserNum = when (option) {
            "1명" -> 1
            "2명" -> 2
            "3명" -> 3
            "4명" -> 4
            "5명" -> 5
            "6명" -> 6
            "7명" -> 7
            "8명" -> 8
            "9명" -> 9
            "10명" -> 10
            else -> 0
        }
        _uiState.update { it.copy(maxUserNum = maxUserNum) }
    }

    fun onSnackBarShown() {
        _uiState.update { it.copy(snackBarMessage = null) }
    }
}