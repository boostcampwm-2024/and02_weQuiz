package kr.boostcamp_2024.course.study.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.study.R

@ExperimentalMaterial3Api
@Composable
internal fun CreateStudyTopAppBar(
    isEditMode: Boolean,
    onNavigationButtonClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = when (isEditMode) {
                    true -> stringResource(R.string.txt_edit_study_top_app_bar)
                    false -> stringResource(R.string.txt_create_study_top_app_bar)
                },
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationButtonClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.des_navigation_back),
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, locale = "ko")
@Composable
private fun CreateStudyTopAppBarPreview() {
    WeQuizTheme {
        CreateStudyTopAppBar(
            isEditMode = false,
            onNavigationButtonClick = {},
        )
    }
}
