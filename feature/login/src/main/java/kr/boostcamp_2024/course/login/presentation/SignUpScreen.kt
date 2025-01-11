package kr.boostcamp_2024.course.login.presentation

import WeQuizPhotoPickerAsyncImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizCircularProgressIndicator
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizValidateTextField
import kr.boostcamp_2024.course.domain.model.UserSubmitInfo
import kr.boostcamp_2024.course.login.R
import kr.boostcamp_2024.course.login.viewmodel.SignUpViewModel

@Composable
internal fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigationButtonClick: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.signUpUiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(uiState) {
        uiState.snackBarMessage?.let {
            snackBarHostState.showSnackbar(context.getString(it))
            viewModel.setNewSnackBarMessage(null)
        }
        if (uiState.isSignUpSuccess) {
            onSignUpSuccess()
        }
    }

    SignUpScreen(
        isLoading = uiState.isLoading,
        userSubmitInfo = uiState.userSubmitInfo,
        isSignUpButtonEnabled = uiState.isSignUpButtonEnabled,
        isEditMode = uiState.isEditMode,
        profileImageByteArray = uiState.profileImageByteArray,
        snackBarHostState = snackBarHostState,
        onNameChanged = viewModel::onNameChanged,
        onNavigationButtonClick = onNavigationButtonClick,
        onSignUpButtonClick = viewModel::signUp,
        onEditButtonClick = viewModel::updateUser,
        onProfileByteArrayChanged = viewModel::onProfileByteArrayChanged,
    )

    LaunchedEffect(uiState.isSubmitSuccess) {
        if (uiState.isSubmitSuccess) {
            onSignUpSuccess()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpScreen(
    isLoading: Boolean,
    userSubmitInfo: UserSubmitInfo,
    isSignUpButtonEnabled: Boolean,
    isEditMode: Boolean,
    profileImageByteArray: ByteArray?,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNameChanged: (String) -> Unit,
    onProfileByteArrayChanged: (ByteArray) -> Unit,
    onNavigationButtonClick: () -> Unit,
    onSignUpButtonClick: () -> Unit,
    onEditButtonClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (isEditMode) {
                            true -> stringResource(R.string.top_app_bar_edit_user)
                            false -> stringResource(R.string.top_app_bar_sign_up)
                        },
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigationButtonClick,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.des_ic_back),
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                start = 16.dp,
                end = 16.dp,
                bottom = innerPadding.calculateBottomPadding(),
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                SignUpContent(
                    email = userSubmitInfo.email,
                    name = userSubmitInfo.name,
                    profileUri = userSubmitInfo.profileImageUrl,
                    profileImageByteArray = profileImageByteArray,
                    onNameChanged = onNameChanged,
                    onProfileByteArrayChanged = onProfileByteArrayChanged,
                )
            }
            item {
                SignUpButtons(
                    isSignUpValid = isSignUpButtonEnabled && !isLoading,
                    onSubmitButtonClick = if (isEditMode) onEditButtonClick else onSignUpButtonClick,
                    isEditMode = isEditMode,
                )
            }
        }
        if (isLoading) {
            WeQuizCircularProgressIndicator()
        }
    }
}

@Composable
fun SignUpContent(
    email: String,
    name: String,
    profileUri: String?,
    profileImageByteArray: ByteArray?,
    onNameChanged: (String) -> Unit,
    onProfileByteArrayChanged: (ByteArray) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        WeQuizPhotoPickerAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(18.dp)),
            imageData = profileImageByteArray ?: profileUri,
            onImageDataChanged = onProfileByteArrayChanged,
        )

        TextField(
            value = email,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
        )

        WeQuizValidateTextField(
            label = stringResource(R.string.txt_sign_up_nick_name),
            text = name,
            onTextChanged = onNameChanged,
            placeholder = stringResource(R.string.txt_sign_up_nick_name_placeholder),
            errorMessage = stringResource(R.string.txt_user_name_error_message),
            validFun = { it.length <= 10 },
        )
    }
}

@Composable
fun SignUpButtons(
    isSignUpValid: Boolean,
    onSubmitButtonClick: () -> Unit,
    isEditMode: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Button(
            onClick = onSubmitButtonClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = isSignUpValid,
        ) {
            Text(
                text = when (isEditMode) {
                    true -> stringResource(R.string.btn_sign_up_edit_user)
                    false -> stringResource(R.string.btn_sign_up)
                },
            )
        }

        // todo: 회원 탈퇴
//        OutlinedButton(
//            onClick = {},
//            modifier = Modifier.fillMaxWidth(),
//        ) {
//            Text(
//                text = stringResource(R.string.btn_sign_up_cancel),
//            )
//        }
    }
}

@Preview(locale = "ko")
@PreviewLightDark
@Composable
fun PreviewSignUpScreen() {
    WeQuizTheme {
        SignUpScreen(
            isLoading = false,
            userSubmitInfo = UserSubmitInfo(
                email = "test@gmail.com",
                name = "위퀴즈",
                profileImageUrl = null,
                studyGroups = emptyList(),
            ),
            isSignUpButtonEnabled = true,
            isEditMode = false,
            profileImageByteArray = null,
            onNameChanged = {},
            onProfileByteArrayChanged = {},
            onNavigationButtonClick = {},
            onSignUpButtonClick = {},
            onEditButtonClick = {},
        )
    }
}
