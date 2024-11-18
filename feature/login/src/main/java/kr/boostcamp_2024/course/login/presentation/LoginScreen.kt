package kr.boostcamp_2024.course.login.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLeftChatBubble
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizRightChatBubble
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizTextField
import kr.boostcamp_2024.course.login.R
import kr.boostcamp_2024.course.login.presentation.component.PasswordTextField
import kr.boostcamp_2024.course.login.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    val loginUiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(loginUiState) {
        if (loginUiState.isLoginSuccess) {
            onLoginSuccess()
        }
        loginUiState.snackBarMessage?.let { message ->
            snackBarHostState.showSnackbar(message)
            loginViewModel.setNewSnackBarMessage(null)
        }
    }

    LoginScreen(
        snackBarHostState,
        loginViewModel::loginForExperience,
        loginViewModel::setNewSnackBarMessage,
    )
}

@Composable
private fun LoginScreen(
    snackBarHostState: SnackbarHostState,
    onLoginSuccess: () -> Unit,
    setNewSnackBarMessage: (String) -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = innerPadding.calculateBottomPadding(),
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LoginGuideImageAndText()
            LoginContent()
            LoginButtons(
                onLoginSuccess = onLoginSuccess,
                showSnackBar = setNewSnackBarMessage,
            )
        }
    }
}

@Composable
fun LoginGuideImageAndText() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.img_app_logo),
            modifier = Modifier.width(300.dp),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
        ) {
            WeQuizLeftChatBubble(
                modifier = Modifier.wrapContentSize(),
                text = stringResource(R.string.txt_introduce_app1),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            WeQuizRightChatBubble(
                modifier = Modifier.wrapContentSize(),
                text = stringResource(R.string.txt_introduce_app2),
            )
        }
    }
}

@Composable
fun LoginContent() {
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        WeQuizTextField(
            label = stringResource(R.string.txt_login_email_label),
            text = email,
            onTextChanged = { email = it },
            placeholder = stringResource(R.string.txt_login_email_placeholder),
        )

        PasswordTextField(
            password = password,
            onPasswordChanged = { password = it },
        )
    }
}

@Composable
fun LoginButtons(
    onLoginSuccess: () -> Unit,
    showSnackBar: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {
                // todo: 로그인 처리
                showSnackBar("추후 제공될 기능입니다.")
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.btn_sign_in))
        }
        OutlinedButton(
            onClick = {
                // todo: 회원가입 처리
                showSnackBar("추후 제공될 기능입니다.")
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.btn_sign_up))
        }
        Text(
            text = stringResource(R.string.txt_experience),
            modifier = Modifier.clickable(
                enabled = true,
                onClick = onLoginSuccess,
            ),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    WeQuizTheme {
        LoginScreen(
            snackBarHostState = SnackbarHostState(),
            onLoginSuccess = {},
            setNewSnackBarMessage = { },
        )
    }
}
