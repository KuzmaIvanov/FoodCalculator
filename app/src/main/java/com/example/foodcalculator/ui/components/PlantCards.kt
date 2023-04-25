package com.example.foodcalculator.ui.components

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.foodcalculator.data.remote.plants.Plant
import com.example.foodcalculator.navigation.Screen
import com.google.gson.Gson

@Composable
fun PlantCards(plants: List<Plant>, navController: NavController) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items(plants) {
            PlantCard(it) { plant ->
                navController.navigate(
                    Screen.PlantDetails.route+"/plant={plant}"
                            .replace(
                                oldValue = "{plant}",
                                newValue = Uri.encode(Gson().toJson(plant))
                            )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun PlantCard(plant: Plant, onPlantClick: (Plant) -> Unit) {
    Card(
        onClick = { onPlantClick(plant) },
        modifier = Modifier.size(width = 150.dp, height = 200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        GlideImage(
            model = plant.imageUrl ?: "", //если imgUrl = null, то можно загрузить просто дефолтную картинку
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(125.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp)) //может внутри GlideImage есть параметр для дефолтной картинки
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = plant.commonName ?: "")
            Text(text = plant.family ?: "")
        }
    }
}