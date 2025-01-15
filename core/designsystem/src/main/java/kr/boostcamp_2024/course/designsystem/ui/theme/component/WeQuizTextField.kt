package kr.boostcamp_2024.course.designsystem.ui.theme.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kr.boostcamp_2024.course.designsystem.R
import kr.boostcamp_2024.course.designsystem.ui.annotation.PreviewKoLightDark
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme

@Composable
fun WeQuizTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    onTextChanged: (String) -> Unit,
    placeholder: String,
    minLines: Int = 1,
    maxLines: Int = 1,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    TextField(
        value = text,
        onValueChange = { onTextChanged(it) },
        modifier = modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        minLines = minLines,
        maxLines = maxLines,
        isError = isError,
        trailingIcon = {
            IconButton(
                onClick = { onTextChanged("") },
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_cancel_24),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = stringResource(id = R.string.des_clear_text),
                )
            }
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
fun WeQuizOutLinedTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    placeholder: String,
    minLines: Int = 1,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        value = text,
        onValueChange = { onTextChanged(it) },
        modifier = modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholder = { Text(placeholder) },
        minLines = minLines,
        maxLines = maxLines,
        trailingIcon = {
            IconButton(
                onClick = { onTextChanged("") },
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_cancel_24),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = stringResource(id = R.string.des_clear_text),
                )
            }
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@PreviewKoLightDark
@Composable
fun WeQuizTextFieldPreview() {
    WeQuizTheme {
        WeQuizTextField(
            label = "Label",
            text = "Text",
            onTextChanged = {},
            placeholder = "Placeholder",
        )
    }
}

@PreviewKoLightDark
@Composable
fun WeQuizOutLinedTextFieldPreview() {
    WeQuizTheme {
        WeQuizOutLinedTextField(
            text = "Text",
            onTextChanged = {},
            placeholder = "Placeholder",
        )
    }
}
