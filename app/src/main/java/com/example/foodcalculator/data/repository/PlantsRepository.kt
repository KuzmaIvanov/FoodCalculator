package com.example.foodcalculator.data.repository

import com.example.foodcalculator.data.remote.plants.PlantsApi

class PlantsRepository(
    private val plantsApi: PlantsApi
) {
    suspend fun getPlants(token: String) = plantsApi.getPlants(token)
    suspend fun getSearchPlants(token: String, q: String) = plantsApi.getSearchPlants(token, q)
    suspend fun getFilterPlants(
        token: String,
        edibleParts: String?,
        bloomMonths: String?,
        fruitColors: String?
    ) = plantsApi.getFilterPlants(token, edibleParts, bloomMonths, fruitColors)

    suspend fun getSearchFilterPlants(
        token: String,
        q: String,
        edibleParts: String,
        bloomMonths: String,
        fruitColors: String
    ) = plantsApi.getSearchFilterPlants(token, q, edibleParts, bloomMonths, fruitColors)
}