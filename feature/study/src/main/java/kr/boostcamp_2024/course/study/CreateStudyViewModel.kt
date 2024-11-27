package kr.boostcamp_2024.course.study

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.StudyGroupCreationInfo
import kr.boostcamp_2024.course.domain.model.StudyGroupUpdatedInfo
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.StorageRepository
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import kr.boostcamp_2024.course.study.navigation.CreateStudyRoute
import javax.inject.Inject
import kotlin.String

data class CreateStudyUiState(
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false,
    val defaultImageUri: String? = null,
    val currentImage: ByteArray? = null,
    val name: String = "",
    val description: String = "",
    val maxUserNum: String = "",
    val currentUserId: String? = null,
    val isSubmitStudySuccess: Boolean = false,
    val snackBarMessage: String? = null,
) {
    val canSubmitStudy: Boolean = (name.isNotBlank() && maxUserNum.isNotBlank() && maxUserNum.toInt() in 2..50)
}

@HiltViewModel
class CreateStudyViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val studyGroupRepository: StudyGroupRepository,
    private val userRepository: UserRepository,
    private val storageRepository: StorageRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val studyGroupId: String? = savedStateHandle.toRoute<CreateStudyRoute>().studyGroupId

    private val _uiState = MutableStateFlow(CreateStudyUiState())
    val uiState = _uiState
        .onStart {
            loadCurrentUserId()
            loadStudyGroup()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), CreateStudyUiState())

    fun loadCurrentUserId() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            authRepository.getUserKey().onSuccess { currentUser ->

                _uiState.update { it.copy(isLoading = false, currentUserId = currentUser) }
            }.onFailure {
                Log.e("CreateStudyViewModel", "Failed to load current user", it)
                _uiState.update { it.copy(isLoading = false, snackBarMessage = "로그인한 유저가 없습니다.") }
            }

        }
    }

    fun loadStudyGroup() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            studyGroupId?.let {
                studyGroupRepository.getStudyGroup(it)
                    .onSuccess { studyGroup ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isEditMode = true,
                                defaultImageUri = studyGroup.studyGroupImageUrl,
                                name = studyGroup.name,
                                description = studyGroup.description ?: "",
                                maxUserNum = studyGroup.maxUserNum.toString(),
                            )
                        }
                    }.onFailure {
                        Log.e("CreateStudyViewModel", "Failed to load study group", it)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                snackBarMessage = "스터디 그룹 로드 실패",
                            )
                        }
                    }
            }
        }
    }

    fun createStudyGroupClick() {
        if (uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            uiState.value.currentUserId?.let { id ->
                val downloadUrl = uiState.value.currentImage?.let { imageByteArray ->
                    storageRepository.uploadImage(imageByteArray).getOrNull()
                }

                val studyGroupCreationInfo = StudyGroupCreationInfo(
                    studyGroupImageUrl = downloadUrl,
                    name = uiState.value.name,
                    description = uiState.value.description.takeIf { it.isNotBlank() },
                    maxUserNum = uiState.value.maxUserNum.toInt(),
                    ownerId = id,
                )
                addStudyGroup(id, studyGroupCreationInfo)
            }
        }
    }

    fun updateStudyGroup() {
        if (uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            studyGroupId?.let {
                val downloadUrl = uiState.value.currentImage?.let { imageByteArray ->
                    uiState.value.defaultImageUri?.let { defaultUri ->
                        storageRepository.deleteImage(defaultUri)
                    }
                    storageRepository.uploadImage(imageByteArray).getOrNull()
                } ?: uiState.value.defaultImageUri

                val studyGroupUpdatedInfo = StudyGroupUpdatedInfo(
                    studyGroupImageUrl = downloadUrl,
                    name = uiState.value.name,
                    description = uiState.value.description.takeIf { it.isNotBlank() },
                    maxUserNum = uiState.value.maxUserNum.toInt(),
                )

                studyGroupRepository.updateStudyGroup(it, studyGroupUpdatedInfo)
                    .onSuccess {
                        Log.d("MainViewModel", "Successfully updated study group")
                        _uiState.update { it.copy(isLoading = true, isSubmitStudySuccess = true) }
                    }
                    .onFailure {
                        Log.e("MainViewModel", "Failed to update study group", it)
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                snackBarMessage = "스터디 그룹 업데이트 실패",
                            )
                        }
                    }
            }
        }
    }

    fun addStudyGroup(currentUserId: String, studyGroupCreationInfo: StudyGroupCreationInfo) {
        viewModelScope.launch {
            studyGroupRepository.addStudyGroup(studyGroupCreationInfo).onSuccess { studyId ->
                Log.d("addStudyGroupResult", "성공, $studyId)")
                addStudyGroupToUser(currentUserId, studyId)

            }.onFailure { throwable ->
                Log.e("errorMessage", "${throwable.message}")
                _uiState.update { it.copy(isLoading = false, snackBarMessage = "스터디 그룹 생성 실패") }
            }
        }
    }

    fun addStudyGroupToUser(currentUserId: String, studyId: String) {
        viewModelScope.launch {
            userRepository.addStudyGroupToUser(currentUserId, studyId).onSuccess { result ->
                Log.d("addStudyGroupToUserResult", "성공, $result)")
                _uiState.update { it.copy(isLoading = false, isSubmitStudySuccess = true) }
            }.onFailure { throwable ->
                Log.e("addStudyGroupToUserResult", "실패, ${throwable.message})")
                _uiState.update { it.copy(isLoading = false, snackBarMessage = "스터디 그룹 생성 실패") }
            }
        }
    }

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onMaxUserNumChange(groupMemberNumber: String) {
        groupMemberNumber.toIntOrNull()?.takeIf { it in 2..50 }?.let {
            _uiState.update { it.copy(maxUserNum = groupMemberNumber) }
        }
    }

    fun onImageByteArrayChanged(imageByteArray: ByteArray) {
        _uiState.update { it.copy(currentImage = imageByteArray) }
    }

    fun onSnackBarShown() {
        _uiState.update { it.copy(snackBarMessage = null) }
    }
}
