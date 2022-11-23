package com.example.rubikanschallenge.data.repository

import androidx.room.withTransaction
import com.example.rubikanschallenge.data.local.UsersDataBase
import com.example.rubikanschallenge.utils.Resource
import com.example.rubikanschallenge.data.network.UserApi
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

         }finally {
             emit(Resource.Error<List<Users>>("An unexpected error happened"))

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



}