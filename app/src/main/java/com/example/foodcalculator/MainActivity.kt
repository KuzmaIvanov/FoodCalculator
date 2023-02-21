package com.example.foodcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.foodcalculator.navigation.Navigation
import com.example.foodcalculator.ui.components.SearchBar
import com.example.foodcalculator.ui.theme.FoodCalculatorTheme
import com.example.foodcalculator.viewmodel.PlantsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodCalculatorTheme {
                val viewModel = hiltViewModel<PlantsViewModel>()
                Navigation()
            }
        }
    }
}