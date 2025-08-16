package com.canchas.app.data.repository.inmemory

import com.canchas.app.data.model.Venue
import com.canchas.app.data.repository.VenueRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryVenueRepository @Inject constructor(): VenueRepository {
    private val venues = MutableStateFlow(
        listOf(
            Venue(id = "v1", name = "Cancha Centro", address = "CDMX Centro", location = GeoPoint(19.4326, -99.1332), pricePerHour = 300.0, capacity = 10),
            Venue(id = "v2", name = "Cancha Norte", address = "CDMX Norte", location = GeoPoint(19.5, -99.12), pricePerHour = 250.0, capacity = 12),
            Venue(id = "v3", name = "Cancha Sur", address = "CDMX Sur", location = GeoPoint(19.35, -99.18), pricePerHour = 280.0, capacity = 8)
        )
    )

    override fun getAllActiveVenues(): Flow<List<Venue>> = callbackFlow {
        val sub = venues.subscribe { trySend(it) }
        awaitClose { sub.dispose() }
    }

    override suspend fun createVenue(venue: Venue): Result<String> = runCatching {
        val id = "v" + System.currentTimeMillis().toString(36)
        val payload = venue.copy(id = id, createdAt = Timestamp.now(), updatedAt = Timestamp.now())
        venues.update { it + payload }
        id
    }

    override suspend fun getVenue(id: String): Result<Venue> = runCatching {
        venues.value.firstOrNull { it.id == id } ?: throw IllegalStateException("Venue not found")
    }

    // Simple subscription helper for StateFlow in callbackFlow
    private fun <T> MutableStateFlow<T>.subscribe(block: (T) -> Unit): AutoCloseable {
        var closed = false
        val job = kotlinx.coroutines.GlobalScope
        val collector = kotlinx.coroutines.flow.onEach(this) { if (!closed) block(it) }
        val jobHandle = kotlinx.coroutines.GlobalScope.launch {
            collector.collect { }
        }
        return AutoCloseable { closed = true; jobHandle.cancel() }
    }
}