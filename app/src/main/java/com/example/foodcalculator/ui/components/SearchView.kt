package com.example.foodcalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodcalculator.R

@Composable
fun SearchView(
    hint: String,
    navController: NavController,
    onSearch: (String) -> Unit,
    onFiltersClick: () -> Unit
) {
    Row {
        SearchBar(
            hint = hint,
            modifier = Modifier
                .weight(1f),
            onSearch = onSearch
        ) {
            navController.navigateUp()
        }
        Spacer(modifier = Modifier
            .width(8.dp))
        FiltersButton(
            onFiltersClick = onFiltersClick,
            modifier = Modifier
                .shadow(5.dp, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .size(44.dp)
        )
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {},
    onNavigateUp: () -> Unit
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }
    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if(text.isNotEmpty()) {
                        onSearch(text)
                    }
                }
            ),
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(horizontal = 40.dp, vertical = 14.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused != true
                }
        )
        if(isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 40.dp, vertical = 12.dp)
            )
        }
        IconButton(onClick = onNavigateUp) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    }
}

@Composable
fun FiltersButton(
    modifier: Modifier = Modifier,
    onFiltersClick: () -> Unit = {}
) {
    FloatingActionButton(
        onClick = onFiltersClick,
        modifier = modifier,
        containerColor = Color.White
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = stringResource(id = R.string.filter_button_content_desc)
        )
    }
}