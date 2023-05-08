package com.example.foodcalculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodcalculator.R
import com.example.foodcalculator.navigation.Screen
import com.example.foodcalculator.ui.components.RecipeCards
import com.example.foodcalculator.ui.components.SearchView
import com.example.foodcalculator.viewmodel.RecipesViewModel

@Composable
fun RecipesSearchScreen(navController: NavController, recipesViewModel: RecipesViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.recipes_search_screen)
        )
        Spacer(modifier = Modifier.height(8.dp))
        SearchView(
            hint = stringResource(id = R.string.recipes_search_view_hint),
            navController = navController,
            onSearch = {
                recipesViewModel.getRecipes(it)       
            },
            onFiltersClick = {
                navController.navigate(Screen.FilterRecipe.route)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        RecipeCards(
            recipes = recipesViewModel.recipes,
            navController = navController,
            fromMyRecipes = false
        )
    }
}