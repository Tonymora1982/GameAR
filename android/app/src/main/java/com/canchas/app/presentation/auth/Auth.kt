package com.canchas.app.presentation.auth

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(): ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    data class AuthUiState(
        val loading: Boolean = false,
        val isAuthenticated: Boolean = auth.currentUser != null,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _events = Channel<String>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun signInWithGoogle(context: Context, webClientId: String) {
        viewModelScope.launch {
            // Mock implementation: Simulate a successful login immediately
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            kotlinx.coroutines.delay(1000) // Simulate a network delay
            _uiState.value = _uiState.value.copy(loading = false, isAuthenticated = true)
        }
    }

    fun signOut() {
        auth.signOut()
        _uiState.value = _uiState.value.copy(isAuthenticated = false)
    }
}

// UI
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.canchas.app.R

@Composable
fun AuthScreen(viewModel: AuthViewModel = androidx.hilt.navigation.compose.hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val webClientId = stringResource(id = R.string.default_web_client_id)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Reserva tus canchas", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))
            if (state.loading) {
                CircularProgressIndicator()
            } else {
                Button(onClick = { viewModel.signInWithGoogle(context, webClientId) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Continuar con Google")
                }
                Spacer(Modifier.height(8.dp))
                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        }
    }
}