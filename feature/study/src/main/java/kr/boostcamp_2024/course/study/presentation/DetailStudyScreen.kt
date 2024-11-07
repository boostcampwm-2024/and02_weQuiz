package kr.boostcamp_2024.course.study.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kr.boostcamp_2024.course.study.component.DetailStudyTopBar
import kr.boostcamp_2024.course.study.navigation.DetailScreenRoute
import kr.boostcamp_2024.course.study.navigation.GroupScreenRoute

@Composable
fun DetailStudyScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateCategoryButtonClick: () -> Unit,
    onCategoryClick: () -> Unit
) {
    val bottomNavController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { DetailStudyTopBar() },
        bottomBar = {
            val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val navList = listOf(
                DetailScreenRoute, GroupScreenRoute
            )
            NavigationBar {
                navList.forEach { nav ->
                    val selected = currentDestination?.hasRoute(nav::class) ?: false
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            bottomNavController.navigate(nav) {
                                // 내비게이션 시작 지점까지 있는 모든 스택을 팝하는 부분
                                // 탭한 스크린만 남겨놓기 위한 설계
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    // 화면 상태 저장하는 부분
                                    saveState = true
                                }
                                // 같은 탭을 또 클릭했을 때 중복으로 스택에 쌓이지 않도록 하는 설정
                                launchSingleTop = true
                                // 같은 탭을 또 클릭했을 때 상태 유지를 위한 설정
                                restoreState = true
                            }
                        },
                        label = { Text(text = nav.title) },
                        icon = {
                            Icon(
                                painter = painterResource(id = nav.iconId),
                                tint = if (selected) Color.Yellow else Color.White,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = bottomNavController,
            startDestination = DetailScreenRoute
        ) {
            composable<DetailScreenRoute> {
                CategoryListScreen()
            }
            composable<GroupScreenRoute> {
                GroupListScreen()
            }
        }
    }
}
