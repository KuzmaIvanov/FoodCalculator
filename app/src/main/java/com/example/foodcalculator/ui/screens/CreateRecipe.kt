package com.example.foodcalculator.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.foodcalculator.R
import com.example.foodcalculator.data.remote.recipes.NutritionAnalysis
import com.example.foodcalculator.data.remote.recipes.RecipePost
import com.example.foodcalculator.viewmodel.RecipesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRecipeScreen(recipesViewModel: RecipesViewModel) {
    var title by rememberSaveable {
        mutableStateOf("")
    }
    var time by rememberSaveable {
        mutableStateOf("")
    }
    var servings by rememberSaveable {
        mutableStateOf("")
    }
    var ingredientId = 0
    var ingredientName by rememberSaveable {
        mutableStateOf("")
    }
    val nutritionAnalysis by remember {
        recipesViewModel.nutritionAnalysis
    }
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.create_recipe_screen_title))
                },
                actions = {
                    TextButton(
                        onClick = {

                        }
                    ) {
                        Text(text = stringResource(id = R.string.action_save))
                    }
                }
            )
        },
        content = { innerPadding ->
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    item {
                        Text(text = stringResource(id = R.string.recipe_title))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = title,
                            onValueChange = {
                                title = it
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.recipe_title_textfield_hint),
                                    color = Color.LightGray
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    item {
                        Card(
                            onClick = {

                            },
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            border = BorderStroke(width = 1.dp, color = Color.LightGray)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_photo_camera_24),
                                    contentDescription = null,
                                    alignment = Alignment.Center
                                )
                                Text(
                                    text = stringResource(id = R.string.action_add_photo),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    item {
                        Text(text = "Cook time")
                        Text(text = "How long does it take to cook this recipe? (min)")
                        OutlinedTextField(
                            value = time,
                            onValueChange = {
                                time = it
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    item {
                        Text(text = "Servings")
                        Text(text = "This is used to scale the recipe and calculate nutrition per serving.")
                        OutlinedTextField(
                            value = servings,
                            onValueChange = {
                                servings = it
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    item {
                        Text(text = "Ingredients")
                        Text(text = "Some hints about ingredients")
                    }
                    items(
                        items = recipesViewModel.listIngredients,
                        key = { ingredient -> ingredient.id }
                    ) {
                        IngredientItem(
                            ingredientName = it.label,
                            onClose = {
                                recipesViewModel.listIngredients.remove(it)
                                if (recipesViewModel.listIngredients.isNotEmpty()) {
                                    recipesViewModel.getNutritionAnalysis(
                                        RecipePost(
                                            title = "title",
                                            ingr = recipesViewModel.listIngredients.map { listItem -> listItem.label },
                                            url = null,
                                            summary = "sum",
                                            yield = "yield",
                                            time = "time",
                                            img = null,
                                            prep = "prep"
                                        )
                                    )
                                }
                            }
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = ingredientName,
                            onValueChange = {
                                ingredientName = it
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if(ingredientName.isNotEmpty()) {
                                        recipesViewModel.listIngredients.add(Ingredient(ingredientId++, ingredientName))
                                        ingredientName = ""
                                        recipesViewModel.getNutritionAnalysis(
                                            RecipePost(
                                                title = "title",
                                                ingr = recipesViewModel.listIngredients.map { it.label },
                                                url = null,
                                                summary = "sum",
                                                yield = "yield",
                                                time = "time",
                                                img = null,
                                                prep = "prep"
                                            )
                                        )
                                    }
                                }
                            ),
                            placeholder = { Text(text = "Enter an ingredient")},
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    item {
                        if(recipesViewModel.listIngredients.isNotEmpty()) {
                            ShowNutritionAnalysis(nutritionAnalysis = nutritionAnalysis)
                        } else {
                            recipesViewModel.nutritionAnalysis.value = NutritionAnalysis()
                        }
                    }
                }
            }
        }
    )
}

data class Ingredient(
    val id: Int,
    val label: String,
)

@Composable
fun IngredientItem(
    ingredientName: String,
    onClose: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = ingredientName,
            modifier = Modifier
                .weight(1f)
        )
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }
    }
}

@Composable
fun ShowNutritionAnalysis(nutritionAnalysis: NutritionAnalysis) {
    Text(text = "Calories   ${nutritionAnalysis.calories} kcal")
    Text(text = "Total weight   ${nutritionAnalysis.totalWeight} g")
    Text(text = "Total fat   ${nutritionAnalysis.fat} g")
    Text(text = "Cholesterol   ${nutritionAnalysis.cholesterol} mg")
    Text(text = "Sodium   ${nutritionAnalysis.sodium} mg")
    Text(text = "Protein   ${nutritionAnalysis.protein} g")
    Text(text = "Vitamin D   ${nutritionAnalysis.vitaminD} µg")
    Text(text = "Calcium   ${nutritionAnalysis.calcium} mg")
    Text(text = "Iron   ${nutritionAnalysis.iron} mg")
    Text(text = "Potassium   ${nutritionAnalysis.potassium} mg")
}