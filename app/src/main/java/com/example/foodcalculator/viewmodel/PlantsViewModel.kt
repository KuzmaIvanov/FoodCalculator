package com.example.foodcalculator.viewmodel

import androidx.lifecycle.ViewModel
import com.example.foodcalculator.data.repository.PlantsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlantsViewModel @Inject constructor(
    private val plantsRepository: PlantsRepository
): ViewModel() {

}