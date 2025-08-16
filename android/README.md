# Canchas Android (Nativo)

MVP nativo con:
- Login con Google (Firebase Auth)
- Firestore (usuarios, canchas, reservaciones)
- Google Maps (Compose)

## Requisitos de configuración (proveer antes de compilar)
1. Archivo `app/google-services.json` desde Firebase Console (Android App agregada con package `com.canchas.app`).
2. SHA-1 y SHA-256 del keystore (agregar en Firebase para Google Sign-In).
3. API Key de Google Maps habilitada para Android (Maps SDK) y opcionalmente Places. Agregar en `local.properties`:

```
MAPS_API_KEY=TU_API_KEY
```

## Primer arranque (local)
- Abrir el proyecto `/app/android` en Android Studio Iguana o más reciente.
- Sincronizar Gradle.
- Colocar `google-services.json` en `app/`.
- Añadir `MAPS_API_KEY` en `local.properties`.
- Ejecutar en dispositivo/emulador Android 8.0+.

## Roadmap próximo
- Repositorios Firestore (venues, reservations) con reglas.
- Pantalla calendario de reservas con prevención de conflictos.
- Dashboards: Admin, Dueño, Usuario.