# Notas de Desarrollo

## Ejecución sin servicios externos
- Para facilitar pruebas, se incluyeron repositorios InMemory (venues/reservations) que permiten:
  - Renderizar markers de ejemplo en el mapa.
  - Crear reservas en el calendario básico con validación de conflicto local.
- Cuando se agregue Firebase:
  - Cambiar bindings en AppModule a FirestoreVenueRepository/FirestoreReservationRepository y reactivar proveedor de FirebaseFirestore.

## Navegación
- AppNavGraph define rutas: auth, map, calendar/{venueId}.
- MainActivity decide startDestination basado en `isAuthenticated`.

## Calendario (MVP)
- Día actual por defecto; navegación +/- día.
- Selección de rango horario por taps consecutivos.
- Prevención de conflictos en memoria; al migrar a Firestore, se usará verificación transaccional.

## Próximos pasos técnicos
- Conectar Auth real y persistencia de usuario (users collection).
- Rules + Emulator Suite para probar seguridad.
- CRUD completo de venues (dueños) y dashboards.