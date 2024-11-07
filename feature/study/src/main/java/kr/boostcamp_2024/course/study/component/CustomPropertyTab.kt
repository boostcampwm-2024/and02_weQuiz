package kr.boostcamp_2024.course.study.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource

@Composable
fun CustomPropertyTab(
    onClicked: () -> Unit,
    icon: Painter,
    description: String? = null,
    @StringRes title: Int
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(title), style = MaterialTheme.typography.titleLarge)
        CustomIconButton(onClicked = onClicked, icon = icon, description = description)
    }
}
