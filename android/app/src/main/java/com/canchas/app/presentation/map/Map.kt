package com.canchas.app.presentation.map

import android.Manifest
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.maps.android.compose.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VenueMapScreen() {
    val permissions = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    var hasPermission by remember { mutableStateOf(false) }

    LaunchedEffect(permissions.allPermissionsGranted) {
        if (permissions.allPermissionsGranted) hasPermission = true
    }

    Box(Modifier.fillMaxSize()) {
        if (!hasPermission) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Necesitamos tu ubicación para mostrar canchas cercanas",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                Button(onClick = { permissions.launchMultiplePermissionRequest() }) {
                    Text("Conceder permisos")
                }
            }
        } else {
            val cameraPositionState = rememberCameraPositionState()
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true),
                uiSettings = MapUiSettings(myLocationButtonEnabled = true)
            ) {
                // TODO: Load and plot venues from Firestore
            }
        }
    }
}