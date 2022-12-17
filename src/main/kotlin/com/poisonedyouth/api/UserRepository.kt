package com.poisonedyouth.api

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object UserRepository {
    fun save(user: User): Long = transaction {
        UserTable.insertAndGetId {
            it[name] = user.name
            it[age] = user.age
            it[city] = user.city
        }.value
    }

    fun findBy(name: String): User? = transaction {
        UserTable.select { UserTable.name eq name }.firstOrNull()?.let {
            User(
                id = it[UserTable.id].value,
                name = it[UserTable.name],
                age = it[UserTable.age],
                city = it[UserTable.city]
            )
        }
    }
}

object UserTable : LongIdTable("user", "user_id") {
    val name = varchar("name", 255).uniqueIndex()
    val age = integer("age")
    val city = varchar("city", 255)
}
