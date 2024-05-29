package com.palash.pedometer_sensor_example.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.palash.pedometer_sensor_example.repositories.StepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepViewModel @Inject constructor(private val stepRepository: StepRepository) : ViewModel() {

    val stepData: LiveData<Int> = stepRepository.stepData

    fun startStepUpdates() {
        viewModelScope.launch {
            stepRepository.startStepUpdates()
        }
    }

    fun stopStepUpdates() {
        viewModelScope.launch {
            stepRepository.stopStepUpdates()
        }
    }
}