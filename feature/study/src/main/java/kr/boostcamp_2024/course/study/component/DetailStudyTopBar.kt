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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
            contentDescription = "배경 이미지",
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
                icon = painterResource(R.drawable.baseline_arrow_back_24),
                description = "뒤로 가기 아이콘 버튼"
            )

            CustomIconButton(
                onClicked = { Log.d("detail", "설정 클릭됨") },
                icon = painterResource(R.drawable.baseline_settings_24),
                description = "설정 아이콘 버튼"
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
            Button(onClick = {}, shape = RoundedCornerShape(8.dp)) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(R.drawable.baseline_account_circle_24),
                    contentDescription = "그룹원 수 버튼"
                )
                Text(modifier = Modifier.padding(start = 8.dp), text = "10명 / 50명", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
