package kr.boostcamp_2024.course.login.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.login.R

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChanged: (String) -> Unit,
) {
    var showPassword by remember { mutableStateOf(false) }
    TextField(
        value = password,
        onValueChange = onPasswordChanged,
        label = { Text(stringResource(R.string.txt_login_password_label)) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        maxLines = 1,
        minLines = 1,
        placeholder = { Text(stringResource(R.string.txt_login_password_placeholder)) },
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(
                onClick = { showPassword = !showPassword },
            ) {
                Icon(
                    painter = if (showPassword) {
                        painterResource(R.drawable.baseline_visibility_24)
                    } else {
                        painterResource(R.drawable.baseline_visibility_off_24)
                    },
                    contentDescription = if (showPassword) {
                        stringResource(R.string.des_ic_visible_password)
                    } else {
                        stringResource(R.string.des_ic_invisible_password)
                    },
                )
            }
        },
    )
}

@Preview
@Composable
fun PasswordTextFieldPreview() {
    WeQuizTheme {
        PasswordTextField(
            password = "",
            onPasswordChanged = {},
        )
    }
}
