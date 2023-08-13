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
class ListViewModel @Inject constructor(private val getUseCase: GetCoordinatesUseCase) : ViewModel() {
    private val _testsLiveData = MutableLiveData<List<CoordinatesModel>>()
    val testsLiveData: LiveData<List<CoordinatesModel>> get() = _testsLiveData

    private val _coordinatesLiveData = MutableLiveData<List<CoordinatesModel>>()
    val coordinatesLiveData: LiveData<List<CoordinatesModel>> get() = _coordinatesLiveData

    fun getAllTests() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val tests = getUseCase.execute()
                _testsLiveData.postValue(tests)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAllCoordinates() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val coordinates = getUseCase.execute()
                _coordinatesLiveData.postValue(coordinates)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

