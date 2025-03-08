package kr.boostcamp_2024.course.study

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
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
    val loadedMaxUserNum: String = "",
) {
    val canSubmitStudy: Boolean = (name.length in 1..20 && description.length in 0..100 && maxUserNum.isNotBlank() && maxUserNum.toInt() in 2..50)
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

    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()

    private fun loadCurrentUserId() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val currentUser = authRepository.getUserKey()
                _uiState.update { it.copy(isLoading = false, currentUserId = currentUser) }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadStudyGroup() {
        viewModelScope.launch {
            try {
                studyGroupId?.let {
                    _uiState.update { it.copy(isLoading = true) }

                    val studyGroup = studyGroupRepository.getStudyGroup(it)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isEditMode = true,
                            defaultImageUri = studyGroup.studyGroupImageUrl,
                            name = studyGroup.name,
                            description = studyGroup.description ?: "",
                            maxUserNum = studyGroup.maxUserNum.toString(),
                            loadedMaxUserNum = studyGroup.maxUserNum.toString(),
                        )
                    }
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun createStudyGroupClick() {
        if (uiState.value.isLoading) return

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                uiState.value.currentUserId?.let { id ->
                    val downloadUrl = uiState.value.currentImage?.let { imageByteArray ->
                        storageRepository.uploadImage(imageByteArray)
                    }

                    val studyGroupCreationInfo = StudyGroupCreationInfo(
                        studyGroupImageUrl = downloadUrl,
                        name = uiState.value.name,
                        description = uiState.value.description.takeIf { it.isNotBlank() },
                        maxUserNum = uiState.value.maxUserNum.toInt(),
                        ownerId = id,
                    )

                    val studyId = studyGroupRepository.addStudyGroup(studyGroupCreationInfo)
                    userRepository.addStudyGroupToUser(id, studyId)

                    _uiState.update { it.copy(isLoading = false, isSubmitStudySuccess = true) }
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun updateStudyGroup() {
        if (uiState.value.isLoading) return

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                studyGroupId?.let { studyGroupId ->
                    val downloadUrl = uiState.value.currentImage?.let { imageByteArray ->
                        uiState.value.defaultImageUri?.let { defaultUri ->
                            storageRepository.deleteImage(defaultUri)
                        }
                        storageRepository.uploadImage(imageByteArray)
                    } ?: uiState.value.defaultImageUri

                    val studyGroupUpdatedInfo = StudyGroupUpdatedInfo(
                        studyGroupImageUrl = downloadUrl,
                        name = uiState.value.name,
                        description = uiState.value.description.takeIf { it.isNotBlank() },
                        maxUserNum = uiState.value.maxUserNum.toInt(),
                    )

                    if (studyGroupUpdatedInfo.maxUserNum < _uiState.value.loadedMaxUserNum.toInt()) {
                        throw Exception("스터디 인원은 수정 전보다 많거나 같아야 합니다.")
                    } else {
                        studyGroupRepository.updateStudyGroup(studyGroupId, studyGroupUpdatedInfo)
                        _uiState.update { it.copy(isLoading = false, isSubmitStudySuccess = true) }
                    }
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
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
        if (groupMemberNumber.isBlank() || groupMemberNumber.toIntOrNull() != null) {
            _uiState.update { it.copy(maxUserNum = groupMemberNumber) }
        }
    }

    fun onImageByteArrayChanged(imageByteArray: ByteArray) {
        _uiState.update { it.copy(currentImage = imageByteArray) }
    }
}
