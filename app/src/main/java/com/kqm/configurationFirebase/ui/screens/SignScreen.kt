package com.kqm.configurationFirebase.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun SignScreen(auth: FirebaseAuth, onNavigateToLogin: () -> Unit) {

    var userName by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Username") },
            placeholder = { Text("Username") },
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Username") }
        )

        Spacer(modifier = Modifier.padding(16.dp))

        PasswordTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password"
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Button (
            onClick = {
                scope.launch {
                    try {
                        auth.createUserWithEmailAndPassword(userName, password).await()
                        onNavigateToLogin()
                    } catch (e: Exception) {
                        val message = when (e) {
                            is FirebaseAuthInvalidUserException -> "Dirección de correo electrónico inválida."
                            is FirebaseAuthInvalidCredentialsException -> "Contraseña incorrecta."
                            else -> "Error al iniciar sesión. Inténtalo de nuevo."
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            },
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        {
            Text(text = "Sign Up")
        }
    }
}
