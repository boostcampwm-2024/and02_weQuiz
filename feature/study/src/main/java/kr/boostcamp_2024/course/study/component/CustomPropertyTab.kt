package kr.boostcamp_2024.course.study.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.study.R

@Composable
fun CustomPropertyTab(
    onClicked: () -> Unit,
    imageVector: ImageVector,
    description: String? = null,
    @StringRes title: Int
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(title), style = MaterialTheme.typography.titleLarge)
        CustomIconButton(onClicked = onClicked, imageVector = imageVector, description = description)
        Spacer(modifier = Modifier.weight(1f))
        AssistChip(onClick = {}, label = {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "10명 / 50명",
                style = MaterialTheme.typography.labelLarge
            )
        }, leadingIcon = {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = stringResource(R.string.assist_chip_top_bar_detail_study)
            )
        }
        )
    }
}
