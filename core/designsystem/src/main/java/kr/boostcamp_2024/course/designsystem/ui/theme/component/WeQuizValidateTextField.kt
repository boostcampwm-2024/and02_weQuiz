package kr.boostcamp_2024.course.designsystem.ui.theme.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.R

@Composable
fun WeQuizValidateTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    onTextChanged: (String) -> Unit,
    placeholder: String,
    minLines: Int = 1,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    errorMessage: String,
    validFun: (String) -> Boolean,
) {
    Column {
        TextField(
            value = text,
            onValueChange = { onTextChanged(it) },
            modifier = modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge,
            label = { Text(label) },
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
            isError = !validFun(text),
        )
        if (!validFun(text)) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

