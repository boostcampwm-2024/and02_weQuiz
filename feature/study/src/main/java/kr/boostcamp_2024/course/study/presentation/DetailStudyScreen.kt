package kr.boostcamp_2024.course.study.presentation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizImageLargeTopAppBar
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.component.CustomIconButton
import kr.boostcamp_2024.course.study.navigation.DetailScreenRoute
import kr.boostcamp_2024.course.study.navigation.GroupScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailStudyScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateCategoryButtonClick: () -> Unit,
    onCategoryClick: () -> Unit,
) {
    var selectedScreenIndex by remember { mutableIntStateOf(0) }
    val screenList = listOf(
        DetailScreenRoute, GroupScreenRoute
    )
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            WeQuizImageLargeTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = "OS 스터디", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.SemiBold
                    )
                },
                topAppBarImageUrl = "https://avatars.githubusercontent.com/u/147039081?v=4",
                navigationIcon = {
                    CustomIconButton(
                        onClicked = onNavigationButtonClick,
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        description = stringResource(R.string.btn_detail_study_top_bar_back)
                    )
                },
                actions = {
                    CustomIconButton(
                        onClicked = { Log.d("detail", "설정 클릭됨") },
                        imageVector = Icons.Filled.Settings,
                        description = stringResource(R.string.btn_top_bar_detail_study_setting)
                    )
                }
            )
        }, bottomBar = {
            NavigationBar {
                screenList.forEachIndexed { index, screen ->
                    val selected = selectedScreenIndex == index
                    NavigationBarItem(selected = selected, onClick = {
                        selectedScreenIndex = index
                    }, label = { Text(text = stringResource(screen.title)) }, icon = {
                        Icon(
                            painter = painterResource(id = screen.iconId),
                            contentDescription = stringResource(R.string.des_icon_bottom_nav_detail_study)
                        )
                    })
                }
            }
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedScreenIndex) {
                0 -> CategoryListScreen(onCreateCategoryButtonClick, onCategoryClick)
                1 -> GroupListScreen()
            }
        }
    }
}
