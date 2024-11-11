package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.quiz.R

@Composable
fun QuestionSolution(solution: String) {
	Column {
		Text(
			text = stringResource(R.string.txt_question_detail_solution),
			modifier = Modifier,
			style = MaterialTheme.typography.bodyMedium,
		)
		Spacer(modifier = Modifier.height(10.dp))
		Row(modifier = Modifier) {
			ChatBubble(
				solution,
				modifier = Modifier.weight(1f),
			)

			Box(
				modifier = Modifier
					.padding(start = 10.dp)
					.align(Alignment.CenterVertically)
					.size(120.dp)
					.clip(CircleShape),
			) {
				Image(
					painter = painterResource(id = R.drawable.create_study_character),
					contentDescription = stringResource(R.string.des_question_detail_solution_profile),
					modifier = Modifier,
					contentScale = ContentScale.Crop,
				)
			}
		}
	}
}
