# Modelo de Datos (Firestore)

Colecciones principales:

## users
- uid: string (doc id)
- email: string
- displayName: string
- role: string ("admin" | "owner" | "user")
- isActive: boolean
- createdAt: timestamp
- lastSignIn: timestamp

## venues
- id: string (doc id)
- name: string
- address: string
- location: GeoPoint (lat, lng)
- ownerId: string (uid del dueño)
- capacity: number
- pricePerHour: number
- isActive: boolean
- createdAt: timestamp
- updatedAt: timestamp

Índices sugeridos:
- venues: isActive asc, createdAt desc (consulta de listado)

## reservations
- id: string (doc id)
- venueId: string
- userId: string
- userName: string
- userEmail: string
- startTime: timestamp
- endTime: timestamp
- status: string ("pending" | "confirmed" | "cancelled" | "completed")
- totalPrice: number
- notes: string
- createdAt: timestamp
- updatedAt: timestamp

Índices sugeridos:
- reservations: venueId asc, status asc, startTime asc
- reservations: userId asc, startTime desc

## Estados/Reglas
- Los Owners administran sus venues y reservas vinculadas
- Admin puede leer todo
- Usuarios leen/escriben sus propias reservas