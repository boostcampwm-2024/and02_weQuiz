package kr.boostcamp_2024.course.login.presentation

import android.util.Log
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.designsystem.ui.annotation.PreviewKoLightDark
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLeftChatBubble
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizRightChatBubble
import kr.boostcamp_2024.course.login.R
import kr.boostcamp_2024.course.login.model.UserUiModel
import kr.boostcamp_2024.course.login.viewmodel.LoginViewModel
import java.security.MessageDigest
import java.util.UUID

@Composable
internal fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onSignUp: (UserUiModel) -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowErrorSnackbar: (Throwable) -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val loginUiState by viewModel.loginUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collectLatest { throwable -> onShowErrorSnackbar(throwable) }
    }

    LaunchedEffect(loginUiState.isLoginSuccess) {
        if (loginUiState.isLoginSuccess) {
            onLoginSuccess()
        }
    }

    LaunchedEffect(loginUiState.isNewUser) {
        if (loginUiState.isNewUser) {
            val userInfo = requireNotNull(loginUiState.userInfo)
            viewModel.resetUserState()
            onSignUp(userInfo)
        }
    }

    LoginScreen(
        onLoginForExperienceButtonClick = viewModel::loginForExperience,
        handleSignIn = viewModel::handleSignIn,
        snackbarHostState = snackbarHostState,
        onShowErrorSnackbar = onShowErrorSnackbar,
    )
}

@Composable
private fun LoginScreen(
    onLoginForExperienceButtonClick: () -> Unit,
    handleSignIn: (GetCredentialResponse) -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LoginGuideImageAndText()
            LoginButtons(
                onLoginForExperienceButtonClick = onLoginForExperienceButtonClick,
                handleSignIn = handleSignIn,
                onShowErrorSnackbar = onShowErrorSnackbar,
            )
        }
    }
}

@Composable
private fun LoginGuideImageAndText() {
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
private fun LoginButtons(
    onLoginForExperienceButtonClick: () -> Unit,
    handleSignIn: (GetCredentialResponse) -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
) {
    val webClientId = stringResource(R.string.web_client_id)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val onClick: () -> Unit = {
        val credentialManager = CredentialManager.create(context)

        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOptions: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setNonce(hashedNonce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOptions)
            .build()

        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                handleSignIn(result)
            } catch (e: Exception) {
                Log.e("LoginScreen", "Error: ${e.message}")
                onShowErrorSnackbar(e)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.clickable(
                enabled = true,
                onClick = onClick,
            ),
            painter = painterResource(R.drawable.img_google_light_btn_login),
            contentDescription = stringResource(R.string.des_google_login),
        )
        Text(
            text = stringResource(R.string.txt_experience),
            modifier = Modifier.clickable(
                enabled = true,
                onClick = onLoginForExperienceButtonClick,
            ),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
        )
    }
}

@PreviewKoLightDark
@Composable
private fun LoginScreenPreview() {
    WeQuizTheme {
        LoginScreen(
            onLoginForExperienceButtonClick = {},
            handleSignIn = { _ -> },
            onShowErrorSnackbar = {},
        )
    }
}
