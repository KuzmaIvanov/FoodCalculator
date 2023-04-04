package com.example.foodcalculator.data.remote.plants

import retrofit2.http.GET
import retrofit2.http.Query

interface PlantsApi {
    @GET("plants")
    suspend fun getPlants(
        @Query("token") token: String
    ) : Plants

    @GET("plants/search")
    suspend fun getSearchPlants(
        @Query("token") token: String,
        @Query("q") q: String
    ) : Plants

    @GET("plants")
    suspend fun getFilterPlants(
        @Query("token") token: String,
        @Query("filter[edible_part]") edibleParts: String?,
        @Query("filter[bloom_months]") bloomMonths: String?,
        @Query("filter[fruit_color]") fruitColors: String?
    ) : Plants

    @GET("plants/search")
    suspend fun getSearchFilterPlants(
        @Query("token") token: String,
        @Query("q") q: String,
        @Query("filter[edible_part]") edibleParts: String?,
        @Query("filter[bloom_months]") bloomMonths: String?,
        @Query("filter[fruit_color]") fruitColors: String?
    ) : Plants
}