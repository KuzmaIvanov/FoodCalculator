package com.example.foodcalculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodcalculator.R
import com.example.foodcalculator.navigation.Screen
import com.example.foodcalculator.ui.components.ApplicationTopAppbar
import com.example.foodcalculator.ui.components.PlantCards
import com.example.foodcalculator.viewmodel.PlantsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyGardenScreen(navController: NavController, plantsViewModel: PlantsViewModel, userId: String) {
    Scaffold(
        topBar = {
            ApplicationTopAppbar(title = stringResource(id = R.string.my_garden_label))
        },
        floatingActionButton = {
            AddPlantFloatingActionButton {
                navController.navigate(Screen.AddPlant.route)
                plantsViewModel.getPlants()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            Surface(modifier = Modifier.padding(it)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    plantsViewModel.getGardenPlants(userId)
                    PlantCards(plants = plantsViewModel.gardenPlants, navController = navController)
                }
            }
        }
    )
}

@Composable
fun AddPlantFloatingActionButton(
    onAddPlantClick: () -> Unit = {}
) {
    ExtendedFloatingActionButton(
        onClick = onAddPlantClick
    ) {
        Text(text = stringResource(id = R.string.add_a_new_plant))
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_plants),
            contentDescription = null
        )
    }
}