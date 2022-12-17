package com.poisonedyouth.api

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.Test

class UserServiceTest {

    @Test
    fun `add user fails if input is invalid`() {
        // given
        val user = UserDto(
            name = "John",
            age = 20,
            city = "Berlin"
        )

        // when
        val actual = UserService.add(user)

        // then
        actual shouldBe ApiResult.Failure(ErrorCode.INVALID_INPUT, "Name must be longer than 8 characters.")
    }

    @Test
    fun `add user fails if user already exists`() {
        // given
        val user = UserDto(
            name = "PoisonedYouth",
            age = 20,
            city = "Berlin"
        )
        mockkObject(UserRepository)
        every {
            UserRepository.save(any())
        } throws IllegalArgumentException("Failed")


        // when
        val actual = UserService.add(user)

        // then
        actual shouldBe ApiResult.Failure(ErrorCode.INVALID_INPUT, "Failed")

        unmockkObject(UserRepository)
    }

    @Test
    fun `add successful`() {
        // given
        val user = UserDto(
            name = "PoisonedYouth",
            age = 20,
            city = "Berlin"
        )
        mockkObject(UserRepository)
        every {
            UserRepository.save(any())
        } returns 1


        // when
        val actual = UserService.add(user)

        // then
        actual shouldBe ApiResult.Success(
            UserDto(
                id = 1,
                name = user.name,
                age = user.age,
                city = user.city
            )
        )

        unmockkObject(UserRepository)
    }

    @Test
    fun `findBy fails if input is null`(){
        // given
        val name = null

        // when
        val actual = UserService.findBy(name)

        // then
        actual shouldBe ApiResult.Failure(ErrorCode.USER_NOT_FOUND, "User with name 'null' does not exist.")
    }

    @Test
    fun `findBy fails if user does not exist`(){
        // given
        val name = "not exist"
        mockkObject(UserRepository)
        every {
            UserRepository.findBy(name)
        } returns null

        // when
        val actual = UserService.findBy(name)

        // then
        actual shouldBe ApiResult.Failure(ErrorCode.USER_NOT_FOUND, "User with name 'not exist' does not exist.")

        unmockkObject(UserRepository)
    }

    @Test
    fun `findBy returns matching user`(){
        // given
        val name = "PoisonedYouth"
        mockkObject(UserRepository)
        every {
            UserRepository.findBy(name)
        } returns User(
            id = 1,
            name = name,
            age = 12,
            city = "Berlin"
        )

        // when
        val actual = UserService.findBy(name)

        // then
        actual shouldBe ApiResult.Success(
            UserDto(
            id = 1,
            name = name,
            age = 12,
            city = "Berlin"
        )
        )
        unmockkObject(UserRepository)
    }
}
