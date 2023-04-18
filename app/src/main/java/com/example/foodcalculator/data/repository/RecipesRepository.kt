package com.example.foodcalculator.data.repository

import com.example.foodcalculator.data.remote.recipes.RecipePost
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
        calories: String?,
        time: String?,
        sugar: String?,
        protein: String?,
        calcium: String?,
        iron: String?,
        phosphorus: String?,
        vitaminC: String?,
        vitaminA: String?
    ) = recipesApi.getRecipes(
        type,
        q,
        appId,
        appKey,
        diet,
        health,
        cuisineType,
        mealType,
        dishType,
        calories,
        time,
        sugar,
        protein,
        calcium,
        iron,
        phosphorus,
        vitaminC,
        vitaminA
    )

    suspend fun getNutritionAnalysis(
        appId: String,
        appKey: String,
        recipePost: RecipePost
    ) = recipesApi.getNutritionAnalysis(appId, appKey, recipePost)
}