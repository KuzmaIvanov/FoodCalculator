package com.example.foodcalculator.ui.components

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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.foodcalculator.data.remote.plants.Plant

@Composable
fun Cards(plants: List<Plant>) {
    LazyColumn {
        items(plants) {
            val commonName = it.commonName ?: ""
            val family = it.family ?: ""
            val imageUrl = it.imageUrl ?: "" //если imgUrl = null, то можно загрузить просто дефолтную картинку
            PlantCard(plantCommonName = commonName, plantFamily = family, imageUrl = imageUrl)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun PlantCard(plantCommonName: String, plantFamily: String, imageUrl: String) {
    Card(
        onClick = { },
        modifier = Modifier.size(width = 150.dp, height = 200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        GlideImage(
            model = imageUrl,
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
            Text(text = plantCommonName)
            Text(text = plantFamily)
        }
    }
}