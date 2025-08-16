package com.canchas.app.data.repository

import com.canchas.app.data.model.Reservation
import com.canchas.app.data.model.ReservationStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface ReservationRepository {
    suspend fun createReservation(reservation: Reservation): Result<String>
    fun getReservationsByVenue(venueId: String, start: Timestamp, end: Timestamp): Flow<List<Reservation>>
    suspend fun checkConflict(venueId: String, start: Timestamp, end: Timestamp): Result<Boolean>
}

@Singleton
class FirestoreReservationRepository @Inject constructor(
    private val db: FirebaseFirestore
) : ReservationRepository {

    private val col get() = db.collection("reservations")

    override suspend fun createReservation(reservation: Reservation): Result<String> = runCatching {
        val hasConflict = checkConflict(reservation.venueId, reservation.startTime, reservation.endTime).getOrThrow()
        if (hasConflict) throw IllegalStateException("Horario no disponible")
        val id = db.runTransaction { tx ->
            val ref = col.document()
            tx.set(ref, reservation.copy(id = ref.id))
            ref.id
        }.await()
        id
    }

    override fun getReservationsByVenue(venueId: String, start: Timestamp, end: Timestamp): Flow<List<Reservation>> = callbackFlow {
        val reg = col
            .whereEqualTo("venueId", venueId)
            .whereIn("status", listOf(ReservationStatus.PENDING.value, ReservationStatus.CONFIRMED.value))
            .whereLessThan("startTime", end)
            .whereGreaterThan("endTime", start)
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val list = snap?.documents?.mapNotNull { doc ->
                    val r = doc.toObject(Reservation::class.java)
                    r?.copy(id = doc.id)
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { reg.remove() }
    }

    override suspend fun checkConflict(venueId: String, start: Timestamp, end: Timestamp): Result<Boolean> = runCatching {
        val snap = col
            .whereEqualTo("venueId", venueId)
            .whereIn("status", listOf(ReservationStatus.PENDING.value, ReservationStatus.CONFIRMED.value))
            .whereLessThan("startTime", end)
            .whereGreaterThan("endTime", start)
            .get().await()
        snap.documents.isNotEmpty()
    }
}