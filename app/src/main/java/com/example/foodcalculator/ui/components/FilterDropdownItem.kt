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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdownItem(text: String, listOfFilterItems: List<String>) {
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
        listOfFilterItems.forEach { item ->
            var state by rememberSaveable { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item,
                    modifier = Modifier
                        .weight(1f)
                )
                RadioButton(
                    selected = state,
                    onClick = { state = !state }
                )
            }
        }
    }
}