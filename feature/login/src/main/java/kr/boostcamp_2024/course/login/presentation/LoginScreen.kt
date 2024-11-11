package kr.boostcamp_2024.course.login.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val loginUiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()
    LaunchedEffect(loginUiState) {
        if (loginUiState is LoginUiState.Success) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginGuideImageAndText()
        LoginContent()
        LoginButtons(onLoginSuccess = loginViewModel::loginForExperience)
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
            contentScale = ContentScale.FillWidth
        )
        WeQuizLeftChatBubble(
            text = stringResource(R.string.txt_introduce_app1),
        )
        WeQuizRightChatBubble(
            text = stringResource(R.string.txt_introduce_app2),
        )
    }
}

@Composable
fun LoginContent() {
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        WeQuizTextField(
            label = stringResource(R.string.txt_login_email_label),
            text = "",
            onTextChanged = {},
            placeholder = stringResource(R.string.txt_login_email_placeholder)
        )

        PasswordTextField(
            password = password,
            onPasswordChanged = { password = it }
        )
    }
}

@Composable
fun LoginButtons(
    onLoginSuccess: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { /* todo: 로그인 처리 */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.btn_sign_in))
        }
        OutlinedButton(
            onClick = { /* todo: 회원가입 처리 */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.btn_sign_up))
        }
        Text(
            text = stringResource(R.string.txt_experience),
            modifier = Modifier.clickable(
                enabled = true,
                onClick = onLoginSuccess
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
            onLoginSuccess = {},
        )
    }
}