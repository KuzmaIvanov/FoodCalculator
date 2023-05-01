package com.example.foodcalculator.navigation

import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.foodcalculator.sign_in.GoogleAuthUiClient
import com.example.foodcalculator.ui.screens.*
import com.example.foodcalculator.viewmodel.PlantsViewModel
import com.example.foodcalculator.viewmodel.RecipesViewModel
import com.example.foodcalculator.viewmodel.SignInViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(context: Context, scope: LifecycleCoroutineScope, googleAuthUiClient: GoogleAuthUiClient) {
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
            if(currentRoute(navController = navController) != Screen.SignIn.route) {
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
        }
    ) { innerPadding ->
        val plantsViewModel = hiltViewModel<PlantsViewModel>()
        val recipesViewModel = hiltViewModel<RecipesViewModel>()
        NavHost(
            navController = navController,
            startDestination = Screen.SignIn.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.SignIn.route) {
                val signInViewModel = viewModel<SignInViewModel>()
                val state by signInViewModel.state.collectAsStateWithLifecycle()

                LaunchedEffect(key1 = Unit) {
                    if(googleAuthUiClient.getSignedInUser() != null) {
                        navController.navigate(Screen.MyRecipes.route)
                    }
                }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if(result.resultCode == RESULT_OK) {
                            scope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                signInViewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )
                
                LaunchedEffect(key1 = state.isSignInSuccessful) {
                    if(state.isSignInSuccessful) {
                        Toast.makeText(
                            context,
                            "Sign in successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(Screen.MyRecipes.route)
                        signInViewModel.resetState()
                    }
                }
                
                SignInScreen(
                    state = state,
                    onSignInClick = {
                        scope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    }
                )
            }
            composable(Screen.MyRecipes.route) {
                Text(text = "This is a My Recipes Page")
            }
            composable(Screen.Search.route) {
                RecipesSearchScreen(navController = navController, recipesViewModel = recipesViewModel)
            }
            composable(Screen.Create.route) {
                CreateRecipeScreen(recipesViewModel = recipesViewModel)
            }
            composable(Screen.MyGarden.route) {
                MyGardenScreen(navController = navController, plantsViewModel = plantsViewModel)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    userData = googleAuthUiClient.getSignedInUser(),
                    onSignOut = {
                        scope.launch {
                            googleAuthUiClient.signOut()
                            Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.SignIn.route)
                        }
                    }
                )
            }
            composable(Screen.AddPlant.route) {
                PlantsSearchScreen(navController = navController, plantsViewModel = plantsViewModel)
            }
            composable(Screen.FilterPlant.route) {
                FilterPlantScreen(navController = navController, plantsViewModel = plantsViewModel)
            }
            composable(Screen.FilterRecipe.route) {
                FilterRecipeScreen(navController = navController, recipesViewModel = recipesViewModel, context = context)
            }
            composable(
                route = Screen.RecipeDetails.route+"/recipe={recipe}",
                arguments = listOf(navArgument("recipe") {
                    type = NavType.StringType
                    nullable = false
                })
            ) {
                RecipeDetailsScreen(
                    navController = navController,
                    recipeAsJson = it.arguments?.getString("recipe")!!
                )
            }
            composable(
                route = Screen.PlantDetails.route+"/plant={plant}",
                arguments = listOf(navArgument("plant") {
                    type = NavType.StringType
                    nullable = false
                })
            ) {
                PlantDetailsScreen(
                    navController = navController,
                    plantAsJson = it.arguments?.getString("plant")!!
                )
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}