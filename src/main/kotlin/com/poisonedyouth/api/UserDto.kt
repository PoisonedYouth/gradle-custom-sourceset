package com.poisonedyouth.api

data class UserDto(
    val id: Long? = null,
    val name: String,
    val age: Int,
    val city: String
)
