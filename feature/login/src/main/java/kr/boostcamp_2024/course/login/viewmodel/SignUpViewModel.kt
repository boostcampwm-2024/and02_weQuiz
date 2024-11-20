package kr.boostcamp_2024.course.login.viewmodel

import android.util.Log
import android.util.Patterns
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
import kr.boostcamp_2024.course.domain.repository.UserRepository
import kr.boostcamp_2024.course.login.navigation.SignUpRoute
import javax.inject.Inject

data class SignUpUiState(
    val userSubmitInfo: UserSubmitInfo = UserSubmitInfo(
        email = "",
        nickName = "",
        profileImageUrl = null,
        studyGroups = emptyList(),
    ),
    val isEditMode: Boolean = false,
    val isSubmitSuccess: Boolean = false,
) {
    val isSignUpButtonEnabled: Boolean
        get() = userSubmitInfo.email.isNotBlank() &&
            userSubmitInfo.nickName.isNotBlank() &&
            isEmailValid
    val isEmailValid: Boolean
        get() = userSubmitInfo.email.isNotBlank() &&
            Patterns.EMAIL_ADDRESS.matcher(userSubmitInfo.email)
                .matches()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _signUpUiState: MutableStateFlow<SignUpUiState> = MutableStateFlow(SignUpUiState())
    val signUpUiState: StateFlow<SignUpUiState> = _signUpUiState.onStart {
        loadUser()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SignUpUiState())
    private val userId = savedStateHandle.toRoute<SignUpRoute>().userId

    private fun loadUser() {
        viewModelScope.launch {
            if (userId != null) {
                userRepository.getUser(userId).onSuccess {
                    _signUpUiState.update { currentState ->
                        currentState.copy(
                            userSubmitInfo = currentState.userSubmitInfo.copy(
                                email = it.email,
                                nickName = it.name,
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

    fun addUser() {
        viewModelScope.launch {
            if (userId != null) {
                userRepository.addUser(userId, _signUpUiState.value.userSubmitInfo).onSuccess {
                    _signUpUiState.update {
                        it.copy(isSubmitSuccess = true)
                    }
                }.onFailure {
                    Log.e("MainViewModel", "Failed to add user")
                    _signUpUiState.update {
                        it.copy(isSubmitSuccess = false)
                    }
                }
            }
        }
    }

    fun updateUser() {
        viewModelScope.launch {
            if (userId != null) {
                userRepository.updateUser(userId, _signUpUiState.value.userSubmitInfo).onSuccess {
                    _signUpUiState.update {
                        it.copy(isSubmitSuccess = true)
                    }
                }.onFailure {
                    Log.e("MainViewModel", "Failed to update user")
                    _signUpUiState.update {
                        it.copy(isSubmitSuccess = false)
                    }
                }
            }
        }
    }

    fun onEmailChanged(email: String) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                userSubmitInfo = currentState.userSubmitInfo.copy(email = email),
            )
        }
    }

    fun onNickNameChanged(nickName: String) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                userSubmitInfo = currentState.userSubmitInfo.copy(nickName = nickName),
            )
        }
    }

    fun onProfileUriChanged(profileUri: String) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                userSubmitInfo = currentState.userSubmitInfo.copy(profileImageUrl = profileUri),
            )
        }
    }
}
