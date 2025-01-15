package kr.boostcamp_2024.course.category.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kr.boostcamp_2024.course.category.R
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme

@Composable
internal fun CategorySettingMenu(
    expanded: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDropDownMenuClick: () -> Unit,
    onDropDownMenuDismiss: () -> Unit,
) {
    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopEnd),
    ) {
        IconButton(
            onClick = { onDropDownMenuClick() },
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.des_btn_settings),
            )
        }
        CategoryDropdownMenu(
            expanded = expanded,
            onDropDownMenuDismiss = onDropDownMenuDismiss,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
        )
    }
}

@Composable
internal fun CategoryDropdownMenu(
    expanded: Boolean,
    onDropDownMenuDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDropDownMenuDismiss() },
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.txt_category_fix)) },
            onClick = {
                onDropDownMenuDismiss()
                onEditClick()
            },
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.txt_category_delete)) },
            onClick = {
                onDropDownMenuDismiss()
                onDeleteClick()
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CategorySettingMenuPreview() {
    WeQuizTheme {
        CategorySettingMenu(
            expanded = false,
            onEditClick = {},
            onDeleteClick = {},
            onDropDownMenuClick = {},
            onDropDownMenuDismiss = {},
        )
    }
}

@Preview
@Composable
private fun CategoryDropdownMenuPreview() {
    WeQuizTheme {
        CategoryDropdownMenu(
            expanded = true,
            onDropDownMenuDismiss = {},
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}
