package com.example.rubikanschallenge.data.network

import com.example.rubikanschallenge.model.UserModel
import com.example.rubikanschallenge.model.UsersModel
import com.example.rubikanschallenge.model.Users
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {

    companion object {
        const val BASE_URL = "https://reqres.in/api/"
    }

    @GET("users?page=1")
    suspend fun getUsers(): UsersModel

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Long): UserModel


}