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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kr.boostcamp_2024.course.domain.model.UserSubmitInfo
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.StorageRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import kr.boostcamp_2024.course.login.R
import kr.boostcamp_2024.course.login.model.UserUiModel
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

data class SignUpUiState(
    val isLoading: Boolean = false,
    val userSubmitInfo: UserSubmitInfo = UserSubmitInfo(
        email = "",
        name = "",
        profileImageUrl = null,
        studyGroups = emptyList(),
    ),
    val isSignUpSuccess: Boolean = false,
    val isEditMode: Boolean = false,
    val isSubmitSuccess: Boolean = false,
    val snackBarMessage: Int? = null,
) {
    val isSignUpButtonEnabled: Boolean
        get() = userSubmitInfo.email.isNotBlank() &&
            userSubmitInfo.name.isNotBlank() &&
            isEmailValid
    val isEmailValid: Boolean
        get() = userSubmitInfo.email.isNotBlank() &&
            Patterns.EMAIL_ADDRESS.matcher(userSubmitInfo.email)
                .matches()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository,
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val userUiModel = savedStateHandle.get<String>("userUiModel?")?.let {
        Json.decodeFromString(UserUiModel.serializer(), it)
    }
    private val userId: String? = savedStateHandle.get<String>("userId?")
    private val _signUpUiState: MutableStateFlow<SignUpUiState> = MutableStateFlow(SignUpUiState())
    val signUpUiState: StateFlow<SignUpUiState> = _signUpUiState.onStart {
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

    fun onEmailChanged(email: String) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                userSubmitInfo = currentState.userSubmitInfo.copy(email = email),
            )
        }
    }

    fun onNameChanged(name: String) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                userSubmitInfo = currentState.userSubmitInfo.copy(name = name),
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

    fun signUp() {
        setLoading(true)
        viewModelScope.launch {
            val imageByteArray = withContext(Dispatchers.IO) {
                signUpUiState.value.userSubmitInfo.profileImageUrl?.let { uriToByteArray(Uri.parse(it)) }
            }
            val downloadUrl = uploadImage(imageByteArray)
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

    private fun uriToByteArray(
        uri: Uri,
    ): ByteArray {
        val inputStream = if (uri.scheme == "http" || uri.scheme == "https") {
            downloadImage(uri.toString())
        } else {
            context.contentResolver.openInputStream(uri)
        }
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        return baos.toByteArray()
    }

    private fun downloadImage(urlString: String): InputStream? =
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("SignUpViewModel", "Failed to download image", e)
            null
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
