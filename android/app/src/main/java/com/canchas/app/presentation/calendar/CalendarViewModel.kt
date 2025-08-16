package com.canchas.app.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canchas.app.data.model.Reservation
import com.canchas.app.data.model.ReservationStatus
import com.canchas.app.data.repository.ReservationRepository
import com.canchas.app.data.repository.VenueRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val reservationRepository: ReservationRepository,
    private val venueRepository: VenueRepository
) : ViewModel() {

    data class UiState(
        val date: Date = today(),
        val reservations: List<Reservation> = emptyList(),
        val message: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var currentVenueId: String? = null

    fun load(venueId: String) {
        currentVenueId = venueId
        viewModelScope.launch {
            reservationRepository.getReservationsByVenue(venueId, dayStart(_uiState.value.date), dayEnd(_uiState.value.date))
                .collectLatest { list -> _uiState.value = _uiState.value.copy(reservations = list, message = null) }
        }
    }

    fun prevDay() { changeDay(-1) }
    fun nextDay() { changeDay(1) }

    private fun changeDay(delta: Int) {
        val cal = Calendar.getInstance()
        cal.time = _uiState.value.date
        cal.add(Calendar.DAY_OF_YEAR, delta)
        val newDate = cal.time
        _uiState.value = _uiState.value.copy(date = newDate)
        currentVenueId?.let { load(it) }
    }

    fun create(venueId: String, startHour: Int, endHour: Int) = viewModelScope.launch {
        val s = ts(_uiState.value.date, startHour)
        val e = ts(_uiState.value.date, endHour)
        val reservation = Reservation(
            venueId = venueId,
            userId = "guest",
            userName = "Invitado",
            userEmail = "",
            startTime = s,
            endTime = e,
            totalPrice = ((endHour - startHour) * 1.0)
        )
        val result = reservationRepository.createReservation(reservation)
        _uiState.value = if (result.isSuccess) _uiState.value.copy(message = "Reservación creada") else _uiState.value.copy(message = result.exceptionOrNull()?.localizedMessage)
    }

    companion object {
        private fun today(): Date {
            val cal = Calendar.getInstance()
            cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
            return cal.time
        }
        private fun dayStart(date: Date): Timestamp {
            val cal = Calendar.getInstance(); cal.time = date
            cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
            return Timestamp(cal.time)
        }
        private fun dayEnd(date: Date): Timestamp {
            val cal = Calendar.getInstance(); cal.time = date
            cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59); cal.set(Calendar.SECOND, 59); cal.set(Calendar.MILLISECOND, 999)
            return Timestamp(cal.time)
        }
        private fun ts(date: Date, hour: Int): Timestamp {
            val cal = Calendar.getInstance(); cal.time = date
            cal.set(Calendar.HOUR_OF_DAY, hour); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
            return Timestamp(cal.time)
        }
    }
}