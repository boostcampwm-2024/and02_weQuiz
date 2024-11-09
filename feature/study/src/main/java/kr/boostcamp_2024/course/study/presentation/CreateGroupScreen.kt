package kr.boostcamp_2024.course.study.presentation

import android.util.Patterns
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizBaseDialog
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizTextField
import kr.boostcamp_2024.course.study.R

@Composable
fun CreateGroupScreen(onDismissButtonClick: () -> Unit, onConfirmButtonClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    val isEmailValid = remember(email) { Patterns.EMAIL_ADDRESS.matcher(email).matches() }
    WeQuizBaseDialog(
        title = "그룹원의 이메일을 입력해주세요!\n초대 메일이 갑니다!",
        dialogImage = painterResource(id = R.drawable.waterfall),
        confirmTitle = "초대",
        dismissTitle = "취소",
        onConfirm = { if (isEmailValid) onConfirmButtonClick() },
        onDismissRequest = onDismissButtonClick,
        content = {
            WeQuizTextField(
                label = "그룹원 이메일",
                text = email,
                onTextChanged = { email = it },
                placeholder = "그룹원 이메일을 입력주세요."
            )
        },
        confirmButtonEnabled = isEmailValid
    )
}

@Preview
@Composable
private fun CreateGroupScreenPreview() {
    CreateGroupScreen(onConfirmButtonClick = {}, onDismissButtonClick = {})
}
