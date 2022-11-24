package com.example.rubikanschallenge.utils

import com.example.rubikanschallenge.model.Users

data class UsersStats (
    val isLoading: Boolean = false,
    val users: List<Users> = emptyList(),
    val error: String = ""
)

data class UserStats (
    val isLoading: Boolean = false,
    val user: Users? = null,
    val error: String = ""
)

data class UpdateStats (
    val isLoading: Boolean = false,
    val updatedAt: String? = null,
    val error: String = ""
)