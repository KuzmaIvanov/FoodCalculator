package com.example.foodcalculator.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcalculator.data.repository.RecipesRepository
import com.example.foodcalculator.other.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
): ViewModel() {
    fun getRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resultStr = recipesRepository.getRecipes(
                    q = "Chicken",
                    appId = Constants.RECIPES_APP_ID,
                    appKey = Constants.RECIPES_APP_KEY
                ).toString()
                Log.d("RESULT", resultStr.substring(0, 30))
            } catch (e: Exception) {
                Log.d("ERROR", "ERROR in recipes req, ${e.message}")
            }
        }
    }
}