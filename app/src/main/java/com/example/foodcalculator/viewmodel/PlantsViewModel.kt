package com.example.foodcalculator.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcalculator.data.remote.plants.Plant
import com.example.foodcalculator.data.repository.PlantsRepository
import com.example.foodcalculator.other.Constants
import com.example.foodcalculator.ui.components.FilterItem
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class PlantsViewModel @Inject constructor(
    private val plantsRepository: PlantsRepository,
    private val appContext: Application,
    private val firestore: FirebaseFirestore
): ViewModel() {
    private val _plants = listOf<Plant>().toMutableStateList()
    val plants: List<Plant>
        get() = _plants

    /*Подумать над тем, как можно вынести явное определение списка фильтров из ViewModel, может в values*/
    private val listOfEdibleParts = listOf("fruits", "leaves", "roots", "flowers", "seeds", "stem")
    val statesOfEdibleParts = getFilterStatesAsMutableStateList(listOfEdibleParts)

    private val listOfBloomMonths = listOf("may", "august", "july")
    val statesOfBloomMonths = getFilterStatesAsMutableStateList(listOfBloomMonths)

    private val listOfFruitColors = listOf("red", "green", "blue", "yellow", "green")
    val statesOfFruitColors = getFilterStatesAsMutableStateList(listOfFruitColors)

    fun getPlants() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val plantsObject = plantsRepository.getPlants(Constants.PLANTS_ACCESS_TOKEN)
                _plants.clear()
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
                Toast.makeText(appContext, "Failed to find plants!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFilterStatesAsMutableStateList(list: List<String>): SnapshotStateList<FilterItem>{
        return List(list.size) { i -> FilterItem(list[i])}.toMutableStateList()
    }

    fun getFilterPlants() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val plantsObject = plantsRepository.getFilterPlants(
                    Constants.PLANTS_ACCESS_TOKEN,
                    getFilterArgumentsAsString(statesOfEdibleParts),
                    getFilterArgumentsAsString(statesOfBloomMonths),
                    getFilterArgumentsAsString(statesOfFruitColors)
                )
                _plants.clear()
                plantsObject.plants.forEach { _plants.add(it) }
                //возможно еще придется обновить statesOf на false все, посмотреть как в других прилажках
            } catch (e: Exception) {
                Toast.makeText(appContext.applicationContext, "Failed to find plants (with filters)!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getSearchFilterPlants(q: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val plantsObject = plantsRepository.getSearchFilterPlants(
                    Constants.PLANTS_ACCESS_TOKEN,
                    q,
                    getFilterArgumentsAsString(statesOfEdibleParts),
                    getFilterArgumentsAsString(statesOfBloomMonths),
                    getFilterArgumentsAsString(statesOfFruitColors)
                )
                _plants.clear()
                plantsObject.plants.forEach { _plants.add(it) }
            } catch (e: Exception) {
                Log.d("ERROR", "You must enter all fields!")
            }
        }
    }

    private fun getFilterArgumentsAsString(list: SnapshotStateList<FilterItem>): String? {
        val sb = StringBuilder()
        list.forEach {
            if(it.checked) {
                sb.append(it.label, ',')
            }
        }
        val result = sb.toString()
        return if (result.isEmpty()) null else result.substring(0, result.length - 1)
    }

    fun savePlantToFireStore(userId: String, plant: Plant) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestore.collection("users")
                    .document(userId)
                    .collection("plants")
                    .document(plant.id.toString())
                    .set(
                        hashMapOf(
                            "author" to plant.author,
                            "common_name" to plant.commonName,
                            "family" to plant.family,
                            "family_common_name" to plant.familyCommonName,
                            "id" to plant.id,
                            "image_url" to plant.imageUrl,
                            "scientific_name" to plant.scientificName,
                            "year" to plant.year,
                            "synonyms" to plant.synonyms
                        )
                    )
                    .await()
            } catch (e: Exception) {
                Log.d("FIRESTORE", "Failed to save data: ${e.message}")
            }
        }
    }

    fun getGardenPlants(userId: String): SnapshotStateList<Plant> {
        val gardenPlants = listOf<Plant>().toMutableStateList()
        firestore.collection("users")
            .document(userId)
            .collection("plants")
            .get()
            .addOnSuccessListener { result ->
                for(document in result) {
                    val plant = document.data
                    gardenPlants.add(
                        Plant(
                            author = plant["author"].toString(),
                            bibliography = null,
                            commonName = plant["common_name"].toString(),
                            family = plant["family"].toString(),
                            familyCommonName = plant["family_common_name"].toString(),
                            id = (plant["id"] as Long).toInt(),
                            imageUrl = plant["image_url"].toString(),
                            scientificName = plant["scientific_name"].toString(),
                            year = (plant["year"] as Long).toInt(),
                            synonyms = plant["synonyms"] as List<String>,
                            genus = null,
                            genusId = null,
                            links = null,
                            rank = null,
                            slug = null,
                            status = null
                        )
                    )
                }
            }
            .addOnFailureListener {
                Log.d("FIRESTORE", "Failed to load data")
            }
        return gardenPlants
    }

    fun deleteGardenPlant(userId: String, plantId: Int) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                firestore.collection("users")
                    .document(userId)
                    .collection("plants")
                    .document(plantId.toString())
                    .delete()
                    .await()
            }
        } catch (e: Exception) {
            Log.d("FIRESTORE", "Failed to delete plant")
        }
    }
}