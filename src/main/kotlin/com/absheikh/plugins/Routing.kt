package com.absheikh.plugins

import com.absheikh.core.UserSession
import com.absheikh.core.Error
import com.absheikh.core.dao.db
import com.absheikh.models.User
import com.absheikh.models.Users
import com.absheikh.models.users
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.sessions.*
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.entity.add
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.firstOrNull
import org.mindrot.jbcrypt.BCrypt

fun Application.configureRouting() {


    routing {

        get("/register"){
            val user = call.sessions.get<UserSession>()
            if(user != null){
                call.respondRedirect("/")
            }else {
                call.respond(FreeMarkerContent("register.ftl", null))
            }

        }

        post("/register"){


            val params = call.receiveParameters()
            val username = params["username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val password = params["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)

            if(username.isBlank() || password.isBlank()){
                call.respond(FreeMarkerContent("register.ftl", mapOf("error" to Error ("Username and password cannot be blank"))))
                return@post
            }
            //check if username exist
            val user = db.users
                .filter { it.username eq username!! }
                .firstOrNull()
            if(user != null){
                call.respond(FreeMarkerContent("register.ftl", mapOf("error" to  Error("Username already exist"))))
                return@post
            }
            //hash password
            val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

           val row = db.insert(Users) {
               set(it.username, username)
               set(it.password, hashedPassword)
           }
            if(row > 0){
                //add user to session
               call.sessions.set(UserSession(username))
                call.respondRedirect("/")

        }
        }


//        Login page
        get("/login") {
            //Check if user is already logged in
            val user = call.sessions.get<UserSession>()
            if(user != null){
                call.respondRedirect("/")
            }else {
                call.respond(FreeMarkerContent("login.ftl", null))

            }
        }
        post("/login") {
            val post = call.receiveParameters()
            val username = post["username"]
            val password = post["password"]

            //Search for user in database
            val user = db.users
                .filter { it.username eq username!! }
                .firstOrNull()

            if (user != null) {
                if (BCrypt.checkpw(password, user.password)) {
                    call.sessions.set(UserSession(user.username))
                    call.respondRedirect("/")
                } else {
                    call.respond(FreeMarkerContent("login.ftl", mapOf("error" to Error("Invalid username or password"))))
                }
            } else {
                call.respond(FreeMarkerContent("login.ftl", mapOf("error" to Error("Invalid username or password"))))
            }


        }

        // Logout
        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/login")
        }


        authenticate("AuthSession") { // <-- this line
            get("/") {
                val user = call.principal<UserSession>()

                call.respond(FreeMarkerContent("index.ftl", mapOf("user" to user)))
            }
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
