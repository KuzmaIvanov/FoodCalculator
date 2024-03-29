package com.example.foodcalculator.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcalculator.data.remote.recipes.NutritionAnalysis
import com.example.foodcalculator.data.remote.recipes.Recipe
import com.example.foodcalculator.data.remote.recipes.RecipePost
import com.example.foodcalculator.data.repository.RecipesRepository
import com.example.foodcalculator.other.Constants
import com.example.foodcalculator.ui.components.FilterItem
import com.example.foodcalculator.ui.screens.Ingredient
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
    private val firestore: FirebaseFirestore
): ViewModel() {
    private val _recipes = listOf<Recipe>().toMutableStateList()
    val recipes: List<Recipe>
        get() = _recipes

    var nutritionAnalysis = mutableStateOf(NutritionAnalysis())
    val listIngredients = listOf<Ingredient>().toMutableStateList()

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

    private fun getRecipeIdFromHref(href: String): String {
        val result = StringBuilder()
        val hrefAfter = href.substringAfterLast("v2/")
        var i = 0
        while (hrefAfter[i] != '?') {
            result.append(hrefAfter[i])
            i++
        }
        return result.toString()
    }
    private fun initializeRecipesFromJsonObject(jsonObject: JsonObject) {
        val hits = jsonObject.getAsJsonArray("hits")
        hits.forEach {
            val recipeAsJsonObject = it.asJsonObject.getAsJsonObject("recipe")
            val selfAsJsonObject = it.asJsonObject.getAsJsonObject("_links").getAsJsonObject("self")
            _recipes.add(Recipe(
                id = getRecipeIdFromHref(selfAsJsonObject.get("href").asString),
                label = recipeAsJsonObject.get("label").asString,
                imageUrl = recipeAsJsonObject.get("image").asString,
                //imageUrl = recipeAsJsonObject.getAsJsonObject("images").getAsJsonObject("LARGE").get("url").asString,
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

    fun getNutritionAnalysis(recipePost: RecipePost) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val resultJsonObject = recipesRepository.getNutritionAnalysis(
                    Constants.NUTRITION_ANALYSIS_APP_ID,
                    Constants.NUTRITION_ANALYSIS_APP_KEY,
                    recipePost
                )
                getInfoFromJsonObject(resultJsonObject)
            }
        } catch (e: Exception) {
            Log.d("ERROR", e.message.toString())
        }
    }
    private fun getInfoFromTotalNutrientsJsonObject(totalNutrientsJsonObject: JsonObject, elementName: String): String {
        return totalNutrientsJsonObject.getAsJsonObject(elementName).get("quantity").asString
    }
    private fun getInfoFromJsonObject(jsonObject: JsonObject) {
        val calories = jsonObject.get("calories").asString
        val totalWeight = jsonObject.get("totalWeight").asString
        val totalNutrientsJsonObject = jsonObject.getAsJsonObject("totalNutrients")
        val fat = getInfoFromTotalNutrientsJsonObject(totalNutrientsJsonObject, "FAT")
        val cholesterol = getInfoFromTotalNutrientsJsonObject(totalNutrientsJsonObject, "CHOLE")
        val sodium = getInfoFromTotalNutrientsJsonObject(totalNutrientsJsonObject, "NA")
        val protein = getInfoFromTotalNutrientsJsonObject(totalNutrientsJsonObject, "PROCNT")
        val vitaminD = getInfoFromTotalNutrientsJsonObject(totalNutrientsJsonObject, "VITD")
        val calcium = getInfoFromTotalNutrientsJsonObject(totalNutrientsJsonObject, "CA")
        val iron = getInfoFromTotalNutrientsJsonObject(totalNutrientsJsonObject, "FE")
        val potassium = getInfoFromTotalNutrientsJsonObject(totalNutrientsJsonObject, "K")
        nutritionAnalysis.value = NutritionAnalysis(calories, totalWeight, fat, cholesterol, sodium, protein, vitaminD, calcium, iron, potassium)
    }

    fun saveRecipeToFireStore(userId: String, recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestore.collection("users")
                    .document(userId)
                    .collection("recipes")
                    .document(recipe.id)
                    .set(
                        hashMapOf(
                            "id" to recipe.id,
                            "label" to recipe.label,
                            "imageUrl" to recipe.imageUrl,
                            "dietLabels" to recipe.dietLabels,
                            "healthLabels" to recipe.healthLabels,
                            "ingredientLines" to recipe.ingredientLines,
                            "calories" to recipe.calories,
                            "totalWeight" to recipe.totalWeight,
                            "totalTime" to recipe.totalTime,
                            "cuisineType" to recipe.cuisineType,
                            "mealType" to recipe.mealType,
                            "dishType" to recipe.dishType,
                            "yield" to recipe.yield
                        )
                    )
                    .await()
            } catch (e: Exception) {
                Log.d("FIRESTORE", "Failed to save data: ${e.message}")
            }
        }
    }

    fun getMyRecipes(userId: String): SnapshotStateList<Recipe> {
        val myRecipes = listOf<Recipe>().toMutableStateList()
        firestore.collection("users")
            .document(userId)
            .collection("recipes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val recipe = document.data
                    myRecipes.add(
                        Recipe(
                            id = recipe["id"].toString(),
                            label = recipe["label"].toString(),
                            imageUrl = recipe["imageUrl"].toString(),
                            dietLabels = recipe["dietLabels"] as List<String>,
                            healthLabels = recipe["healthLabels"] as List<String>,
                            ingredientLines = recipe["ingredientLines"] as List<String>,
                            calories = (recipe["calories"] as Double).toFloat(),
                            totalWeight = (recipe["totalWeight"] as Double).toFloat(),
                            totalTime = (recipe["totalTime"] as Long).toInt(),
                            cuisineType = recipe["cuisineType"] as List<String>,
                            mealType = recipe["mealType"] as List<String>,
                            dishType = recipe["dishType"] as List<String>,
                            yield = (recipe["yield"] as Long).toInt()
                        )
                    )
                }
            }
            .addOnFailureListener {
                Log.d("FIRESTORE", "Failed to load data")
            }
        return myRecipes
    }

    fun deleteRecipeFromFireStore(userId: String, recipeId: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                firestore.collection("users")
                    .document(userId)
                    .collection("recipes")
                    .document(recipeId)
                    .delete()
                    .await()
            }
        } catch (e: Exception) {
            Log.d("FIRESTORE", "Failed to delete recipe")
        }
    }
}