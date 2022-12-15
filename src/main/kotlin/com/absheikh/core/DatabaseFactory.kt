package com.absheikh.core.dao

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database

val config: Config = ConfigFactory.load()
val dbName: String = config.getString("app.database.database")
val dbUser: String = config.getString("app.database.user")
val dbPassword: String = config.getString("app.database.password")
val dbHost: String = config.getString("app.database.hostname")
val dbPort: String = config.getString("app.database.port")
val pgSchema: String = config.getString("app.database.schema")
val dbDriver: String = config.getString("app.database.driverClassName")
val dbMaxPoolSize: Int = config.getInt("app.database.maxPoolSize")

val db = Database.connect(HikariDataSource(HikariConfig().apply {
    driverClassName = dbDriver
    jdbcUrl = "jdbc:postgresql://${dbHost}:${dbPort}/${dbName}?currentSchema=$pgSchema"
    username = dbUser
    password = dbPassword
    maximumPoolSize = dbMaxPoolSize
}))