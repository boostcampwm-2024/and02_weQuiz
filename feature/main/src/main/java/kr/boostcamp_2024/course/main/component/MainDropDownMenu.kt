package kr.boostcamp_2024.course.main.component

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kr.boostcamp_2024.course.main.R

@Composable
fun MainDropDownMenu(
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
