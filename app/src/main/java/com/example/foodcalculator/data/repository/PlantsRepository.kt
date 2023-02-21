package com.example.foodcalculator.data.repository

import com.example.foodcalculator.data.remote.plants.PlantsApi
import com.example.foodcalculator.other.Constants

class PlantsRepository(
    private val plantsApi: PlantsApi
) {
    suspend fun getPlants() = plantsApi.getPlants(Constants.PLANTS_ACCESS_TOKEN)
}