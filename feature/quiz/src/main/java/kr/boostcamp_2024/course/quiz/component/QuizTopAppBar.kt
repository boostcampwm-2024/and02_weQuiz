package kr.boostcamp_2024.course.quiz.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizBaseDialog
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz
import kr.boostcamp_2024.course.quiz.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun QuizTopAppBar(
    category: Category?,
    quiz: BaseQuiz?,
    currentUserId: String?,
    onWaitingRealTimeQuizButtonClick: (Boolean) -> Unit,
    onNavigationButtonClick: () -> Unit,
    onSettingMenuClick: (String, String) -> Unit,
    onDeleteMenuClick: (String, BaseQuiz) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    var isSettingMenuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick =
                    if (quiz is RealTimeQuiz && quiz.waitingUsers.contains(currentUserId)) {
                        { showDialog = true }
                    } else {
                        { onNavigationButtonClick() }
                    },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        actions = {
            IconButton(onClick = { isSettingMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.des_quiz_screen_top_app_bar_action_icon),
                )
            }
            DropdownMenu(
                expanded = isSettingMenuExpanded,
                onDismissRequest = { isSettingMenuExpanded = false },
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.txt_edit_quiz_drop_down_menu_item)) },
                    onClick = {
                        category?.let { category ->
                            quiz?.let { quiz ->
                                onSettingMenuClick(category.id, quiz.id)
                            }
                            isSettingMenuExpanded = false
                        }
                    },
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.txt_delete_quiz_drop_down_menu_item)) },
                    onClick = {
                        category?.let { category ->
                            quiz?.let { quiz ->
                                onDeleteMenuClick(category.id, quiz)
                            }
                        }
                        isSettingMenuExpanded = false
                    },
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
    )

    if (showDialog) {
        WeQuizBaseDialog(
            title = stringResource(R.string.txt_waiting_cancel_dialog),
            dialogImage = painterResource(R.drawable.quiz_system_profile),
            confirmTitle = stringResource(R.string.txt_waiting_cancel_dialog_confirm),
            dismissTitle = stringResource(R.string.txt_waiting_cancel_dialog_dismiss),
            onConfirm = {
                showDialog = false
                onWaitingRealTimeQuizButtonClick(false)
            },
            onDismissRequest = { showDialog = false },
            content = { /* no-op */ },
        )
    }
}
