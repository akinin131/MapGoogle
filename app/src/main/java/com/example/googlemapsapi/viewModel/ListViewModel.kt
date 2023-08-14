package com.example.googlemapsapi.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CoordinatesModel
import com.example.domain.usecase.GetCoordinatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val getCoordinatesUseCase: GetCoordinatesUseCase) : ViewModel() {

    private val coordinatesData = MutableLiveData<List<CoordinatesModel>>()
    val coordinatesLiveData: LiveData<List<CoordinatesModel>> get() = coordinatesData

    fun getAllCoordinates() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val coordinates = getCoordinatesUseCase.execute()
                coordinatesData.postValue(coordinates)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

