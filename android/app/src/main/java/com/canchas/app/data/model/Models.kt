package com.canchas.app.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

// Usuarios

data class AppUser(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val role: UserRole = UserRole.USER,
    val createdAt: Timestamp? = null,
    val lastSignIn: Timestamp? = null,
    val isActive: Boolean = true
)

enum class UserRole(val value: String) { ADMIN("admin"), OWNER("owner"), USER("user"); }

// Cancha / Venue

data class Venue(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val ownerId: String = "",
    val capacity: Int = 0,
    val pricePerHour: Double = 0.0,
    val isActive: Boolean = true,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)

// Reservación

enum class ReservationStatus(val value: String) { PENDING("pending"), CONFIRMED("confirmed"), CANCELLED("cancelled"), COMPLETED("completed"); }

data class Reservation(
    val id: String = "",
    val venueId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val startTime: Timestamp = Timestamp.now(),
    val endTime: Timestamp = Timestamp.now(),
    val status: ReservationStatus = ReservationStatus.PENDING,
    val totalPrice: Double = 0.0,
    val notes: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)