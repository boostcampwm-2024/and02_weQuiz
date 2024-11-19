package kr.boostcamp_2024.course.login.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kr.boostcamp_2024.course.domain.model.UserCreationInfo
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.StorageRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import kr.boostcamp_2024.course.login.R
import kr.boostcamp_2024.course.login.model.UserUiModel
import java.io.ByteArrayOutputStream
import javax.inject.Inject

data class SignUpUiState(
    val isLoading: Boolean = false,
    val userCreationInfo: UserCreationInfo = UserCreationInfo(
        email = "",
        name = "",
        profileImageUrl = null,
    ),
    val isSignUpSuccess: Boolean = false,
    val isSignUpValid: Boolean = false,
    val isEmailValid: Boolean = true,
    val snackBarMessage: Int? = null,
)

@HiltViewModel
class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository,
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val userUiModel = requireNotNull(
        savedStateHandle.get<String>("userUiModel")?.let { string ->
            Json.decodeFromString<UserUiModel>(string)
        },
    ) { "UserUiModel is required" }
    private val _signUpUiState: MutableStateFlow<SignUpUiState> = MutableStateFlow(
        SignUpUiState(
            userCreationInfo = UserCreationInfo(
                email = userUiModel.email,
                name = userUiModel.name,
                profileImageUrl = userUiModel.profileImageUrl,
            ),
            isEmailValid = Patterns.EMAIL_ADDRESS.matcher(userUiModel.email).matches(),
            isSignUpValid = userUiModel.email.isNotBlank() && userUiModel.name.isNotBlank(),
        ),
    )
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

    fun onNameChanged(nickName: String) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                userCreationInfo = currentState.userCreationInfo.copy(name = nickName),
            )
        }
        checkIsSignUpValid()
    }

    fun onProfileUriChanged(profileUri: String) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                userCreationInfo = currentState.userCreationInfo.copy(profileImageUrl = profileUri),
            )
        }
    }

    private fun checkIsSignUpValid() {
        val currentState = _signUpUiState.value
        val isSignUpValid = currentState.userCreationInfo.email.isNotBlank() &&
            currentState.userCreationInfo.name.isNotBlank() &&
            currentState.isEmailValid
        _signUpUiState.update { currentState.copy(isSignUpValid = isSignUpValid) }
    }

    private fun checkIsEmailValid() {
        val currentState = _signUpUiState.value
        val isEmailValid = currentState.userCreationInfo.email.isNotBlank() &&
            Patterns.EMAIL_ADDRESS.matcher(currentState.userCreationInfo.email).matches()
        _signUpUiState.update { currentState.copy(isEmailValid = isEmailValid) }
    }

    fun signUp() {
        setLoading()
        viewModelScope.launch {
            val imageByteArray = signUpUiState.value.userCreationInfo.profileImageUrl?.let { uriToByteArray(Uri.parse(it)) }
            val downloadUrl = uploadImage(imageByteArray)
            val userCreationInfo = UserCreationInfo(
                email = signUpUiState.value.userCreationInfo.email,
                name = signUpUiState.value.userCreationInfo.name,
                profileImageUrl = downloadUrl,
            )
            userRepository.addUser(userUiModel.id, userCreationInfo)
                .onSuccess {
                    saveUserKey(userUiModel.id)
                }.onFailure {
                    Log.e("SignUpViewModel", "Failed to sign up", it)
                    setNewSnackBarMessage(R.string.error_sign_up)
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

    private fun uriToByteArray(
        uri: Uri,
    ): ByteArray {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        return baos.toByteArray()
    }

    private fun setLoading() {
        _signUpUiState.update {
            it.copy(isLoading = true)
        }
    }

    fun setNewSnackBarMessage(message: Int?) {
        _signUpUiState.update {
            it.copy(snackBarMessage = message)
        }
    }
}
