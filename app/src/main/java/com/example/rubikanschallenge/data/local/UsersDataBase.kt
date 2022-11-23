package com.example.rubikanschallenge.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rubikanschallenge.model.Users


@Database(entities = [Users::class], version = 1)
abstract class UsersDataBase : RoomDatabase() {

    abstract fun usersDao(): UsersDao
}