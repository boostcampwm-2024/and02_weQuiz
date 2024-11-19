package kr.boostcamp_2024.course.login.viewmodel

import android.util.Log
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import kr.boostcamp_2024.course.login.model.UserUiModel
import javax.inject.Inject

data class LoginUiState(
    val isLoginSuccess: Boolean = false,
    val snackBarMessage: Int? = null,
    val userInfo: UserUiModel? = null,
    val isNewUser: Boolean = false,
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _loginUiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun loginForExperience() {
        viewModelScope.launch {
            val defaultUserKey = "M2PzD8bxVaDAwNrLhr6E"
            saveUserKey(defaultUserKey)
        }
    }

    fun handleSignIn(
        result: GetCredentialResponse,
        errorMessage: Int,
    ) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        checkUser(googleIdTokenCredential)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("LoginScreen", "Received an invalid google id token response", e)
                        setNewSnackBarMessage(errorMessage)
                    }
                } else {
                    Log.e("LoginScreen", "Unexpected type of credential")
                    setNewSnackBarMessage(errorMessage)
                }
            }

            else -> {
                Log.e("LoginScreen", "Unexpected type of credential")
                setNewSnackBarMessage(errorMessage)
            }
        }
    }

    private fun checkUser(googleIdTokenCredential: GoogleIdTokenCredential) {
        viewModelScope.launch {
            userRepository.getUser(googleIdTokenCredential.idToken)
                .onSuccess {
                    // 이미 회원가입된 유저
                    saveUserKey(googleIdTokenCredential.idToken)
                    _loginUiState.update { currentState ->
                        currentState.copy(isLoginSuccess = true)
                    }
                }
                .onFailure {
                    // 회원 가입 필요
                    _loginUiState.update { currentState ->
                        currentState.copy(
                            userInfo = UserUiModel(
                                id = googleIdTokenCredential.idToken,
                                email = googleIdTokenCredential.id,
                                name = googleIdTokenCredential.familyName + googleIdTokenCredential.givenName,
                                profileImageUrl = googleIdTokenCredential.profilePictureUri.toString(),
                            ),
                            isNewUser = true,
                        )
                    }
                }
        }
    }

    private suspend fun saveUserKey(userKey: String) {
        authRepository.storeUserKey(userKey).onSuccess {
            _loginUiState.update { currentState ->
                currentState.copy(isLoginSuccess = true)
            }
        }.onFailure {
            Log.e("LoginViewModel", "Failed to store user key")
        }
    }

    fun setNewSnackBarMessage(message: Int?) {
        _loginUiState.update { currentState ->
            currentState.copy(snackBarMessage = message)
        }
    }
}
