package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.study.R

@ExperimentalMaterial3Api
@Composable
fun MembersDropDownMenu(
    modifier: Modifier = Modifier,
    onOptionSelected: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val membersDropDownMenuText = stringResource(R.string.txt_members_drop_down_menu)
    var selectedOption by remember { mutableStateOf(membersDropDownMenuText) }
    val dropDownMenuOptions = stringArrayResource(R.array.drop_down_menu_options)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            label = { Text(membersDropDownMenuText) },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 110.dp),
        ) {
            dropDownMenuOptions.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(index)
                        selectedOption = option
                        expanded = false
                    },
                )
            }
        }
    }
}
