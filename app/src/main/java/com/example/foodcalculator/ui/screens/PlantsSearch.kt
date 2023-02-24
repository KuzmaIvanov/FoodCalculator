package com.example.foodcalculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodcalculator.R
import com.example.foodcalculator.ui.components.Cards
import com.example.foodcalculator.ui.components.SearchView
import com.example.foodcalculator.viewmodel.PlantsViewModel

@Composable
fun PlantsSearchScreen(
    navController: NavController,
    plantsViewModel: PlantsViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.plants_search_screen)
        )
        Spacer(modifier = Modifier.height(8.dp))
        SearchView(stringResource(id = R.string.plants_search_view_hint), navController = navController) {
            plantsViewModel.getSearchPlants(it)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Cards(plants = plantsViewModel.plants)
    }
}