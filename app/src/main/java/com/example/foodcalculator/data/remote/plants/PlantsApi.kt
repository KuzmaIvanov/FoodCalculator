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
}