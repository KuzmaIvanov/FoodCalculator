package com.example.foodcalculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.foodcalculator.R
import com.example.foodcalculator.data.remote.recipes.Recipe
import com.example.foodcalculator.viewmodel.RecipesViewModel
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun RecipeDetailsScreen(
    navController: NavController,
    recipeAsJson: String,
    userId: String,
    recipesViewModel: RecipesViewModel,
    fromMyRecipes: Boolean
) {
    val recipe = Gson().fromJson(recipeAsJson, Recipe::class.java)
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.recipe_details_screen)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if(fromMyRecipes) {
                        IconButton(onClick = {
                            recipesViewModel.deleteRecipeFromFireStore(userId, recipe.id)
                            navController.navigateUp()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_cancel),
                                contentDescription = null
                            )
                        }
                    } else {
                        IconButton(onClick = { recipesViewModel.saveRecipeToFireStore(userId, recipe) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add_no_circle),
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        },
        content = { paddingValue ->
            Surface(modifier = Modifier.padding(paddingValue)) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    GlideImage(
                        model = recipe.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth
                    )
                    Text(text = recipe.label)
                    Text(text = "Total calories: ${recipe.calories}")
                    Text(text = "Total weight: ${recipe.totalWeight}")
                    Text(text = "Total time: ${recipe.totalTime ?: "empty"}")
                    Text(text = "Servings: ${recipe.yield ?: "empty"}")
                    ShowListOfString(name = "Ingredients", list = recipe.ingredientLines)
                    ShowListOfString(name = "Diet", list = recipe.dietLabels)
                    ShowListOfString(name = "Health", list = recipe.healthLabels)
                    ShowListOfString(name = "Cuisine type", list = recipe.cuisineType)
                    ShowListOfString(name = "Meal type", list = recipe.mealType)
                    ShowListOfString(name = "Dish type", list = recipe.dishType)
                }
            }
        }
    )
}

@Composable
fun ShowListOfString(name: String, list: List<String>) {
    Text(text = "$name:")
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        list.forEach {
            Text(text = it)
        }
    }
}