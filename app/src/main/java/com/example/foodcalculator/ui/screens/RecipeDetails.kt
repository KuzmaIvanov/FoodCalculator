package com.example.foodcalculator.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.foodcalculator.R
import com.example.foodcalculator.data.remote.recipes.Recipe
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun RecipeDetailsScreen(navController: NavController, recipeAsJson: String) {
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

                }
            )
        },
        content = {
            Surface(modifier = Modifier.padding(it)) {
                GlideImage(
                    model = recipe.imageUrl,
                    contentDescription = null
                )
            }
        }
    )
}