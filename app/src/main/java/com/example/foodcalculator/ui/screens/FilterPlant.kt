package com.example.foodcalculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodcalculator.R
import com.example.foodcalculator.ui.components.FilterDropdownItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterPlantScreen(navController: NavController) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.filter_plant_screen)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(painter = painterResource(id = R.drawable.ic_cancel), contentDescription = null)
                    }
                },
                actions = {
                    TextButton(onClick = {  }) {
                        Text(text = stringResource(id = R.string.action_apply))
                    }
                }
            )
        },
        content = { innerPadding ->
            Surface(modifier = Modifier.padding(innerPadding)) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    var plantName by rememberSaveable {
                        mutableStateOf("")
                    }
                    OutlinedTextField(
                        value = plantName,
                        onValueChange = {
                            plantName = it
                        },
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FilterDropdownItem(
                        text = "Edible parts",
                        listOfFilterItems = listOf("fruits", "leaves", "roots")
                    )
                    Divider()
                    FilterDropdownItem(
                        text = "Bloom months",
                        listOfFilterItems = listOf("may", "august", "july")
                    )
                }
            }
        }
    )
}