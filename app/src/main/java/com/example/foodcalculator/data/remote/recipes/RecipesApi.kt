package com.example.foodcalculator.data.remote.recipes

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipesApi {
    @GET("v2")
    suspend fun getRecipes(
        @Query("type") type: String,
        @Query("q") q: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("diet") diet: Array<String>?,
        @Query("health") health: Array<String>?,
        @Query("cuisineType") cuisineType: Array<String>?,
        @Query("mealType") mealType: Array<String>?,
        @Query("dishType") dishType: Array<String>?,
        @Query("calories") calories: String?, // "100-300" will return all recipes with which have between 100 and 300 kcal per serving. Or MAX, MIN+
        @Query("time") time: String?, //"1-60" minutes, MAX, MIN+
        @Query("nutrients[SUGAR]") sugar: String?,
        @Query("nutrients[PROCNT]") protein: String?,
        @Query("nutrients[CA]") calcium: String?,
        @Query("nutrients[FE]") iron: String?,
        @Query("nutrients[P]") phosphorus: String?,
        @Query("nutrients[VITC]") vitaminC: String?,
        @Query("nutrients[VITA_RAE]") vitaminA: String?,
    ): JsonObject
}