package kr.boostcamp_2024.course.login.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import javax.inject.Inject

data class LoginUiState(
    val isLoginSuccess: Boolean = false,
    val snackBarMessage: String? = null,
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _loginUiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun loginForExperience() {
        viewModelScope.launch {
            val defaultUserKey = "M2PzD8bxVaDAwNrLhr6E"
            authRepository.storeUserKey(defaultUserKey).onSuccess {
                _loginUiState.update { currentState ->
                    currentState.copy(isLoginSuccess = true)
                }
            }.onFailure {
                Log.e("LoginViewModel", "Failed to store user key")
            }
        }
    }

    fun setNewSnackBarMessage(message: String?) {
        _loginUiState.update { currentState ->
            currentState.copy(snackBarMessage = message)
        }
    }
}
