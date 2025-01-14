package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLocalRoundedImage
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizRightChatBubble
import kr.boostcamp_2024.course.quiz.R

@Composable
fun RealTimeQuizGuideContent(
    ownerName: String,
    totalParticipants: Int,
    submittedParticipants: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.End,
            modifier = Modifier,
        ) {
            WeQuizRightChatBubble(
                modifier = Modifier,
                text = stringResource(R.string.txt_quiz_owner, ownerName),
            )
            WeQuizRightChatBubble(
                modifier = Modifier,
                text = stringResource(R.string.txt_quiz_submit_state, submittedParticipants, totalParticipants),
            )
        }
        WeQuizLocalRoundedImage(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterVertically),
            imagePainter = painterResource(id = R.drawable.quiz_system_profile),
            contentDescription = stringResource(R.string.des_image_question),
        )
    }
}

@Preview(showBackground = true, locale = "ko")
@PreviewLightDark
@Composable
private fun RealTimeQuizGuideContentPreview() {
    RealTimeQuizGuideContent(
        ownerName = "이훈",
        totalParticipants = 10,
        submittedParticipants = 5,
    )
}
