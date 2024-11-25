package kr.boostcamp_2024.course.quiz.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.quiz.presentation.question.CreateQuestionScreen
import kr.boostcamp_2024.course.quiz.presentation.question.QuestionDetailScreen
import kr.boostcamp_2024.course.quiz.presentation.question.QuestionScreen
import kr.boostcamp_2024.course.quiz.presentation.quiz.CreateQuizScreen
import kr.boostcamp_2024.course.quiz.presentation.quiz.QuizResultScreen
import kr.boostcamp_2024.course.quiz.presentation.quiz.QuizScreen

@Serializable
data class CreateQuestionRoute(
    val quizId: String,
)

@Serializable
data class QuestionDetailRoute(
    val questionId: String,
)

@Serializable
data class QuestionRoute(
    val quizId: String,
)

@Serializable
data class QuizRoute(
    val categoryId: String,
    val quizId: String,
)

@Serializable
data class QuizResultRoute(
    val userOmrId: String? = null,
    val quizId: String? = null,
)

@Serializable
data class CreateQuizRoute(
    val categoryId: String,
    val quizId: String? = null,
)

fun NavController.navigateCreateQuestion(quizId: String) {
    navigate(CreateQuestionRoute(quizId))
}

fun NavController.navigateQuestionDetail(questionId: String) {
    navigate(QuestionDetailRoute(questionId))
}

fun NavController.navigateQuestion(quizId: String) {
    navigate(QuestionRoute(quizId))
}

fun NavController.navigateQuiz(categoryId: String, quizId: String) {
    navigate(QuizRoute(categoryId, quizId))
}

fun NavController.navigateQuizResult(userOmrId: String? = null, quizId: String? = null) {
    popBackStack()
    navigate(QuizResultRoute(userOmrId, quizId))
}

fun NavController.navigateCreateQuiz(categoryId: String, quizId: String? = null) {
    navigate(CreateQuizRoute(categoryId, quizId))
}

fun NavGraphBuilder.quizNavGraph(
    onNavigationButtonClick: () -> Unit,
    onCreateQuestionSuccess: () -> Unit,
    onQuizFinished: (String?, String?) -> Unit,
    onQuestionClick: (String) -> Unit,
    onCreateQuizSuccess: () -> Unit,
    onCreateQuestionButtonClick: (String) -> Unit,
    onStartQuizButtonClick: (String) -> Unit,
    onSettingMenuClick: (String, String) -> Unit,
    onEditQuizSuccess: () -> Unit,
    onQuizDeleteSuccess: () -> Unit,
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
    composable<QuestionRoute> {
        QuestionScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onQuizFinished = onQuizFinished,
        )
    }
    composable<QuizRoute> {
        QuizScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onCreateQuestionButtonClick = onCreateQuestionButtonClick,
            onStartQuizButtonClick = onStartQuizButtonClick,
            onSettingMenuClick = onSettingMenuClick,
            onQuizDeleteSuccess = onQuizDeleteSuccess,
        )
    }
    composable<QuizResultRoute> {
        QuizResultScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onQuestionClick = onQuestionClick,
        )
    }
    composable<CreateQuizRoute> {
        CreateQuizScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onCreateQuizSuccess = onCreateQuizSuccess,
            onEditQuizSuccess = onEditQuizSuccess,
        )
    }
}
