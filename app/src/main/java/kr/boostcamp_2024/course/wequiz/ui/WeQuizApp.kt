package kr.boostcamp_2024.course.wequiz.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.domain.NetworkMonitor
import kr.boostcamp_2024.course.wequiz.R

@Composable
fun WeQuizApp(networkMonitor: NetworkMonitor) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val localContextResource = LocalContext.current.resources
    val networkState by networkMonitor.networkState.collectAsStateWithLifecycle(true)
    val onShowErrorSnackbar: (throwable: Throwable) -> Unit = { throwable ->
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = throwable.message ?: localContextResource.getString(R.string.default_error_message),
            )
        }
    }

    WeQuizTheme {
        WeQuizNavHost(
            snackbarHostState = snackbarHostState,
            modifier = Modifier
                .fillMaxSize(),
            onShowErrorSnackbar = onShowErrorSnackbar,
        )
        NetworkDialog(networkState = networkState)
    }
}
