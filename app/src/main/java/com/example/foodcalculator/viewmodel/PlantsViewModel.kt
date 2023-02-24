package com.example.foodcalculator.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcalculator.data.remote.plants.Plant
import com.example.foodcalculator.data.repository.PlantsRepository
import com.example.foodcalculator.other.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class PlantsViewModel @Inject constructor(
    private val plantsRepository: PlantsRepository,
    private val appContext: Application
): ViewModel() {
    private val _plants = listOf<Plant>().toMutableStateList()
    val plants: List<Plant>
        get() = _plants
    init {
        getPlants()
    }
    private fun getPlants() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val plantsObject = plantsRepository.getPlants(Constants.PLANTS_ACCESS_TOKEN)
                plantsObject.plants.forEach { _plants.add(it) }
            } catch (e: Exception) {
                Toast.makeText(appContext, "Failed to load all plants!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getSearchPlants(plantName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val plantsObject = plantsRepository.getSearchPlants(Constants.PLANTS_ACCESS_TOKEN, plantName)
                _plants.clear()
                plantsObject.plants.forEach { _plants.add(it) }
            } catch (e: Exception) {
                Log.d("FAILED TO FIND", "FAILED ${e.message}")
                Toast.makeText(appContext, "Failed to find plants!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}