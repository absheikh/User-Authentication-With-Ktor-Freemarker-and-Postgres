package com.absheikh.models

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*


interface User : Entity<User> {
    var id: String
    var username: String
    var password: String

}

object Users : Table<User>("users") {
    val id = text("id").primaryKey().bindTo { it.id }
    val username = text("username").bindTo { it.username }
    val password = text("password").bindTo { it.password }

}
val Database.users get() = this.sequenceOf(Users)