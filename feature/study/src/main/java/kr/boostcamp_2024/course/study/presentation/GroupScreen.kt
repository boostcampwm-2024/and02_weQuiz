package kr.boostcamp_2024.course.study.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.component.CustomPropertyTab
import kr.boostcamp_2024.course.study.component.GroupItem

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
        GroupLazyColumn()
    }
}

@Composable
fun GroupLazyColumn() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding()
    ) {
        items(10) { index ->
            GroupItem(
                removeButtonClick = { Log.d("detail", "group lazy column 아이템 클릭됨") },
                profileImg = null,
                name = "홍길동"
            )

            if (index < 9) {
                HorizontalDivider()
            }
        }
    }
}
