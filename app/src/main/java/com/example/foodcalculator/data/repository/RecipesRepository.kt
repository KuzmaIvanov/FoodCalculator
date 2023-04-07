package com.example.foodcalculator.data.repository

import com.example.foodcalculator.data.remote.recipes.RecipesApi

class RecipesRepository(
    private val recipesApi: RecipesApi
) {
    suspend fun getRecipes(
        type: String = "public",
        q: String,
        appId: String,
        appKey: String,
        diet: Array<String>? = null,
        health: Array<String>? = null,
        cuisineType: Array<String>? = null,
        mealType: Array<String>? = null,
        dishType: Array<String>? = null,
        calories: String? = null,
        time: String? = null
    ) = recipesApi.getRecipes(type, q, appId, appKey, diet, health, cuisineType, mealType, dishType, calories, time)
}