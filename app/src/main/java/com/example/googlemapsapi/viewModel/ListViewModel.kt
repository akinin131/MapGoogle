package com.example.googlemapsapi.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CoordinatesModel
import com.example.domain.usecase.GetCoordinatesUseCase
import com.example.domain.usecase.SaveCoordinatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val getUseCase: GetCoordinatesUseCase) :ViewModel(){
    private val _testsLiveData = MutableLiveData<List<CoordinatesModel>>()
    val testsLiveData: LiveData<List<CoordinatesModel>> get() = _testsLiveData
    fun getAllTests() {
        viewModelScope.launch {
            val tests = getUseCase.execute()
            _testsLiveData.postValue(tests)
        }
    }
}