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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.main.R

@Composable
fun NotificationItem(
    notification: Notification,
    onRejectClick: () -> Unit,
    onAcceptClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp),
        ) {
            Text(
                text = notification.groupId.toString(),
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = stringResource(R.string.txt_notification_item_invite_message),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.align(Alignment.End),
            ) {
                Button(
                    onClick = onRejectClick,
                    modifier = Modifier.size(width = 53.dp, height = 24.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 2.dp,
                        bottom = 4.dp,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.txt_notification_item_reject),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }

                Spacer(Modifier.width(10.dp))

                Button(
                    onClick = onAcceptClick,
                    modifier = Modifier.size(width = 53.dp, height = 24.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 2.dp,
                        bottom = 4.dp,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.txt_notification_item_message),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }

            }

        }
    }
    Spacer(modifier = modifier.width(12.dp))

    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
}

// 임시 활용 data class
data class Notification(
    val notificationId: Int = 0,
    val groupId: Int,
)
