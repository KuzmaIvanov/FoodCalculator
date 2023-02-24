package com.example.foodcalculator.data.repository

import com.example.foodcalculator.data.remote.plants.PlantsApi

class PlantsRepository(
    private val plantsApi: PlantsApi
) {
    suspend fun getPlants(token: String) = plantsApi.getPlants(token)
    suspend fun getSearchPlants(token: String, q: String) = plantsApi.getSearchPlants(token, q)
}