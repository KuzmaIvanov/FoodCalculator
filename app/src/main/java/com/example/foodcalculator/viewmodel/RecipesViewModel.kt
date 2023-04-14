package com.example.foodcalculator.viewmodel

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcalculator.data.remote.recipes.Recipe
import com.example.foodcalculator.data.repository.RecipesRepository
import com.example.foodcalculator.other.Constants
import com.example.foodcalculator.ui.components.FilterItem
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

    private val listOfDietLabels = listOf(
        "balanced",
        "high-fiber",
        "high-protein",
        "low-carb",
        "low-fat",
        "low-sodium"
    )
    val statesOfDietLabels = getFilterStatesAsMutableStateList(listOfDietLabels)

    private val listOfHealthLabels = listOf(
        "alcohol-cocktail",
        "alcohol-free",
        "celery-free",
        "crustacean-free",
        "dairy-free",
        "DASH",
        "egg-free",
        "fish-free",
        "fodmap-free",
        "gluten-free",
        "immuno-supportive",
        "keto-friendly",
        "kidney-friendly",
        "kosher",
        "low-fat-abs",
        "low-potassium",
        "low-sugar",
        "lupine-free",
        "mollusk-free",
        "mustard-free",
        "no-oil-added",
        "paleo",
        "peanut-free",
        "pescatarian",
        "pork-free",
        "red-meat-free",
        "sesame-free",
        "shellfish-free",
        "soy-free",
        "sugar-conscious",
        "sulfite-free",
        "tree-nut-free",
        "vegan",
        "vegetarian",
        "wheat-free"
    )
    val statesOfHealthLabels = getFilterStatesAsMutableStateList(listOfHealthLabels)

    private val listOfCuisineType = listOf(
        "American",
        "Asian",
        "British",
        "Caribbean",
        "Central Europe",
        "Chinese",
        "Eastern Europe",
        "French",
        "Indian",
        "Italian",
        "Japanese",
        "Kosher",
        "Mediterranean",
        "Mexican",
        "Middle Eastern",
        "Nordic",
        "South American",
        "South East Asian"
    )
    val statesOfCuisineType = getFilterStatesAsMutableStateList(listOfCuisineType)

    private val listOfMealType = listOf(
        "Breakfast",
        "Dinner",
        "Lunch",
        "Snack",
        "Teatime"
    )
    val statesOfMealType = getFilterStatesAsMutableStateList(listOfMealType)

    private val listOfDishType = listOf(
        "Biscuits and cookies",
        "Bread",
        "Cereals",
        "Condiments and sauces",
        "Desserts",
        "Drinks",
        "Main course",
        "Pancake",
        "Preps",
        "Preserve",
        "Salad",
        "Sandwiches",
        "Side dish",
        "Soup",
        "Starter",
        "Sweets"
    )
    val statesOfDishType = getFilterStatesAsMutableStateList(listOfDishType)

    fun getRecipes(
        q: String,
        calories: String? = null,
        time: String? = null,
        sugar: String? = null,
        protein: String? = null,
        calcium: String? = null,
        iron: String? = null,
        phosphorus: String? = null,
        vitaminC: String? = null,
        vitaminA: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resultJsonObject = recipesRepository.getRecipes(
                    q = q,
                    appId = Constants.RECIPES_APP_ID,
                    appKey = Constants.RECIPES_APP_KEY,
                    diet = fromListOfFilterItemToArray(statesOfDietLabels.filter { it.checked }),
                    health = fromListOfFilterItemToArray(statesOfHealthLabels.filter { it.checked }),
                    cuisineType = fromListOfFilterItemToArray(statesOfCuisineType.filter { it.checked }),
                    mealType = fromListOfFilterItemToArray(statesOfCuisineType.filter { it.checked }),
                    dishType = fromListOfFilterItemToArray(statesOfDishType.filter { it.checked }),
                    calories = calories,
                    time = time,
                    sugar = sugar,
                    protein = protein,
                    calcium = calcium,
                    iron = iron,
                    phosphorus = phosphorus,
                    vitaminC = vitaminC,
                    vitaminA = vitaminA
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

    private fun getFilterStatesAsMutableStateList(list: List<String>): SnapshotStateList<FilterItem> {
        return List(list.size) { i -> FilterItem(list[i]) }.toMutableStateList()
    }

    private fun fromListOfFilterItemToArray(list: List<FilterItem>): Array<String>? {
        val arrayList = arrayListOf<String>()
        list.forEach {
            arrayList.add(it.label)
        }
        return if(arrayList.isEmpty()) null else arrayList.toTypedArray()
    }
}