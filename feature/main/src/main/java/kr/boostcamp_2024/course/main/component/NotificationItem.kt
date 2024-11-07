package kr.boostcamp_2024.course.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun NotificationItem(
    notification: Notification,
    onRejectClick: () -> Unit,
    onAcceptClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        ) // 이미지 넣어야 하는 곳

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
        ) {
            Text(
                text = notification.groupId.toString(), // 그룹이름 받아오는것으로 수정
                fontSize = 14.sp
            )
            Text(
                text = "그룹 초대가 왔어요! 그룹원이 회원님을 기다립니다.",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                fontSize = 16.sp,
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                Button(
                    onClick = onRejectClick,
                    modifier = Modifier.size(width = 53.dp, height = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB2CCC1)
                    ),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 2.dp,
                        bottom = 4.dp
                    )
                ) {
                    Text(text = "거절", fontSize = 11.sp)
                }

                Spacer(Modifier.width(10.dp))

                Button(
                    onClick = onAcceptClick,
                    modifier = Modifier.size(width = 53.dp, height = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF006A5D)
                    ),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 2.dp,
                        bottom = 4.dp
                    )
                ) {
                    Text(text = "수락", fontSize = 11.sp)
                }

            }

        }
    }
    Spacer(modifier = modifier.width(12.dp))

    Spacer(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .fillMaxWidth()
            .background(Color(0xFFBFC9C3))
    )
}

/* 임시 활용 data class */
data class Notification(
    val notificationId: Int = 0,
    val groupId: Int
)
