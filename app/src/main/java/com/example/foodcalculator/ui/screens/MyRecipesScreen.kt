package com.example.foodcalculator.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodcalculator.R
import com.example.foodcalculator.ui.components.ApplicationTopAppbar
import com.example.foodcalculator.ui.components.RecipeCards
import com.example.foodcalculator.viewmodel.RecipesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRecipesScreen(
    navController: NavController,
    recipesViewModel: RecipesViewModel,
    userId: String
) {
    Scaffold(
        topBar = {
            ApplicationTopAppbar(title = stringResource(id = R.string.my_recipes_label))
        },
        content = {
            Surface(modifier = Modifier.padding(it)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    RecipeCards(recipes = recipesViewModel.getMyRecipes(userId), navController = navController, fromMyRecipes = true)
                }
            }
        }
    )
}