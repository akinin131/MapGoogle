package com.example.googlemapsapi.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CoordinatesModel
import com.example.domain.usecase.SaveCoordinatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor (private val saveUseCase: SaveCoordinatesUseCase) : ViewModel() {

    fun saveTest(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val test = CoordinatesModel(latitude = latitude, longitude = longitude)
            saveUseCase.execute(test)

        }
    }
}