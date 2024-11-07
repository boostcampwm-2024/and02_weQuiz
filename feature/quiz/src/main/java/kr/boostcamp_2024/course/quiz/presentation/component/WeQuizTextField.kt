package kr.boostcamp_2024.course.quiz.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
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
import kr.boostcamp_2024.course.quiz.R

@Composable
fun WeQuizTextField(
    label: String,
    placeholder: String,
    minLine: Int = 1
) {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        minLines = minLine,
        trailingIcon = {
            IconButton(
                onClick = { text = "" }
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_cancel_on_surface_variant),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = stringResource(id = R.string.des_clear_text)
                )
            }
        }
    )
}