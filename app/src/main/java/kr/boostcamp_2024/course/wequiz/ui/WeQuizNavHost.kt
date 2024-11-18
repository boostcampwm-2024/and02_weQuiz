package kr.boostcamp_2024.course.wequiz.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kr.boostcamp_2024.course.category.navigation.categoryNavGraph
import kr.boostcamp_2024.course.category.navigation.navigateCategory
import kr.boostcamp_2024.course.category.navigation.navigateCreateCategory
import kr.boostcamp_2024.course.login.navigation.LoginRoute
import kr.boostcamp_2024.course.login.navigation.loginNavGraph
import kr.boostcamp_2024.course.main.navigation.mainNavGraph
import kr.boostcamp_2024.course.main.navigation.navigateMain
import kr.boostcamp_2024.course.main.navigation.navigateNotification
import kr.boostcamp_2024.course.quiz.navigation.navigateCreateQuestion
import kr.boostcamp_2024.course.quiz.navigation.navigateCreateQuiz
import kr.boostcamp_2024.course.quiz.navigation.navigateQuestion
import kr.boostcamp_2024.course.quiz.navigation.navigateQuestionDetail
import kr.boostcamp_2024.course.quiz.navigation.navigateQuiz
import kr.boostcamp_2024.course.quiz.navigation.navigateQuizResult
import kr.boostcamp_2024.course.quiz.navigation.quizNavGraph
import kr.boostcamp_2024.course.study.navigation.navigateCreateStudy
import kr.boostcamp_2024.course.study.navigation.navigateStudy
import kr.boostcamp_2024.course.study.navigation.studyNavGraph
import kotlin.reflect.KClass

@Composable
fun WeQuizNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: KClass<*> = LoginRoute::class,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        loginNavGraph(
            onLoginSuccess = {
                navController.popBackStack() // 로그인 시 로그인 화면 제거, 추후 수정 필요
                navController.navigateMain()
            },
        )

        mainNavGraph(
            onNavigationButtonClick = navController::navigateUp,
            onNotificationButtonClick = navController::navigateNotification,
            onCreateStudyButtonClick = navController::navigateCreateStudy,
            onStudyGroupClick = navController::navigateStudy,
        )

        studyNavGraph(
            onNavigationButtonClick = navController::navigateUp,
            onCreateStudySuccess = navController::navigateUp,
            onCreateCategoryButtonClick = navController::navigateCreateCategory,
            onCategoryClick = navController::navigateCategory,
        )

        categoryNavGraph(
            onNavigationButtonClick = navController::navigateUp,
            onCreateQuizButtonClick = navController::navigateCreateQuiz,
            onQuizClick = navController::navigateQuiz,
            onCreateCategorySuccess = navController::navigateUp,
        )

        quizNavGraph(
            onNavigationButtonClick = navController::navigateUp,
            onCreateQuestionSuccess = navController::navigateUp,
            onQuizFinished = navController::navigateQuizResult,
            onQuestionClick = navController::navigateQuestionDetail,
            onCreateQuizSuccess = navController::navigateUp,
            onCreateQuestionButtonClick = navController::navigateCreateQuestion,
            onStartQuizButtonClick = navController::navigateQuestion,
        )
    }
}
