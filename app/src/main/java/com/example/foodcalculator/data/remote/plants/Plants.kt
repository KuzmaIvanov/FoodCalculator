package com.example.foodcalculator.data.remote.plants

import com.squareup.moshi.Json

data class Plants(
    @Json(name = "data") val plants: List<Plant>,
    val links: LinksX,
    val meta: Meta
)