package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3Api
@Composable
fun QuestionDetailTopAppBar(
    modifier: Modifier = Modifier, onNavigationButtonClick: () -> Unit
) {
    CenterAlignedTopAppBar(modifier = modifier.padding(bottom = 20.dp), title = {
        Text(
            text = "문제 상세 설명", modifier = Modifier, style = MaterialTheme.typography.titleLarge
        )
    }, navigationIcon = {
        IconButton(onClick = onNavigationButtonClick, modifier = modifier) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = modifier,
            )
        }
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface
    )
    )
}