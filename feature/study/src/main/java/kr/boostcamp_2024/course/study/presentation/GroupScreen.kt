package kr.boostcamp_2024.course.study.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.component.CustomPropertyTab

@Composable
fun GroupScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {
        CustomPropertyTab(
            onClicked = { Log.d("detail", "그룹 클릭됨") },
            icon = painterResource(R.drawable.baseline_add_circle_outline_24),
            title = "그룹원"
        )
    }
}
