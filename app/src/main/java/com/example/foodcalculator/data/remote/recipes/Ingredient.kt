package com.example.foodcalculator.data.remote.recipes

data class Ingredient(
    val text: String,
    val quantity: Float,
    val measure: String,
    val food: String,
    val weight: Float,
    val foodCategory: String,
    val imageUrl: String
)
