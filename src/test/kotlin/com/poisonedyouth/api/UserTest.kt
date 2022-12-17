package com.poisonedyouth.api

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test

class UserTest {

    @Test
    fun `creation fails if name is too short`() {
        // given
        val name = "John"
        val age = 20
        val city = "Berlin"

        // when + then
        shouldThrow<IllegalArgumentException> {
            User(
                name = name,
                age = age,
                city = city
            )
        }
    }

    @Test
    fun `creation fails if age is too small`() {
        // given
        val name = "PoisonedYouth"
        val age = 5
        val city = "Berlin"

        //  when + then
        shouldThrow<IllegalArgumentException> {
            User(
                name = name,
                age = age,
                city = city
            )
        }
    }

    @Test
    fun `creation fails if age is too high`() {
        // given
        val name = "PoisonedYouth"
        val age = 200
        val city = "Berlin"

        // when + then
        shouldThrow<IllegalArgumentException> {
            User(
                name = name,
                age = age,
                city = city
            )
        }
    }

    @Test
    fun `creation fails if city contains invalid chars small`() {
        // given
        val name = "PoisonedYouth"
        val age = 20
        val city = "Berlin5"

        // when + then
        shouldThrow<IllegalArgumentException> {
            User(
                name = name,
                age = age,
                city = city
            )
        }
    }

    @Test
    fun `creation successful`() {
        // given
        val name = "PoisonedYouth"
        val age = 20
        val city = "Berlin"

        // when + then
        shouldNotThrow<IllegalArgumentException> {
            User(
                name = name,
                age = age,
                city = city
            )
        }
    }
}
