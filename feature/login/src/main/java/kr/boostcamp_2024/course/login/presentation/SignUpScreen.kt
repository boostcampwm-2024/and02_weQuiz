package kr.boostcamp_2024.course.login.presentation

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizAsyncImage
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizCircularProgressIndicator
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

    SignupScreen(
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onNameChanged = viewModel::onNameChanged,
        onProfileUriChanged = viewModel::onProfileUriChanged,
        onNavigationButtonClick = onNavigationButtonClick,
        onSignUpButtonClick = viewModel::signUp,
        setNewSnackBarMessage = viewModel::setNewSnackBarMessage,
        onEditButtonClick = viewModel::updateUser,
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
    snackBarHostState: SnackbarHostState,
    onNameChanged: (String) -> Unit,
    onProfileUriChanged: (String) -> Unit,
    onNavigationButtonClick: () -> Unit,
    onSignUpButtonClick: () -> Unit,
    setNewSnackBarMessage: (Int) -> Unit,
    onEditButtonClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (uiState.isEditMode) {
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
                    email = uiState.userSubmitInfo.email,
                    name = uiState.userSubmitInfo.name,
                    profileUri = uiState.userSubmitInfo.profileImageUrl,
                    onNameChanged = onNameChanged,
                    onProfileUriChanged = onProfileUriChanged,
                    setNewSnackBarMessage = setNewSnackBarMessage,
                )
            }
            item {
                SignUpButtons(
                    isSignUpValid = uiState.isSignUpButtonEnabled,
                    onSubmitButtonClick = if (uiState.isEditMode) onEditButtonClick else onSignUpButtonClick,
                    isEditMode = uiState.isEditMode,
                )
            }
        }
        if (uiState.isLoading) {
            WeQuizCircularProgressIndicator()
        }
    }
}

@Composable
fun SignUpContent(
    email: String,
    name: String,
    profileUri: String?,
    onNameChanged: (String) -> Unit,
    onProfileUriChanged: (String) -> Unit,
    setNewSnackBarMessage: (Int) -> Unit,
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        try {
            uri?.let {
                onProfileUriChanged(it.toString())
            }
        } catch (e: Exception) {
            Log.e("SignUpScreen", "Failed to load image from gallery", e)
            setNewSnackBarMessage(R.string.error_photo_picker)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        WeQuizAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(18.dp))
                .clickable(enabled = true) {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                    )
                },
            imgUrl = profileUri,
            placeholder = painterResource(kr.boostcamp_2024.course.designsystem.R.drawable.img_photo_picker),
            contentDescription = stringResource(R.string.des_img_photo_picker),
            fallback = painterResource(kr.boostcamp_2024.course.designsystem.R.drawable.img_photo_picker),
        )

        TextField(
            value = email,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
        )

        WeQuizTextField(
            label = stringResource(R.string.txt_sign_up_nick_name),
            text = name,
            onTextChanged = onNameChanged,
            placeholder = stringResource(R.string.txt_sign_up_nick_name_placeholder),
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
