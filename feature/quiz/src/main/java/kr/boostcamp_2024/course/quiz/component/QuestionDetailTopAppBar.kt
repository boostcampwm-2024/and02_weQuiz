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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.quiz.R

@ExperimentalMaterial3Api
@Composable
fun QuestionDetailTopAppBar(
    modifier: Modifier = Modifier,
    onNavigationButtonClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = modifier.padding(bottom = 20.dp),
        title = {
            Text(
                text = stringResource(R.string.txt_question_detail_top_app_bar_title),
                modifier = Modifier,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigationButtonClick,
                modifier = Modifier,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.des_question_detail_top_app_bar_navigation_icon),
                    modifier = Modifier,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    )
}
