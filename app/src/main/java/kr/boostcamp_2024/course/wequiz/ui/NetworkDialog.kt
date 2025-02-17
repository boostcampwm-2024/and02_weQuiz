package kr.boostcamp_2024.course.wequiz.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.designsystem.ui.annotation.PreviewKoLightDark
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizBaseDialog
import kr.boostcamp_2024.course.domain.NetworkState
import kr.boostcamp_2024.course.quiz.R

@Composable
fun NetworkDialog(
    networkState: NetworkState,
) {
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(networkState) {
        showDialog = networkState is NetworkState.NotConnected
    }
    if (showDialog) {
        WeQuizBaseDialog(
            title = "이용중인 네트워크 환경이 불안정합니다. 무선통신(5G/LTE 또는 Wi-Fi)상태를 확인 후 다시 이용 부탁드립니다.",
            dialogImage = painterResource(id = R.drawable.quiz_create_ai_profile),
            confirmTitle = "재시도",
            dismissTitle = null,
            onConfirm = {
                coroutineScope.launch {
                    showDialog = false
                    delay(500L)
                    showDialog = (networkState == NetworkState.NotConnected)
                }
            },
            onDismissRequest = {
            },
            content = { },
        )
    }
}

@PreviewKoLightDark
@Composable
private fun NetworkDialogPreview() {
    WeQuizTheme {
        WeQuizBaseDialog(
            title = "이용중인 네트워크 환경이 불안정합니다. 무선통신(5G/LTE 또는 Wi-Fi)상태를 확인 후 다시 이용 부탁드립니다.",
            dialogImage = painterResource(id = R.drawable.quiz_create_ai_profile),
            confirmTitle = "재시도",
            onConfirm = {},
            onDismissRequest = { },
            content = { },
        )
    }
}
