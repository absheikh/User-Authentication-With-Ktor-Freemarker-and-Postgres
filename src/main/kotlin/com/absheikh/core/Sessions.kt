package com.absheikh.core

import io.ktor.server.auth.*

data class UserSession(val username: String): Principal

data class Error(val message: String)