package kr.boostcamp_2024.course.login.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizTextField
import kr.boostcamp_2024.course.login.R
import kr.boostcamp_2024.course.login.presentation.component.PasswordTextField

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigationButtonClick: () -> Unit,
) {
    SignupScreen(
        onSignUpSuccess,
        onNavigationButtonClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignupScreen(
    onSignUpSuccess: () -> Unit,
    onNavigationButtonClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.top_app_bar_sign_up),
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigationButtonClick,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.des_ic_back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = innerPadding.calculateBottomPadding(),
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                SignUpContent()
            }
            item {
                SignUpButtons(
                    onSignUpSuccess = onSignUpSuccess,
                )
            }
        }
    }
}

@Composable
fun SignUpContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(148.dp)
                .clip(shape = MaterialTheme.shapes.large)
                .clickable(enabled = true) {
                    // todo: photo picker
                },
            painter = painterResource(kr.boostcamp_2024.course.designsystem.R.drawable.img_photo_picker),
            contentDescription = stringResource(R.string.des_img_photo_picker),
            contentScale = ContentScale.FillWidth,
        )

        WeQuizTextField(
            label = stringResource(R.string.txt_login_email_label),
            text = "",
            onTextChanged = {},
            placeholder = stringResource(R.string.txt_login_email_placeholder),
        )

        PasswordTextField(
            password = "",
            onPasswordChanged = {},
        )

        WeQuizTextField(
            label = stringResource(R.string.txt_sign_up_nick_name),
            text = "",
            onTextChanged = {},
            placeholder = stringResource(R.string.txt_sign_up_nick_name_placeholder),
        )
    }
}

@Composable
fun SignUpButtons(
    onSignUpSuccess: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.btn_sign_up),
            )
        }

        OutlinedButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.btn_sign_up_cancel),
            )
        }
    }
}

@Preview
@Composable
fun PreviewSignUpScreen() {
    WeQuizTheme {
        SignUpScreen(
            onSignUpSuccess = {},
            onNavigationButtonClick = {},
        )
    }
}
