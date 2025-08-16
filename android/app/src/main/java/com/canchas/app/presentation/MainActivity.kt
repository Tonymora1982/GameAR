package com.canchas.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.canchas.app.presentation.auth.AuthViewModel
import com.canchas.app.presentation.navigation.AppNavGraph
import com.canchas.app.ui.theme.CanchasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
    val uiState = authViewModel.uiState.collectAsStateWithLifecycle().value
    AppNavGraph(isAuthenticated = uiState.isAuthenticated)
}