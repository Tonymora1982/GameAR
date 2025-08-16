package com.canchas.app.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canchas.app.data.model.Venue
import com.canchas.app.data.repository.VenueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val venueRepository: VenueRepository
): ViewModel() {

    data class MapUiState(
        val loading: Boolean = false,
        val venues: List<Venue> = emptyList(),
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        loadVenues()
    }

    private fun loadVenues() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            venueRepository.getAllActiveVenues()
                .catch { e -> _uiState.value = MapUiState(error = e.localizedMessage ?: "Error") }
                .collect { list -> _uiState.value = MapUiState(venues = list) }
        }
    }
}