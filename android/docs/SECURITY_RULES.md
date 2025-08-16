# Reglas de Seguridad (Firestore - sugeridas)

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {

    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
      allow read: if request.auth != null && get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role == 'admin';
    }

    match /venues/{venueId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null && request.auth.uid == request.resource.data.ownerId && get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role in ['owner','admin'];
      allow update: if request.auth != null && (request.auth.uid == resource.data.ownerId || get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role == 'admin');
      allow delete: if request.auth != null && get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role == 'admin';
    }

    match /reservations/{reservationId} {
      allow read: if request.auth != null && (
        request.auth.uid == resource.data.userId ||
        request.auth.uid == get(/databases/$(database)/documents/venues/$(resource.data.venueId)).data.ownerId ||
        get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role == 'admin');

      allow create: if request.auth != null && request.auth.uid == request.resource.data.userId && request.resource.data.status == 'pending' && request.resource.data.startTime > request.time;

      allow update: if request.auth != null && (
        // usuario modifica notas mientras esté pending
        (request.auth.uid == resource.data.userId && resource.data.status == 'pending' && request.resource.data.keys().hasOnly(['notes','updatedAt'])) ||
        // owner cambia estado
        (request.auth.uid == get(/databases/$(database)/documents/venues/$(resource.data.venueId)).data.ownerId && request.resource.data.keys().hasOnly(['status','updatedAt']) && request.resource.data.status in ['confirmed','cancelled']) ||
        // admin
        get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role == 'admin'
      );

      allow delete: if request.auth != null && request.auth.uid == resource.data.userId && resource.data.status in ['pending','confirmed'];
    }
  }
}
```