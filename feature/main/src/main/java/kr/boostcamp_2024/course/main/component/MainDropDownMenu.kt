package kr.boostcamp_2024.course.main.component

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.main.R

@Composable
internal fun MainDropDownMenu(
    isExpanded: Boolean,
    onDismissRequest: () -> Unit,
    onEditUserClick: () -> Unit,
    onLogOutClick: () -> Unit,
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = onDismissRequest,
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.txt_main_menu_edit)) },
            onClick = {
                onEditUserClick()
                onDismissRequest()
            },
        )
        HorizontalDivider()
        DropdownMenuItem(
            text = { Text(stringResource(R.string.txt_main_menu_logout)) },
            onClick = {
                onLogOutClick()
                onDismissRequest()
            },
        )
    }
}

@Preview(showBackground = true, locale = "ko")
@Composable
private fun MainDropDownMenuPreview() {
    WeQuizTheme {
        MainDropDownMenu(
            isExpanded = true,
            onDismissRequest = {},
            onEditUserClick = {},
            onLogOutClick = {},
        )
    }
}
