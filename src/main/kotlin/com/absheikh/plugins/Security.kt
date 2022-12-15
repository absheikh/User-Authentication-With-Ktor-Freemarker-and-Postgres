package com.absheikh.plugins

import com.absheikh.core.UserSession
import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.sessions.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureSecurity() {
    install(Sessions) {
        val secretSignKey = hex("6819b57a326945c1968f45236589")
        cookie<UserSession>("AuthSession", directorySessionStorage(File("build/.sessions"))){
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60 * 60 * 5
            transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
        }


    }
    install(Authentication){
        session<UserSession>("AuthSession"){
            challenge {
                call.respondRedirect("/login")
            }
            validate { session ->
                session
            }
        }


    }

}
