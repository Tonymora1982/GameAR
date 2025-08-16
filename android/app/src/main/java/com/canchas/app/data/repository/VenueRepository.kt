package com.canchas.app.data.repository

import com.canchas.app.data.model.Venue
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface VenueRepository {
    fun getAllActiveVenues(): Flow<List<Venue>>
    suspend fun createVenue(venue: Venue): Result<String>
    suspend fun getVenue(id: String): Result<Venue>
}

@Singleton
class FirestoreVenueRepository @Inject constructor(
    private val db: FirebaseFirestore
) : VenueRepository {

    private val col get() = db.collection("venues")

    override fun getAllActiveVenues(): Flow<List<Venue>> = callbackFlow {
        val reg = col.whereEqualTo("isActive", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val list = snap?.documents?.mapNotNull { doc ->
                    val v = doc.toObject(Venue::class.java)
                    v?.copy(id = doc.id)
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { reg.remove() }
    }

    override suspend fun createVenue(venue: Venue): Result<String> = runCatching {
        val ref = col.document()
        val payload = venue.copy(id = ref.id, createdAt = Timestamp.now(), updatedAt = Timestamp.now())
        ref.set(payload).await()
        ref.id
    }

    override suspend fun getVenue(id: String): Result<Venue> = runCatching {
        val doc = col.document(id).get().await()
        val v = doc.toObject(Venue::class.java) ?: throw IllegalStateException("Venue not found")
        v.copy(id = doc.id)
    }
}