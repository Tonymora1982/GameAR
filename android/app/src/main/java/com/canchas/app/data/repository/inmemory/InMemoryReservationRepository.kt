package com.canchas.app.data.repository.inmemory

import com.canchas.app.data.model.Reservation
import com.canchas.app.data.model.ReservationStatus
import com.canchas.app.data.repository.ReservationRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryReservationRepository @Inject constructor(): ReservationRepository {
    private val reservations = MutableStateFlow(listOf<Reservation>())

    override suspend fun createReservation(reservation: Reservation): Result<String> = runCatching {
        val has = checkConflict(reservation.venueId, reservation.startTime, reservation.endTime).getOrThrow()
        if (has) throw IllegalStateException("Horario no disponible")
        val id = "r" + System.currentTimeMillis().toString(36)
        reservations.update { it + reservation.copy(id = id, status = ReservationStatus.PENDING) }
        id
    }

    override fun getReservationsByVenue(venueId: String, start: Timestamp, end: Timestamp): Flow<List<Reservation>> = callbackFlow {
        val sub = subscribe { list ->
            val filtered = list.filter { it.venueId == venueId &&
                it.status in listOf(ReservationStatus.PENDING, ReservationStatus.CONFIRMED) &&
                it.startTime < end && it.endTime > start }
            trySend(filtered)
        }
        awaitClose { sub.close() }
    }

    override suspend fun checkConflict(venueId: String, start: Timestamp, end: Timestamp): Result<Boolean> = runCatching {
        reservations.value.any { it.venueId == venueId &&
            it.status in listOf(ReservationStatus.PENDING, ReservationStatus.CONFIRMED) &&
            it.startTime < end && it.endTime > start }
    }

    private fun subscribe(block: (List<Reservation>) -> Unit): AutoCloseable {
        var closed = false
        val job = kotlinx.coroutines.GlobalScope.launch {
            reservations.collect { if (!closed) block(it) }
        }
        return AutoCloseable { closed = true; job.cancel() }
    }
}