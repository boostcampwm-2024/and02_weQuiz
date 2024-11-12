package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLeftChatBubble
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLocalRoundedImage
import kr.boostcamp_2024.course.study.R

@Composable
fun StudyCreationGuide(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(16.dp),
    ) {
        WeQuizLocalRoundedImage(
            modifier = Modifier.size(120.dp),
            imagePainter = painterResource(id = R.drawable.create_study_character),
            contentDescription = stringResource(R.string.des_create_study_character),
        )

        Column(
            modifier = Modifier.padding(start = 10.dp)
        ) {
            WeQuizLeftChatBubble(text = stringResource(R.string.txt_create_study_chat_bubble_first))
            Spacer(modifier = Modifier.height(6.dp))
            WeQuizLeftChatBubble(text = stringResource(R.string.txt_create_study_chat_bubble_second))
        }
    }
}