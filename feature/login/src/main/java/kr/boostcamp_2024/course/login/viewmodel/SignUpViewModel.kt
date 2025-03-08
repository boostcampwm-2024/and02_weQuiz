package kr.boostcamp_2024.course.login.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.UserSubmitInfo
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.StorageRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
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
    val isEditMode: Boolean = false,
    val isSubmitSuccess: Boolean = false,
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

    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()

    private fun setUserUiModel() {
        _signUpUiState.update {
            it.copy(
                userSubmitInfo = UserSubmitInfo(
                    email = requireNotNull(userUiModel?.email),
                    name = requireNotNull(userUiModel?.name),
                    profileImageUrl = userUiModel?.profileImageUrl,
                ),
            )
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            try {
                userId?.let {
                    val user = userRepository.getUser(userId)
                    _signUpUiState.update { currentState ->
                        currentState.copy(
                            userSubmitInfo = currentState.userSubmitInfo.copy(
                                email = user.email,
                                name = user.name,
                                profileImageUrl = user.profileUrl,
                            ),
                            isEditMode = true,
                        )
                    }
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
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
        viewModelScope.launch {
            try {
                userId?.let {
                    setLoading(true)
                    val downloadUrl = uploadImage(_signUpUiState.value.profileImageByteArray)
                    val userCreationInfo = UserSubmitInfo(
                        email = signUpUiState.value.userSubmitInfo.email,
                        name = signUpUiState.value.userSubmitInfo.name,
                        profileImageUrl = downloadUrl ?: signUpUiState.value.userSubmitInfo.profileImageUrl,
                    )
                    userRepository.updateUser(userId, userCreationInfo)
                    _signUpUiState.update {
                        it.copy(
                            isSubmitSuccess = true,
                            isLoading = false,
                        )
                    }
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                setLoading(false)
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            try {
                authRepository.login(requireNotNull(userUiModel?.id))

                val downloadUrl = uploadImage(_signUpUiState.value.profileImageByteArray)
                val userCreationInfo = UserSubmitInfo(
                    email = signUpUiState.value.userSubmitInfo.email,
                    name = signUpUiState.value.userSubmitInfo.name,
                    profileImageUrl = downloadUrl ?: signUpUiState.value.userSubmitInfo.profileImageUrl,
                )
                userRepository.addUser(authRepository.getUserKey(), userCreationInfo)

                _signUpUiState.update {
                    it.copy(
                        isSubmitSuccess = true,
                        isLoading = false,
                    )
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                setLoading(true)
            }
        }
    }

    private suspend fun uploadImage(imageByteArray: ByteArray?): String? {
        imageByteArray?.let {
            return storageRepository.uploadImage(imageByteArray)
        } ?: return null
    }

    private fun setLoading(isLoading: Boolean) {
        _signUpUiState.update {
            it.copy(isLoading = isLoading)
        }
    }
}
