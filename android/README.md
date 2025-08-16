# Canchas Android (Nativo)

MVP nativo con:
- Login con Google (Firebase Auth) [se activará cuando agregues credenciales]
- Firestore (usuarios, canchas, reservaciones) [pendiente credenciales]
- Google Maps (Compose) [pendiente API Key]

## Requisitos de configuración (cuando se integren servicios)
1. Archivo `app/google-services.json` desde Firebase Console (Android App agregada con package `com.canchas.app`).
2. SHA-1 y SHA-256 del keystore (agregar en Firebase para Google Sign-In).
3. API Key de Google Maps habilitada para Android (Maps SDK) y opcionalmente Places. Agregar en `local.properties`:

```
MAPS_API_KEY=TU_API_KEY
```

## Cómo correr el proyecto (sin servicios externos aún)
- Abre el proyecto `/app/android` en Android Studio Iguana o posterior.
- Sincroniza Gradle.
- Puedes ejecutar la app; verás la pantalla de Login y el Mapa. Sin credenciales, el botón de Google no autenticará, y el mapa requerirá una API Key válida para mostrar tiles.

## Documentación adicional
- docs/ARCHITECTURE.md
- docs/DATA_MODEL.md
- docs/FIREBASE_AND_MAPS_SETUP.md
- docs/SECURITY_RULES.md
- docs/ROADMAP.md