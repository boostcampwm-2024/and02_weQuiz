package kr.boostcamp_2024.course.quiz.presentation.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.quiz.presentation.component.ChatBubbleLeft
import kr.boostcamp_2024.course.quiz.presentation.component.ProfileCircleImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizScreen(
    onNavigationButtonClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "퀴즈 생성")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigationButtonClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Character Guide
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProfileCircleImage(modifier = Modifier.size(120.dp))
                ChatBubbleLeft(text = "추가할 퀴즈에 대한\n정보를 입력해주세요!")
            }

            // QuizTtitle
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                var quizTitle by remember { mutableStateOf("") }

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = quizTitle,
                    onValueChange = { quizTitle = it },
                    label = {
                        Text(text = "제목")
                    },
                    placeholder = {
                        Text(text = "퀴즈 제목을 입력하세요.")
                    },
                    trailingIcon = {
                        IconButton(onClick = { quizTitle = "" }) {
                            Icon(
                                imageVector = Icons.Outlined.Cancel,
                                contentDescription = null
                            )
                        }
                    }
                )

                var quizDescription by remember { mutableStateOf("") }

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = quizDescription,
                    onValueChange = { quizDescription = it },
                    label = {
                        Text(text = "설명")
                    },
                    placeholder = {
                        Text(text = "퀴즈 설명을 입력하세요.")
                    },
                    minLines = 6,
                    maxLines = 6,
                    trailingIcon = {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Top
                        ) {
                            IconButton(
                                modifier = Modifier.fillMaxHeight(),
                                onClick = { quizDescription = "" }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Cancel,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateQuizScreenPreview() {
    CreateQuizScreen(
        onNavigationButtonClick = {},
    )
}