package kr.boostcamp_2024.course.designsystem.ui.theme.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.R
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme

@Composable
fun WeQuizBaseDialog(
    title: String,
    dialogImage: Painter,
    confirmTitle: String,
    dismissTitle: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
    confirmButtonEnabled: Boolean = true,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                WeQuizLocalRoundedImage(
                    modifier = Modifier.size(120.dp),
                    imagePainter = dialogImage,
                    contentDescription = null,
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
                content()
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(dismissTitle)
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = confirmButtonEnabled,
            ) {
                Text(confirmTitle)
            }
        },
    )
}

@Preview
@Composable
private fun WeQuizBaseDialogPreview() {
    WeQuizTheme {
        WeQuizBaseDialog(
            title = "Are you sure you want to delete this question?",
            dialogImage = painterResource(id = R.drawable.img_error),
            confirmTitle = "Delete",
            dismissTitle = "Cancel",
            onConfirm = {},
            onDismissRequest = {},
            confirmButtonEnabled = false,
            content = {
                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Password") },
                    placeholder = { Text("Enter your password") },
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        )
    }
}
