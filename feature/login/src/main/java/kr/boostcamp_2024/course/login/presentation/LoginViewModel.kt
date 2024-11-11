package kr.boostcamp_2024.course.login.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.login.BuildConfig
import javax.inject.Inject

sealed interface LoginUiState {
    data object Loading : LoginUiState
    data object Success : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _loginUiState: MutableStateFlow<LoginUiState> =
        MutableStateFlow(LoginUiState.Loading)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun loginForExperience() {
        viewModelScope.launch {
            val defaultUserKey = BuildConfig.DEFAULT_USER_KEY
            authRepository.storeUserKey(defaultUserKey)
                .onSuccess {
                    _loginUiState.value = LoginUiState.Success
                }.onFailure {
                    Log.e("LoginViewModel", "Failed to store user key")
                }
        }
    }
}