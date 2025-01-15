package kr.boostcamp_2024.course.login.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.UserSubmitInfo
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.StorageRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import kr.boostcamp_2024.course.login.R
import kr.boostcamp_2024.course.login.model.UserUiModel
import kr.boostcamp_2024.course.login.navigation.CustomNavType.UserUiModelType
import kr.boostcamp_2024.course.login.navigation.SignUpRoute
import javax.inject.Inject
import kotlin.reflect.typeOf

data class SignUpUiState(
    val isLoading: Boolean = false,
    val userSubmitInfo: UserSubmitInfo = UserSubmitInfo(
        email = "",
        name = "",
        profileImageUrl = null,
        studyGroups = emptyList(),
    ),
    val profileImageByteArray: ByteArray? = null,
    val isSignUpSuccess: Boolean = false,
    val isEditMode: Boolean = false,
    val isSubmitSuccess: Boolean = false,
    val snackBarMessage: Int? = null,
) {
    val isSignUpButtonEnabled: Boolean = userSubmitInfo.email.isNotBlank() && userSubmitInfo.name.length in 1..20
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val userUiModel = savedStateHandle.toRoute<SignUpRoute>(mapOf(typeOf<UserUiModel?>() to UserUiModelType)).userUiModel
    private val userId = savedStateHandle.toRoute<SignUpRoute>(mapOf(typeOf<UserUiModel?>() to UserUiModelType)).userId

    private val _signUpUiState: MutableStateFlow<SignUpUiState> = MutableStateFlow(SignUpUiState())
    val signUpUiState: StateFlow<SignUpUiState> = _signUpUiState
        .onStart {
            if (userId != null) {
                loadUser()
            } else {
                setUserUiModel()
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            SignUpUiState(),
        )

    private fun setUserUiModel() {
        try {
            _signUpUiState.update {
                it.copy(
                    userSubmitInfo = UserSubmitInfo(
                        email = requireNotNull(userUiModel?.email),
                        name = requireNotNull(userUiModel?.name),
                        profileImageUrl = userUiModel?.profileImageUrl,
                    ),
                )
            }
        } catch (e: Exception) {
            Log.e("SignUpViewModel", "Failed to set userUiModel", e)
            setNewSnackBarMessage(R.string.error_login)
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            if (userId != null) {
                userRepository.getUser(userId).onSuccess {
                    _signUpUiState.update { currentState ->
                        currentState.copy(
                            userSubmitInfo = currentState.userSubmitInfo.copy(
                                email = it.email,
                                name = it.name,
                                profileImageUrl = it.profileUrl,
                            ),
                            isEditMode = true,
                        )
                    }
                }.onFailure {
                    Log.e("MainViewModel", "Failed to load user", it)
                }
            }
        }
    }

    fun onNameChanged(name: String) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                userSubmitInfo = currentState.userSubmitInfo.copy(name = name),
            )
        }
    }

    fun onProfileByteArrayChanged(profileImageByteArray: ByteArray?) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                profileImageByteArray = profileImageByteArray,
            )
        }
    }

    fun updateUser() {
        setLoading(true)
        viewModelScope.launch {
            if (userId != null) {
                val downloadUrl = uploadImage(_signUpUiState.value.profileImageByteArray)
                val userCreationInfo = UserSubmitInfo(
                    email = signUpUiState.value.userSubmitInfo.email,
                    name = signUpUiState.value.userSubmitInfo.name,
                    profileImageUrl = downloadUrl,
                )
                userRepository.updateUser(userId, userCreationInfo).onSuccess {
                    _signUpUiState.update {
                        it.copy(
                            isSubmitSuccess = true,
                            isLoading = false,
                        )
                    }
                }.onFailure {
                    Log.e("MainViewModel", "Failed to update user")
                    setLoading(false)
                }
            }
        }
    }

    fun signUp() {
        setLoading(true)
        viewModelScope.launch {
            val downloadUrl = uploadImage(_signUpUiState.value.profileImageByteArray)
            val userCreationInfo = UserSubmitInfo(
                email = signUpUiState.value.userSubmitInfo.email,
                name = signUpUiState.value.userSubmitInfo.name,
                profileImageUrl = downloadUrl,
            )
            userRepository.addUser(requireNotNull(userUiModel?.id), userCreationInfo)
                .onSuccess {
                    saveUserKey(requireNotNull(userUiModel?.id))
                }.onFailure {
                    Log.e("SignUpViewModel", "Failed to sign up", it)
                    setNewSnackBarMessage(R.string.error_sign_up)
                    setLoading(false)
                }
        }
    }

    private suspend fun saveUserKey(userKey: String) {
        authRepository.storeUserKey(userKey).onSuccess {
            _signUpUiState.update {
                it.copy(
                    isSignUpSuccess = true,
                    isLoading = false,
                )
            }
        }.onFailure {
            Log.e("LoginViewModel", "Failed to store user key")
        }
    }

    private suspend fun uploadImage(imageByteArray: ByteArray?): String? {
        imageByteArray?.let {
            storageRepository.uploadImage(imageByteArray)
                .onSuccess { downloadUrl ->
                    return downloadUrl
                }.onFailure {
                    Log.e("SignUpViewModel", "Failed to upload image", it)
                    setNewSnackBarMessage(R.string.error_upload_image)
                }
        }
        return null
    }

    private fun setLoading(isLoading: Boolean) {
        _signUpUiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    fun setNewSnackBarMessage(message: Int?) {
        _signUpUiState.update {
            it.copy(snackBarMessage = message)
        }
    }
}
