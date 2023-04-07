package com.example.foodcalculator.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.foodcalculator.data.remote.recipes.Recipe
import com.example.foodcalculator.viewmodel.RecipesViewModel

@Composable
fun RecipesSearchScreen(navController: NavController, recipesViewModel: RecipesViewModel) {
    Column {
        Button(onClick = { recipesViewModel.getRecipes() }) {
            Text(text = "Click me")
        }
        ListOfRecipes(list = recipesViewModel.recipes)
    }
}

@Composable
fun ListOfRecipes(list: List<Recipe>) {
    LazyColumn {
        items(list) {
            Text(text = it.label)
        }
    }
}