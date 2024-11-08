package com.practice.weather.view


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.practice.weather.model.ApiResponse
import com.practice.weather.viewmodel.AppViewModel
import com.practice.weather.viewmodel.UIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavHostController, viewModel: AppViewModel, cityName: String) {
    val weatherState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getWeather(cityName, "a4b47cfe49be6c892d4fcab8bf735784")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Weather in $cityName") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (weatherState.value is UIState.Success) {
                                viewModel.addCity(cityName)
                            }
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (weatherState.value) {
                is UIState.Loading -> CircularProgressIndicator()
                is UIState.Success -> WeatherDetails(weather = (weatherState.value as UIState.Success).data)
                is UIState.Error -> Text("Invalid City: $cityName")
                UIState.Empty -> Unit
            }
        }
    }
}

@Composable
fun WeatherDetails(weather: ApiResponse) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Temp: ${weather.main.temp}°C")
                Text(text = "Humidity: ${weather.main.humidity}%")
                Text(text = "Pressure: ${weather.main.pressure} hPa")
                if (weather.weather.isNotEmpty()) {
                    Text(text = "Condition: ${weather.weather[0].description}")
                }
            }
            if (weather.weather.isNotEmpty()) {
                val imageUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png"
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.height(135.dp)
                ) {
                    Icon(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(64.dp),
                        tint = Color(0xFF87CEEB)
                    )
                }
            }
        }
    }
}
