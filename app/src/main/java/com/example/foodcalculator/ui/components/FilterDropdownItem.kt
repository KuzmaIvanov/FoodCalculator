package com.example.foodcalculator.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.foodcalculator.R

@Composable
fun FilterDropdownItem(text: String, listOfFilterItems: List<FilterItem>) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier
                .weight(1f)
        )
        IconButton(onClick = { isExpanded = !isExpanded }) {
            Icon(
                painter = if(isExpanded) painterResource(id = R.drawable.ic_expand_less) else painterResource(
                    id = R.drawable.ic_expand_more
                ), 
                contentDescription = null 
            )
        }
    }
    if(isExpanded) {
        FilterItemList(list = listOfFilterItems)
    }
}
class FilterItem(
    val label: String,
    initialChecked: Boolean = false
) {
    var checked: Boolean by mutableStateOf(initialChecked)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterItemList(
    list: List<FilterItem>
) {
    list.forEach { item ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = item.label,
                modifier = Modifier
                    .weight(1f)
            )
            RadioButton(
                selected = item.checked,
                onClick = { item.checked = !item.checked }
            )
        }
    }
}