package com.kqm.configurationFirebase.navigation

import kotlinx.serialization.Serializable

@Serializable
object SignUpRoute

@Serializable
object LoginRoute

@Serializable
data class ProfileRoute(val userName: String)