package com.practice.weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.practice.weather.model.ApiResponse
import com.practice.weather.model.AppRepository
import com.practice.weather.model.Main
import com.practice.weather.viewmodel.AppViewModel
import com.practice.weather.viewmodel.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
class AppViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var viewModel: AppViewModel

    @Mock
    private lateinit var repository: AppRepository

    // Test dispatcher to run coroutines in the test environment
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Set up the ViewModel with a mocked repository
        viewModel = AppViewModel(repository)

        // Set the main dispatcher for testing
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `test getWeather success state`() = runTest {
        // Arrange
        val mockResponse = ApiResponse(Main(1.0f, 5,3), listOf(),"test")
        val city = "New York"
        val apiKey = "fakeApiKey"

        // Mock the repository's behavior
        Mockito.`when`(repository.getWeather(city, apiKey)).thenReturn(Result.success(mockResponse))

        // Act
        viewModel.getWeather(city, apiKey)
        testDispatcher.scheduler.advanceUntilIdle() // Ensure that coroutines are executed

        // Assert
        val state = viewModel.uiState.first() // Get the first emitted state
        assertEquals(UIState.Success(mockResponse), state)
    }

    @Test
    fun `test getWeather error state`() = runTest {
        // Arrange
        val city = "New York"
        val apiKey = "fakeApiKey"
        val errorMessage = "Network error"

        // Mock the repository's behavior to return an error
        Mockito.`when`(repository.getWeather(city, apiKey)).thenReturn(Result.failure(Exception(errorMessage)))

        // Act
        viewModel.getWeather(city, apiKey)
        testDispatcher.scheduler.advanceUntilIdle() // Ensure that coroutines are executed

        // Assert
        val state = viewModel.uiState.first() // Get the first emitted state
        assertEquals(UIState.Error(errorMessage), state)
    }

    @Test
    fun `test addCity method`() {
        // Arrange
        val city = "New York"

        // Act
        viewModel.addCity(city)

        // Assert
        // Verify that the city is added to the list
        assert(viewModel.cityList.value.contains(city))
    }

    @Test
    fun `test initial UIState is Empty`() {
        // Assert that the initial state of the UIState is Empty
        assertEquals(UIState.Empty, viewModel.uiState.value)
    }
}
