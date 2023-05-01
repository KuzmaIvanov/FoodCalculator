package com.example.foodcalculator.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.foodcalculator.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int?,
    @DrawableRes val drawableId: Int?
) {
    object MyRecipes: Screen("MyRecipes", R.string.my_recipes_label, R.drawable.ic_my_recipes)
    object Search: Screen("Search", R.string.search_label, R.drawable.ic_search)
    object Create: Screen("Create", R.string.create_label, R.drawable.ic_create)
    object MyGarden: Screen("MyGarden", R.string.my_garden_label, R.drawable.ic_my_garden)
    object Profile: Screen("Profile", R.string.profile_label, R.drawable.ic_profile)
    object AddPlant: Screen("AddPlant", null, null)
    object FilterPlant: Screen("FilterPlant", null, null)
    object FilterRecipe: Screen("FilterRecipe", null, null)
    object RecipeDetails: Screen("RecipeDetails", null, null)
    object PlantDetails: Screen("PlantDetails", null, null)
    object SignIn: Screen("SignIn", null, null)
}
