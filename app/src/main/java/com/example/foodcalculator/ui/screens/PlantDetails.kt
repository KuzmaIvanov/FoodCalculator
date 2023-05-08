package com.example.foodcalculator.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import com.example.foodcalculator.data.remote.plants.Plant
import com.example.foodcalculator.viewmodel.PlantsViewModel
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun PlantDetailsScreen(
    navController: NavController,
    plantAsJson: String,
    userId: String,
    plantsViewModel: PlantsViewModel,
    context: Context,
    fromGarden: Boolean
) {
    val plant = Gson().fromJson(plantAsJson, Plant::class.java)
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.plant_details_screen)
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
                    if(fromGarden) {
                        IconButton(onClick = {
                            plantsViewModel.deleteGardenPlant(userId, plant.id)
                            navController.navigateUp()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_cancel),
                                contentDescription = null
                            )
                        }
                    } else {
                        IconButton(onClick = { plantsViewModel.savePlantToFireStore(userId, plant) }) {
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
                        model = plant.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth
                    )
                    Text(text = plant.commonName ?: "")
                    Text(text = "Family: ${plant.family ?: ""}")
                    Text(text = "Family common name: ${plant.familyCommonName}")
                    Text(text = "Scientific name: ${plant.scientificName}")
                    ShowListOfString(name = "Synonyms", list = plant.synonyms ?: listOf())
                    if(plant.author != null && plant.year != null) {
                        Text(text = "Author is ${plant.author}, found in ${plant.year}")
                    }
                }
            }
        }
    )
}