package com.kqm.configurationFirebase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import com.kqm.configurationFirebase.ui.screens.LoginScreen
import com.kqm.configurationFirebase.ui.screens.ProfileScreen
import com.kqm.configurationFirebase.ui.screens.SignScreen

@Composable
fun NavigationWrapper(auth: FirebaseAuth) {

    val navController = rememberNavController()

    LaunchedEffect(NavigationState.targetRoute) {
        NavigationState.targetRoute?.let {
            navController.navigate(it)
            NavigationState.targetRoute = null
        }
    }

    NavHost(navController = navController, startDestination = LoginRoute) {

        composable<LoginRoute> {
            LoginScreen(
                auth = auth,
                onNavigateToSignUp = { navController.navigate(SignUpRoute) },
                onNavigateToProfile = { name -> navController.navigate(ProfileRoute(name)) }
            )
        }

        composable<SignUpRoute> {
            SignScreen(
                auth = auth,
                onNavigateLogin = { navController.navigate(LoginRoute) }
            )
        }

        composable<ProfileRoute> { backStackEntry ->
            val userName = backStackEntry.toRoute<ProfileRoute>()
            ProfileScreen(userName.userName)
        }
    }
}