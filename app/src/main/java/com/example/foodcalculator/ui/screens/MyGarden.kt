package com.example.foodcalculator.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.foodcalculator.viewmodel.PlantsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyGardenScreen(navController: NavController, plantsViewModel: PlantsViewModel) {
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
            Text(text = "This is my garden", modifier = Modifier.padding(it))
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