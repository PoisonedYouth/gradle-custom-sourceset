package com.poisonedyouth.api

import com.poisonedyouth.module
import io.kotest.matchers.shouldBe
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class UserEndpointTest {

    @BeforeEach
    fun setupDatabase() {
        val database = Database.connect("jdbc:h2:mem:${UUID.randomUUID()};DB_CLOSE_DELAY=-1")
        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(UserTable)
        }
    }

    @Test
    fun `get returns matching user`() = testApplication {
        application {
            module()
        }
        UserRepository.save(
            User(
                name = "PoisonedYouth",
                age = 12,
                city = "Berlin"
            )
        )

        val response = client.get("/api/user") {
            parameter("name", "PoisonedYouth")
            accept(ContentType.Application.Json)

        }
        response.status shouldBe HttpStatusCode.OK
        response.bodyAsText() shouldBe "{\"id\":1,\"name\":\"PoisonedYouth\",\"age\":12,\"city\":\"Berlin\"}"
    }

    @Test
    fun `get returns failure if user does not exist`() = testApplication {
        application {
            module()
        }
        UserRepository.save(
            User(
                name = "PoisonedYouth",
                age = 12,
                city = "Berlin"
            )
        )

        val response = client.get("/api/user") {
            parameter("name", "Not Existing")
            accept(ContentType.Application.Json)

        }
        response.status shouldBe HttpStatusCode.BadRequest
        response.bodyAsText() shouldBe "User with name 'Not Existing' does not exist."
    }

    @Test
    fun `post returns failure if input is not valid`() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                jackson()
            }
        }

        val response = client.post("/api/user") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            setBody(
                UserDto(
                    name = "John",
                    age = 12,
                    city = "Berlin"
                )
            )
        }
        response.status shouldBe HttpStatusCode.BadRequest
        response.bodyAsText() shouldBe "Name must be longer than 8 characters."
    }

    @Test
    fun `post persists user`() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                jackson()
            }
        }

        val response = client.post("/api/user") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            setBody(
                UserDto(
                    name = "PoisonedYouth",
                    age = 12,
                    city = "Berlin"
                )
            )
        }
        response.status shouldBe HttpStatusCode.Created
        response.bodyAsText() shouldBe "{\"id\":1,\"name\":\"PoisonedYouth\",\"age\":12,\"city\":\"Berlin\"}"
    }
}
