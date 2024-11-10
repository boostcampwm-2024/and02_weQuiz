package kr.boostcamp_2024.course.main.component

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
import kr.boostcamp_2024.course.main.R

@ExperimentalMaterial3Api
@Composable
fun NotificationTopAppBar(
    modifier: Modifier = Modifier,
    onNavigationButtonClick: () -> Unit
) {
    CenterAlignedTopAppBar(modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.txt_notification_top_app_bar_title),
                modifier = Modifier
            )
        }, navigationIcon = {
            IconButton(
                onClick = onNavigationButtonClick,
                modifier = modifier
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.txt_notification_top_app_bar_navigation_icon),
                    modifier = modifier,
                )
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}