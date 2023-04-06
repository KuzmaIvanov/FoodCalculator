package com.example.foodcalculator.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.foodcalculator.viewmodel.RecipesViewModel

@Composable
fun RecipesSearchScreen(navController: NavController, recipesViewModel: RecipesViewModel) {
    Button(onClick = { recipesViewModel.getRecipes() }) {
        Text(text = "Click me")
    }
}