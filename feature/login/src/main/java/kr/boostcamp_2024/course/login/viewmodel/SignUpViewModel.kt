package kr.boostcamp_2024.course.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kr.boostcamp_2024.course.domain.model.UserCreationInfo
import javax.inject.Inject

data class SignUpUiState(
    val userCreationInfo: UserCreationInfo = UserCreationInfo(
        email = "",
        password = "",
        nickName = "",
        profileImage = null,
    ),
    val isSignUpValid: Boolean = false,
    val isEmailValid: Boolean = true,
)

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {
    private val _signUpUiState: MutableStateFlow<SignUpUiState> = MutableStateFlow(SignUpUiState())
    val signUpUiState: StateFlow<SignUpUiState> = _signUpUiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                userCreationInfo = currentState.userCreationInfo.copy(email = email),
            )
        }
        checkIsSignUpValid()
        checkIsEmailValid()
    }

    fun onPasswordChanged(password: String) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                userCreationInfo = currentState.userCreationInfo.copy(password = password),
            )
        }
        checkIsSignUpValid()
    }

    fun onNickNameChanged(nickName: String) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                userCreationInfo = currentState.userCreationInfo.copy(nickName = nickName),
            )
        }
        checkIsSignUpValid()
    }

    fun onProfileUriChanged(profileUri: String) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                userCreationInfo = currentState.userCreationInfo.copy(profileImage = profileUri),
            )
        }
    }

    private fun checkIsSignUpValid() {
        val currentState = _signUpUiState.value
        val isSignUpValid = currentState.userCreationInfo.email.isNotBlank() &&
            currentState.userCreationInfo.password.isNotBlank() &&
            currentState.userCreationInfo.nickName.isNotBlank() &&
            currentState.isEmailValid
        _signUpUiState.update { currentState.copy(isSignUpValid = isSignUpValid) }
    }

    private fun checkIsEmailValid() {
        val currentState = _signUpUiState.value
        val isEmailValid = currentState.userCreationInfo.email.isNotBlank() &&
            Patterns.EMAIL_ADDRESS.matcher(currentState.userCreationInfo.email).matches()
        _signUpUiState.update { currentState.copy(isEmailValid = isEmailValid) }
    }
}
