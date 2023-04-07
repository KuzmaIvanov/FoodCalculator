package com.example.foodcalculator.data.remote.recipes

data class Recipe(
    val label: String,
    val imageUrl: String,
    val dietLabels: List<String>,
    val healthLabels: List<String>,
    val ingredientLines: List<String>,
    val calories: Float,
    val totalWeight: Float,
    val totalTime: Int,
    val cuisineType: List<String>,
    val mealType: List<String>,
    val dishType: List<String>
)
