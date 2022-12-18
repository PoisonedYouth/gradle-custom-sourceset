package com.poisonedyouth.api

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class UserRepositoryTest {

    @BeforeEach
    fun setupDatabase() {
        val database = Database.connect("jdbc:h2:mem:${UUID.randomUUID()};DB_CLOSE_DELAY=-1")
        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(UserTable)
        }
    }

    @Test
    fun `save new user is possible`() {
        // given
        val user = User(
            name = "poisonedyouth",
            age = 12,
            city = "LA"
        )

        // when
        val actual = UserRepository.save(user)

        // then
        actual shouldBe 1L
    }

    @Test
    fun `save new user fails if unique index violated`() {
        // given
        val user = User(
            name = "poisonedyouth",
            age = 12,
            city = "LA"
        )
        UserRepository.save(user)

        // when + then
        shouldThrowAny {
            UserRepository.save(
                user.copy(
                    id = null,
                    age = 45,
                    city = "Berlin"
                )
            )
        }
    }

    @Test
    fun `findBy returns matching user`() {
        // given
        val user = User(
            name = "poisonedyouth",
            age = 12,
            city = "LA"
        )
        UserRepository.save(user)

        // when
        val actual = UserRepository.findBy("poisonedyouth")

        // then
        actual shouldBe user.copy(
            id = 1
        )
    }

    @Test
    fun `findBy returns null for not matching user`() {
        // given
        val user = User(
            name = "poisonedyouth",
            age = 12,
            city = "LA"
        )
        UserRepository.save(user)

        // when
        val actual = UserRepository.findBy("xxx")

        // then
        actual shouldBe null
    }
}
