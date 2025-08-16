package com.canchas.app.di

import android.app.Application
import com.canchas.app.data.repository.FirestoreReservationRepository
import com.canchas.app.data.repository.FirestoreVenueRepository
import com.canchas.app.data.repository.ReservationRepository
import com.canchas.app.data.repository.VenueRepository
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

    @Provides
    @Singleton
    fun provideFirestore(app: Application): FirebaseFirestore {
        FirebaseApp.initializeApp(app)
        return FirebaseFirestore.getInstance().apply {
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideVenueRepository(db: FirebaseFirestore): VenueRepository = FirestoreVenueRepository(db)

    @Provides
    @Singleton
    fun provideReservationRepository(db: FirebaseFirestore): ReservationRepository = FirestoreReservationRepository(db)
}