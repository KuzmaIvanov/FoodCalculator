package com.example.foodcalculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.foodcalculator.viewmodel.PlantsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterPlantScreen(navController: NavController, plantsViewModel: PlantsViewModel) {
    var plantName by rememberSaveable {
        mutableStateOf("")
    }
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.filter_plant_screen)
                    )
                },
                navigationIcon = {
                    /*нужно еще не забыть сделать reset*/
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(painter = painterResource(id = R.drawable.ic_cancel), contentDescription = null)
                    }
                },
                actions = {
                    TextButton(onClick = {
                        if(plantName.isEmpty()) {
                            plantsViewModel.getFilterPlants()
                            navController.navigateUp()
                        }
                    }) {
                        Text(text = stringResource(id = R.string.action_apply))
                    }
                }
            )
        },
        content = { innerPadding ->
            Surface(modifier = Modifier.padding(innerPadding)) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
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
                    LazyColumn {
                        item {
                            FilterDropdownItem(
                                text = "Edible parts",
                                listOfFilterItems = plantsViewModel.statesOfEdibleParts
                            )
                        }
                        item {
                            FilterDropdownItem(
                                text = "Bloom months",
                                listOfFilterItems = plantsViewModel.statesOfBloomMonths
                            )
                        }
                        item {
                            FilterDropdownItem(
                                text = "Fruit color",
                                listOfFilterItems = plantsViewModel.statesOfFruitColors
                            )
                        }
                    }
                }
            }
        }
    )
}