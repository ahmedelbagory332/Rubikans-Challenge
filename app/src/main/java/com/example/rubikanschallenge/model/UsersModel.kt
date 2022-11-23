package com.example.rubikanschallenge.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class UsersModel(

    @SerializedName("data")
    val users: List<Users>,
 )

data class UserModel(

    @SerializedName("data")
    val user: Users,
)

@Entity(tableName = "users")
data class Users(
    @PrimaryKey val id: Long,
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val avatar: String,
)

