package com.example.foodcalculator.viewmodel

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcalculator.data.remote.recipes.Recipe
import com.example.foodcalculator.data.repository.RecipesRepository
import com.example.foodcalculator.other.Constants
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
): ViewModel() {
    private val _recipes = listOf<Recipe>().toMutableStateList()
    val recipes: List<Recipe>
        get() = _recipes

    fun getRecipes(q: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resultJsonObject = recipesRepository.getRecipes(
                    q = q,
                    appId = Constants.RECIPES_APP_ID,
                    appKey = Constants.RECIPES_APP_KEY
                )
                _recipes.clear()
                initializeRecipesFromJsonObject(resultJsonObject)
            } catch (e: Exception) {
                Log.d("ERROR", "ERROR in recipes req, ${e.message}")
            }
        }
    }
    private fun jsonArrayOfStringFromJsonObjectToList(jsonObject: JsonObject, arrayMemberName: String): List<String> {
        val array = jsonObject.getAsJsonArray(arrayMemberName)
        val result = mutableListOf<String>()
        array.forEach {
            result.add(it.asString)
        }
        return result
    }
    private fun initializeRecipesFromJsonObject(jsonObject: JsonObject) {
        val hits = jsonObject.getAsJsonArray("hits")
        hits.forEach { it ->
            val recipeAsJsonObject = it.asJsonObject.getAsJsonObject("recipe")
            _recipes.add(Recipe(
                label = recipeAsJsonObject.get("label").asString,
                imageUrl = recipeAsJsonObject.get("image").asString,
                dietLabels = jsonArrayOfStringFromJsonObjectToList(recipeAsJsonObject, "dietLabels"),
                healthLabels = jsonArrayOfStringFromJsonObjectToList(recipeAsJsonObject, "healthLabels"),
                ingredientLines = jsonArrayOfStringFromJsonObjectToList(recipeAsJsonObject, "ingredientLines"),
                calories = recipeAsJsonObject.get("calories").asFloat,
                totalWeight = recipeAsJsonObject.get("totalWeight").asFloat,
                totalTime = recipeAsJsonObject.get("totalTime").asInt,
                cuisineType = jsonArrayOfStringFromJsonObjectToList(recipeAsJsonObject, "cuisineType"),
                mealType = jsonArrayOfStringFromJsonObjectToList(recipeAsJsonObject, "mealType"),
                dishType = jsonArrayOfStringFromJsonObjectToList(recipeAsJsonObject, "dishType"),
                yield = recipeAsJsonObject.get("yield").asInt
            ))
        }
    }
}