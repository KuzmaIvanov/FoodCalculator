package com.example.foodcalculator.data.remote.recipes

data class RecipePost(
    val title: String,
    val ingr: List<String>,
    val url: String?,
    val summary: String,
    val yield: String,
    val time: String,
    val img: String?,
    val prep: String
)
