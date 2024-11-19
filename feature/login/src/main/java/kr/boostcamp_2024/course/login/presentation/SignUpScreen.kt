package kr.boostcamp_2024.course.login.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizTextField
import kr.boostcamp_2024.course.login.R
import kr.boostcamp_2024.course.login.viewmodel.SignUpUiState
import kr.boostcamp_2024.course.login.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigationButtonClick: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.signUpUiState.collectAsStateWithLifecycle()
    SignupScreen(
        uiState = uiState,
        onEmailChanged = viewModel::onEmailChanged,
        onNickNameChanged = viewModel::onNickNameChanged,
        onProfileUriChanged = viewModel::onProfileUriChanged,
        onNavigationButtonClick = onNavigationButtonClick,
        onEditButtonClick = viewModel::updateUser,
        onSignUpButtonClick = viewModel::addUser,
    )

    LaunchedEffect(uiState.isSubmitSuccess) {
        if (uiState.isSubmitSuccess) {
            onSignUpSuccess()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignupScreen(
    uiState: SignUpUiState,
    onEmailChanged: (String) -> Unit,
    onNickNameChanged: (String) -> Unit,
    onProfileUriChanged: (String) -> Unit,
    onNavigationButtonClick: () -> Unit,
    onEditButtonClick: () -> Unit,
    onSignUpButtonClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (uiState.isEditMode) {
                            true -> "회원 수정"
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
                    email = uiState.userCreationInfo.email,
                    nickName = uiState.userCreationInfo.nickName,
                    profileUri = uiState.userCreationInfo.profileImageUrl,
                    onEmailChanged = onEmailChanged,
                    onNickNameChanged = onNickNameChanged,
                    onProfileUriChanged = onProfileUriChanged,
                    isEmailValid = uiState.isEmailValid,
                )
            }
            item {
                SignUpButtons(
                    isSignUpValid = uiState.isSignUpButtonEnabled,
                    onSignUpSuccess = if (uiState.isEditMode) onEditButtonClick else onSignUpButtonClick,
                    isEditMode = uiState.isEditMode,
                )
            }
        }
    }
}

@Composable
fun SignUpContent(
    email: String,
    nickName: String,
    profileUri: String?,
    onEmailChanged: (String) -> Unit,
    onNickNameChanged: (String) -> Unit,
    onProfileUriChanged: (String) -> Unit,
    isEmailValid: Boolean,
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        try {
            // todo: 갤러리 이미지 처리
        } catch (e: Exception) {
            // todo: 갤러리 예외 처리
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(148.dp)
                .clip(shape = MaterialTheme.shapes.large)
                .clickable(enabled = true) {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                    )
                },
            painter = painterResource(kr.boostcamp_2024.course.designsystem.R.drawable.img_photo_picker),
            contentDescription = stringResource(R.string.des_img_photo_picker),
            contentScale = ContentScale.FillWidth,
        )

        WeQuizTextField(
            label = stringResource(R.string.txt_login_email_label),
            text = email,
            isError = isEmailValid.not(),
            onTextChanged = onEmailChanged,
            placeholder = stringResource(R.string.txt_login_email_placeholder),
        )

        WeQuizTextField(
            label = stringResource(R.string.txt_sign_up_nick_name),
            text = nickName,
            onTextChanged = onNickNameChanged,
            placeholder = stringResource(R.string.txt_sign_up_nick_name_placeholder),
        )
    }
}

@Composable
fun SignUpButtons(
    isSignUpValid: Boolean,
    onSignUpSuccess: () -> Unit,
    isEditMode: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Button(
            onClick = onSignUpSuccess,
            modifier = Modifier.fillMaxWidth(),
            enabled = isSignUpValid,
        ) {
            Text(
                text = when (isEditMode) {
                    true -> "회원 정보 수정"
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

@Preview
@Composable
fun PreviewSignUpScreen() {
    WeQuizTheme {
        SignUpScreen(
            onSignUpSuccess = {},
            onNavigationButtonClick = {},
        )
    }
}
