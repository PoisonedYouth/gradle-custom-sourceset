package com.poisonedyouth.api

private const val NAME_MINIMUM_LENGTH = 8

private const val AGE_LOWER_BOUND = 10

private const val AGE_UPPER_BOUND = 100

data class User(
    val id: Long? = null,
    val name: String,
    val age: Int,
    val city: String
) {
    init {
        require(name.length >= NAME_MINIMUM_LENGTH) {
            "Name must be longer than 8 characters."
        }
        require(age in AGE_LOWER_BOUND..AGE_UPPER_BOUND) {
            "Age must be between 10 and 100."
        }
        require(!city.contains(Regex("[0-9]")))
    }
}
