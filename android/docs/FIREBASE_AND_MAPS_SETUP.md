# Configuración Firebase y Google Maps

> Nota: Este MVP queda listo para integrar Firebase y Google; se activará cuando agregues credenciales.

## 1) Firebase
1. Crea un proyecto en Firebase Console
2. Agrega app Android con package: `com.canchas.app`
3. Descarga `google-services.json` y colócalo en: `android/app/google-services.json`
4. En Firebase Authentication, habilita Google como proveedor
5. Agrega huellas SHA-1 y SHA-256 (debug y release) en la app de Firebase (requerido para Google Sign-In)

## 2) Google Cloud / Mapas
1. Habilita Maps SDK for Android (y Places si lo requieres)
2. Crea una API Key restringida a Android App (package + SHA-1)
3. En `android/local.properties` agrega:
```
MAPS_API_KEY=TU_API_KEY
```

## 3) Sincroniza y prueba
- Abre `/app/android` en Android Studio
- Sincroniza Gradle y ejecuta en dispositivo

## Troubleshooting
- Falta `google-services.json`: Gradle fallará. Asegúrate de colocarlo en `app/`
- Error de credenciales Google: revisa SHA-1/256 y default_web_client_id en strings (se sobreescribe desde google-services.json)
- Mapa en blanco: valida que la key tenga Maps SDK activado y restricciones correctas