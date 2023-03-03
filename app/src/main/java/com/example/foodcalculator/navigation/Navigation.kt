package com.example.foodcalculator.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodcalculator.ui.screens.FilterPlantScreen
import com.example.foodcalculator.ui.screens.MyGardenScreen
import com.example.foodcalculator.ui.screens.PlantsSearchScreen
import com.example.foodcalculator.viewmodel.PlantsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val screens = listOf(
        Screen.MyRecipes,
        Screen.Search,
        Screen.Create,
        Screen.MyGarden,
        Screen.Profile
    )
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.drawableId!!),
                                contentDescription = null
                            )
                        },
                        label = { Text(text = stringResource(id = screen.resourceId!!)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        val plantsViewModel = hiltViewModel<PlantsViewModel>()
        NavHost(
            navController = navController,
            startDestination = Screen.MyRecipes.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.MyRecipes.route) {
                Text(text = "This is a My Recipes Page")
            }
            composable(Screen.Search.route) {
                Text(text = "This is a Search Page")
            }
            composable(Screen.Create.route) {
                Text(text = "This is a Create Page")
            }
            composable(Screen.MyGarden.route) {
                MyGardenScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                Text(text = "This is a Profile Page")
            }
            composable(Screen.AddPlant.route) {
                PlantsSearchScreen(navController = navController, plantsViewModel = plantsViewModel)
            }
            composable(Screen.FilterPlant.route) {
                FilterPlantScreen(navController = navController)
            }
        }
    }
}