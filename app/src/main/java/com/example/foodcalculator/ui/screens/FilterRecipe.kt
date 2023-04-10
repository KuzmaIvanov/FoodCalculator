package com.example.foodcalculator.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodcalculator.viewmodel.RecipesViewModel
import com.example.foodcalculator.R
import com.example.foodcalculator.ui.components.FilterDropdownItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterRecipeScreen(navController: NavController, recipesViewModel: RecipesViewModel, context: Context) {
    var recipeName by remember {
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
                            recipesViewModel.getRecipes(q = recipeName)
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
                    }
                }
            }
        }
    )
}