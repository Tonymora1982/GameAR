package com.canchas.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.canchas.app.presentation.auth.AuthScreen
import com.canchas.app.presentation.auth.AuthViewModel
import com.canchas.app.presentation.map.VenueMapScreen
import com.canchas.app.ui.theme.CanchasTheme

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CanchasTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppEntry()
                }
            }
        }
    }
}

@Composable
fun AppEntry(authViewModel: AuthViewModel = hiltViewModel()) {
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isAuthenticated) {
        VenueMapScreen()
    } else {
        AuthScreen()
    }
}