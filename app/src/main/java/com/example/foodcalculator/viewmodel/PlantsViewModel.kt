package com.example.foodcalculator.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcalculator.data.remote.plants.Plant
import com.example.foodcalculator.data.repository.PlantsRepository
import com.example.foodcalculator.other.Constants
import com.example.foodcalculator.ui.components.FilterItem
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

    /*Подумать над тем, как можно вынести явное определение списка фильтров из ViewModel, может в values*/
    private val listOfEdibleParts = listOf("fruits", "leaves", "roots", "flowers", "seeds", "stem")
    val statesOfEdibleParts = getFilterStatesAsMutableStateList(listOfEdibleParts)

    private val listOfBloomMonths = listOf("may", "august", "july")
    val statesOfBloomMonths = getFilterStatesAsMutableStateList(listOfBloomMonths)

    private val listOfFruitColors = listOf("red", "green", "blue", "yellow", "green")
    val statesOfFruitColors = getFilterStatesAsMutableStateList(listOfFruitColors)

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
                //Log.d("FAILED TO FIND", "FAILED ${e.message}")
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
                Toast.makeText(appContext, "Failed to find plants (with filters)!", Toast.LENGTH_SHORT).show()
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
}