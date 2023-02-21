package com.example.foodcalculator.data.remote.plants

import com.squareup.moshi.Json

data class Plant(
    val author: String,
    val bibliography: String,
    @Json(name = "common_name") val commonName: String,
    val family: String,
    @Json(name = "family_common_name") val familyCommonName: String,
    val genus: String,
    @Json(name = "genus_id") val genusId: Int,
    val id: Int,
    @Json(name = "image_url") val imageUrl: String,
    val links: Links,
    val rank: String,
    @Json(name = "scientific_name") val scientificName: String,
    val slug: String,
    val status: String,
    val synonyms: List<String>,
    val year: Int
)