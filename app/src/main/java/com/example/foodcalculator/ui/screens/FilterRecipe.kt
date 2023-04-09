package com.example.foodcalculator.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.foodcalculator.navigation.Screen
import com.example.foodcalculator.viewmodel.RecipesViewModel

@Composable
fun FilterRecipeScreen(navController: NavController, recipesViewModel: RecipesViewModel) {
    Text(text = "This is filter recipe page!")
}