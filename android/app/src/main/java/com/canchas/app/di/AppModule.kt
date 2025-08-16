package com.canchas.app.di

import android.app.Application
import com.canchas.app.data.repository.*
import com.canchas.app.data.repository.inmemory.InMemoryReservationRepository
import com.canchas.app.data.repository.inmemory.InMemoryVenueRepository
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // En producción proveeremos Firestore real. Mientras tanto, repos in-memory para demo sin llaves.

    @Provides
    @Singleton
    fun provideVenueRepository(): VenueRepository = InMemoryVenueRepository()

    @Provides
    @Singleton
    fun provideReservationRepository(): ReservationRepository = InMemoryReservationRepository()

    // Referencia para futura integración:
    // @Provides @Singleton fun provideFirestore(app: Application): FirebaseFirestore { ... }
}