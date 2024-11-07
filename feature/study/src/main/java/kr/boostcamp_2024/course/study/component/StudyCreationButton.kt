package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StudyCreationButton(onCreateStudySuccess: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onCreateStudySuccess,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF136B55)
        )
    ) {
        Text(
            text = "스터디 생성", modifier = modifier, style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ), color = Color.White
        )
    }
}
