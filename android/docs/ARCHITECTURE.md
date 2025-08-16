# Arquitectura del MVP

Este MVP sigue una arquitectura moderna de Android basada en:

- Lenguaje: Kotlin
- UI: Jetpack Compose + Material 3
- Arquitectura: MVVM + Single-Activity
- DI: Hilt
- Datos: Firebase Firestore (offline enabled) [se integrará cuando se agregue google-services.json]
- Auth: Google Sign-In vía Credential Manager + Firebase Auth [se activará posteriormente]
- Mapas: Google Maps SDK + Maps Compose [se activará al agregar API Key]

## Capas

- Presentación (Compose): pantallas y componentes (AuthScreen, VenueMapScreen)
- Lógica (ViewModels): AuthViewModel, MapViewModel
- Datos (Repos): VenueRepository, ReservationRepository (implementaciones Firestore)
- DI: AppModule provee Firestore y repos

## Flujo principal

1. MainActivity renderiza AppEntry
2. AuthViewModel expone estado `isAuthenticated`
3. Si autenticado → VenueMapScreen (mapa y canchas)
4. Si no autenticado → AuthScreen (botón "Continuar con Google")

## Decisiones técnicas

- Compose + Navigation: navegación simple condicional por auth state
- Hilt: simplifica inyección (Application anotada con @HiltAndroidApp)
- Firestore con persistencia offline: experiencia robusta con intermitencia de red
- Credential Manager + Google Identity: experiencia moderna de login
- Maps Compose: integración nativa de mapas en Compose