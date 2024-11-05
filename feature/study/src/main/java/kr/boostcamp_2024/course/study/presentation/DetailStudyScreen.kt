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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
                            bottomNavController.navigate(nav)
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
                CategoryScreen()
            }
            composable<GroupScreenRoute> {
                GroupScreen()
            }
        }
    }
}
