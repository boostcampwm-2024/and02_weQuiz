package kr.boostcamp_2024.course.study.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.component.DetailStudyTopBar
import kr.boostcamp_2024.course.study.navigation.DetailScreenRoute
import kr.boostcamp_2024.course.study.navigation.GroupScreenRoute

@Composable
fun DetailStudyScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateCategoryButtonClick: () -> Unit,
    onCreateGroupButtonClick: () -> Unit,
    onCategoryClick: () -> Unit,
) {
    var selectedScreenIndex by remember { mutableIntStateOf(0) }
    val screenList = listOf(
        DetailScreenRoute, GroupScreenRoute
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { DetailStudyTopBar(onNavigationButtonClick) },
        bottomBar = {
            NavigationBar {
                screenList.forEachIndexed { index, screen ->
                    val selected = selectedScreenIndex == index
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            selectedScreenIndex = index
                        },
                        label = { Text(text = stringResource(screen.title)) },
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.iconId),
                                contentDescription = stringResource(R.string.icon_bottm_nav_detail_study)
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedScreenIndex) {
                0 -> CategoryListScreen(onCreateCategoryButtonClick, onCategoryClick)
                1 -> GroupListScreen(onCreateGroupButtonClick)
            }
        }
    }
}
