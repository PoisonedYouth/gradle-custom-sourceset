package com.poisonedyouth.api

import org.slf4j.LoggerFactory

object UserService {

    private val logger = LoggerFactory.getLogger(UserService::class.java)

    fun add(userDto: UserDto): ApiResult<UserDto, ErrorCode> {
        return try {
            logger.info("Start adding user '$userDto'...")
            val user = User(
                name = userDto.name,
                age = userDto.age,
                city = userDto.city
            )
            val id = UserRepository.save(user)
            ApiResult.Success(
                userDto.copy(
                    id = id
                )
            )
        } catch (e: IllegalArgumentException) {
            logger.error("Failed to add user '$userDto'.", e)
            ApiResult.Failure(
                errorCode = ErrorCode.INVALID_INPUT,
                message = e.message ?: "Unkown error"
            )
        }
    }

    @SuppressWarnings("TooGenericExceptionCaught") // Its intended to catch exceptions in this layer
    fun findBy(name: String?): ApiResult<UserDto, ErrorCode> {
        return try {
            logger.info("Start finding user with name '$name'...")
            val user = name?.let { UserRepository.findBy(it) }
            if (user == null) {
                logger.error("User with name '$name' does not exist.")
                return ApiResult.Failure(
                    errorCode = ErrorCode.USER_NOT_FOUND,
                    message = "User with name '$name' does not exist."
                )
            }
            ApiResult.Success(
                UserDto(
                    id = user.id,
                    name = user.name,
                    age = user.age,
                    city = user.city
                )
            )
        } catch (e: RuntimeException) {
            logger.error("Failed to find user with name '$name'.", e)
            ApiResult.Failure(
                errorCode = ErrorCode.GENERAL_ERROR,
                message = e.message ?: "Unkown error"
            )
        }
    }
}

enum class ErrorCode {
    INVALID_INPUT,
    USER_NOT_FOUND,
    GENERAL_ERROR
}

sealed interface ApiResult<out T, out U> {
    data class Success<T>(val value: T) : ApiResult<T, Nothing>
    data class Failure<U>(val errorCode: U, val message: String) : ApiResult<Nothing, U>
}
