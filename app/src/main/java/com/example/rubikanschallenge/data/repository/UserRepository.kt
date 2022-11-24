package com.example.rubikanschallenge.data.repository

import android.util.Log
import androidx.room.withTransaction
import com.example.rubikanschallenge.data.local.UsersDataBase
import com.example.rubikanschallenge.utils.Resource
import com.example.rubikanschallenge.data.network.UserApi
import com.example.rubikanschallenge.model.UpdateModel
import com.example.rubikanschallenge.model.UserModel
import com.example.rubikanschallenge.model.Users
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi,private val usersDataBase: UsersDataBase){


    fun getUsers(): Flow<Resource<List<Users>>> = flow {
         try {
           emit(Resource.Loading<List<Users>>())
             usersDataBase.withTransaction {
                 usersDataBase.usersDao().deleteAllUsers()
                 usersDataBase.usersDao().insertUsers(userApi.getUsers().users)
             }
             usersDataBase.usersDao().getAllUsers().collect{ users ->
                 emit(Resource.Success<List<Users>>(users))
             }
         }catch (e:Exception){
             usersDataBase.usersDao().getAllUsers().collect{ users ->
                 emit(Resource.Success<List<Users>>(users))
             }

         }
    }

    fun getUser(id: Long): Flow<Resource<UserModel>> = flow {
        try {
            emit(Resource.Loading<UserModel>())
            emit(Resource.Success<UserModel>(userApi.getUser(id)))

        }catch (e:Exception){
            emit(Resource.Error<UserModel>("An unexpected error happened"))
        }
    }

    fun updateUser(id: Long , name:String): Flow<Resource<UpdateModel>> = flow {
        try {
            emit(Resource.Loading<UpdateModel>())
            emit(Resource.Success<UpdateModel>(userApi.updateUser(id,name)))

        }catch (e:Exception){
            Log.d("bego", e.localizedMessage!!)
            usersDataBase.usersDao().updateUser(id,name)
            emit(Resource.Error<UpdateModel>("Data Updated Locally due to server connection error"))
        }
    }



}