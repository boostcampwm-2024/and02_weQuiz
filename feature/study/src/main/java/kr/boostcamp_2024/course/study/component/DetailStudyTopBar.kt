package kr.boostcamp_2024.course.study.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.study.R

@Preview
@Composable
fun DetailStudyTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.waterfall),
            contentDescription = stringResource(R.string.top_bar_detail_study_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomIconButton(
                onClicked = { /* 뒤로 가기 동작 */ },
                imageVector =  Icons.AutoMirrored.Filled.ArrowBack,
                description = stringResource(R.string.btn_detail_study_top_bar_back)
            )

            CustomIconButton(
                onClicked = { Log.d("detail", "설정 클릭됨") },
                imageVector = Icons.Filled.Settings,
                description = stringResource(R.string.btn_top_bar_detail_study_setting)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomStart)
        ) {
            Text(text = "OS 스터디", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.SemiBold)
            Text(text = "os 와압!!!", style = MaterialTheme.typography.bodySmall)
            AssistChip(
                onClick = {},
                label = {Text(modifier = Modifier.padding(start = 8.dp), text = "10명 / 50명", style = MaterialTheme.typography.labelLarge)},
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = stringResource(R.string.assist_chip_top_bar_detail_study)
                    )
                }
            )
        }
    }
}
