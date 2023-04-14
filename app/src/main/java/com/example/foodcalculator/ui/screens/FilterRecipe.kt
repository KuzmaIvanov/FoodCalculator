package com.example.foodcalculator.ui.screens

import android.content.Context
import android.widget.Toast
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
import com.example.foodcalculator.viewmodel.RecipesViewModel
import com.example.foodcalculator.R
import com.example.foodcalculator.ui.components.FilterDropdownItem
import com.example.foodcalculator.ui.components.FilterRangeItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterRecipeScreen(navController: NavController, recipesViewModel: RecipesViewModel, context: Context) {
    var recipeName by rememberSaveable {
        mutableStateOf("")
    }
    val calories = rememberSaveable {
        mutableStateOf("")
    }
    val time = rememberSaveable {
        mutableStateOf("")
    }
    val sugar = rememberSaveable {
        mutableStateOf("")
    }
    val protein = rememberSaveable {
        mutableStateOf("")
    }
    val calcium = rememberSaveable {
        mutableStateOf("")
    }
    val iron = rememberSaveable {
        mutableStateOf("")
    }
    val phosphorus = rememberSaveable {
        mutableStateOf("")
    }
    val vitaminC = rememberSaveable {
        mutableStateOf("")
    }
    val vitaminA = rememberSaveable {
        mutableStateOf("")
    }
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.filter_recipe_screen))
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(painter = painterResource(id = R.drawable.ic_cancel), contentDescription = null)
                    }
                },
                actions = {
                    TextButton(onClick = {
                        if(recipeName.isEmpty()) {
                            Toast.makeText(context, "You must enter query text", Toast.LENGTH_SHORT).show()
                        } else {
                            recipesViewModel.getRecipes(
                                q = recipeName,
                                calories = calories.value.ifEmpty { null },
                                time = time.value.ifEmpty { null },
                                sugar = sugar.value.ifEmpty { null },
                                protein = protein.value.ifEmpty { null },
                                calcium = calcium.value.ifEmpty { null },
                                iron = iron.value.ifEmpty { null },
                                phosphorus = phosphorus.value.ifEmpty { null },
                                vitaminC = vitaminC.value.ifEmpty { null },
                                vitaminA = vitaminA.value.ifEmpty { null }
                            )
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
                        value = recipeName,
                        onValueChange = {
                            recipeName = it
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
                                text = "Diets",
                                listOfFilterItems = recipesViewModel.statesOfDietLabels
                            )
                        }
                        item {
                            FilterDropdownItem(
                                text = "Health",
                                listOfFilterItems = recipesViewModel.statesOfHealthLabels
                            )
                        }
                        item {
                            FilterDropdownItem(
                                text = "Cuisine type",
                                listOfFilterItems = recipesViewModel.statesOfCuisineType
                            )
                        }
                        item {
                            FilterDropdownItem(
                                text = "Meal type",
                                listOfFilterItems = recipesViewModel.statesOfMealType
                            )
                        }
                        item {
                            FilterDropdownItem(
                                text = "Dish type",
                                listOfFilterItems = recipesViewModel.statesOfDishType
                            )
                        }
                        item {
                            FilterRangeItem(label = "Calories", unit = "kcal",text = calories)
                            FilterRangeItem(label = "Total time", unit = "min", text = time)
                            FilterRangeItem(label = "Sugars", unit = "g", text = sugar)
                            FilterRangeItem(label = "Protein", unit = "g", text = protein)
                            FilterRangeItem(label = "Calcium", unit = "mg", text = calcium)
                            FilterRangeItem(label = "Iron", unit = "mg", text = iron)
                            FilterRangeItem(label = "Phosphorus", unit = "mg", text = phosphorus)
                            FilterRangeItem(label = "Vitamin C", unit = "mg", text = vitaminC)
                            FilterRangeItem(label = "Vitamin A", unit = "mg", text = vitaminA)
                        }
                    }
                }
            }
        }
    )
}