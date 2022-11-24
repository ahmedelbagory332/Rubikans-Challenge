package com.example.rubikanschallenge.data.network

import com.example.rubikanschallenge.model.UpdateModel
import com.example.rubikanschallenge.model.UserModel
import com.example.rubikanschallenge.model.UsersModel
import com.example.rubikanschallenge.model.Users
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {

    companion object {
        const val BASE_URL = "https://reqres.in/api/"
    }

    @GET("users?page=1")
    suspend fun getUsers(): UsersModel

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Long): UserModel

    @FormUrlEncoded
    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Long , @Field("name") name:String): UpdateModel


}