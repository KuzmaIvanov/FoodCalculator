package com.example.foodcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.foodcalculator.navigation.Navigation
import com.example.foodcalculator.sign_in.GoogleAuthUiClient
import com.example.foodcalculator.ui.theme.FoodCalculatorTheme
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodCalculatorTheme {
                Navigation(applicationContext, lifecycleScope, googleAuthUiClient)
            }
        }
    }
}