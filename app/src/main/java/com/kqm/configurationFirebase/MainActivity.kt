package com.kqm.configurationFirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kqm.configurationFirebase.auth.AuthManager.auth
import com.kqm.configurationFirebase.auth.AuthManager.getUserName
import com.kqm.configurationFirebase.auth.AuthManager.loginUser
import com.kqm.configurationFirebase.auth.AuthManager.signOut
import com.kqm.configurationFirebase.navigation.LoginRoute
import com.kqm.configurationFirebase.navigation.NavigationState
import com.kqm.configurationFirebase.navigation.NavigationWrapper
import com.kqm.configurationFirebase.navigation.ProfileRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationWrapper(auth)
        }
    }

    override fun onStart() {
        super.onStart()
        if (!loginUser()) {
            NavigationState.targetRoute = LoginRoute
        }else {
            val user = getUserName() ?: ""
            NavigationState.targetRoute = ProfileRoute(user)
        }
    }

    override fun onStop() {
        super.onStop()
        signOut()
    }
}
