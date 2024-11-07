package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.study.R

@Composable
fun GroupItem(profileImg: String?, name: String, removeButtonClick: () -> Unit) {
    Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically){
        CustomRoundImg(imgUrl = profileImg, modifier = Modifier
            .clip(CircleShape)
            .size(54.dp))
        Text(modifier = Modifier
            .padding(start = 16.dp)
            .weight(1f), text = name, style = MaterialTheme.typography.bodyMedium)
        Button(onClick = removeButtonClick) {
            Icon(painter = painterResource(R.drawable.baseline_remove_24), contentDescription = "remove button")
            Text(modifier = Modifier.padding(start = 8.dp), text = stringResource(R.string.btn_remove_group), style = MaterialTheme.typography.labelMedium)
        }
    }
}