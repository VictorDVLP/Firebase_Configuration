package com.kqm.configurationFirebase.auth

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object AuthManager {
    val auth = Firebase.auth

    fun signOut() {
        auth.signOut()
    }

    private fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun loginUser(): Boolean = getCurrentUser() != null

    fun getUserName(): String? = getCurrentUser()?.displayName

}