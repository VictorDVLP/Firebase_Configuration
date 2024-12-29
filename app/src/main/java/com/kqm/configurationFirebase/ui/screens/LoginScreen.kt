package com.kqm.configurationFirebase.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.kqm.configurationFirebase.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(
    auth: FirebaseAuth,
    modifier: Modifier = Modifier,
    onNavigateToSignUp: () -> Unit,
    onNavigateToProfile: (String) -> Unit
) {

    var userName by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Username") },
            placeholder = { Text("Username") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Username"
                )
            }
        )

        Spacer(modifier = Modifier.padding(4.dp))

        PasswordTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password"
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        auth.signInWithEmailAndPassword(userName, password).await()
                        onNavigateToProfile(userName)
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
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
            )
        )
        {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Don't have an account?")
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "Sign Up",
                fontWeight = FontWeight.Bold,
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onNavigateToSignUp() })
        }

    }
}


@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Contraseña",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    supportingText: (@Composable () -> Unit)? = null
) {

    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(text = label) },
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        supportingText = supportingText,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                painterResource(R.drawable.visibility)
            else painterResource(R.drawable.password)
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = image, description)
            }
        }
    )
}
