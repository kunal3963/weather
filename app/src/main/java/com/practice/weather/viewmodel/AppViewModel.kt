package com.practice.weather.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.weather.model.ApiResponse
import com.practice.weather.model.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Empty)
    val uiState = _uiState.asStateFlow()

    private val _cityList = MutableStateFlow<Set<String>>(emptySet())
    val cityList = _cityList.asStateFlow()

    fun addCity(city: String) {
        _cityList.value = _cityList.value.plus(city)
        Log.d("AppViewModel", "Added city: $city")
    }

    fun getWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val result = repository.getWeather(city, apiKey)
            _uiState.value = if (result.isSuccess) {
                UIState.Success(result.getOrNull()!!)
            } else {
                UIState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }
}

sealed class UIState {
    object Empty : UIState()
    object Loading : UIState()
    data class Success(val data: ApiResponse) : UIState()
    data class Error(val message: String) : UIState()
}