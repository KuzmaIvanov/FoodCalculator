package com.example.foodcalculator.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.foodcalculator.data.remote.recipes.Recipe

@Composable
fun RecipeCards(recipes: List<Recipe>) {
    LazyColumn {
        items(recipes) {
            RecipeCard(recipe = it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun RecipeCard(recipe: Recipe) {
    Card(
        onClick = {},
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = recipe.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(width = 150.dp, height = 150.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = recipe.label,)
                Text(text = "${recipe.yield} servings")
                Text(text = "Total weight: ${recipe.totalWeight} g")
                if(recipe.totalTime != 0) {
                    Text(text = "Total time: ${recipe.totalTime} min")
                }
                Text(text = "${recipe.calories} kcal")
            }
        }
    }
}