package kr.boostcamp_2024.course.quiz.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.quiz.QuizResultScreen
import kr.boostcamp_2024.course.quiz.presentation.question.CreateQuestionScreen
import kr.boostcamp_2024.course.quiz.presentation.question.QuestionDetailScreen
import kr.boostcamp_2024.course.quiz.presentation.question.QuestionScreen
import kr.boostcamp_2024.course.quiz.presentation.quiz.QuizScreen

@Serializable
data object CreateQuestionRoute

@Serializable
data object QuestionDetailRoute

@Serializable
data object QuestionScreenRoute

@Serializable
data object QuizRoute

@Serializable
data object QuizResultRoute

fun NavController.navigateCreateQuestion() {
    navigate(CreateQuestionRoute)
}

fun NavController.navigateQuestionDetail() {
    navigate(QuestionDetailRoute)
}

fun NavController.navigateQuestionScreen() {
    navigate(QuestionScreenRoute) {
        popUpTo(QuizRoute) {
            inclusive = true
        }
    }
}

fun NavController.navigateQuiz() {
    navigate(QuizRoute)
}

fun NavController.navigateQuizResult() {
    navigate(QuizResultRoute) {
        popUpTo(QuestionScreenRoute) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.quizNavGraph(
    onNavigationButtonClick: () -> Unit,
    onCreateQuestionSuccess: () -> Unit,
    onQuizFinished: () -> Unit,
    onQuizStartButtonClick: () -> Unit,
    onQuestionClick: () -> Unit,
) {
    composable<CreateQuestionRoute> {
        CreateQuestionScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onCreateQuestionSuccess = onCreateQuestionSuccess,
        )
    }
    composable<QuestionDetailRoute> {
        QuestionDetailScreen(
            onNavigationButtonClick = onNavigationButtonClick,
        )
    }
    composable<QuestionScreenRoute> {
        QuestionScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onQuizFinished = onQuizFinished,
        )
    }
    composable<QuizRoute> {
        QuizScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onQuizStartButtonClick = onQuizStartButtonClick,
        )
    }
    composable<QuizResultRoute> {
        QuizResultScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onQuestionClick = onQuestionClick,
        )
    }
}


