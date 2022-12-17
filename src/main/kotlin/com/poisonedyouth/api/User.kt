package com.poisonedyouth.api

data class User(
    val id: Long? = null,
    val name: String,
    val age: Int,
    val city: String
) {
    init {
        require(name.length > 8) {
            "Name must be longer than 8 characters."
        }
        require(age in 10..100) {
            "Age must be between 10 and 100."
        }
        require(!city.contains(Regex("[0-9]")))
    }
}
