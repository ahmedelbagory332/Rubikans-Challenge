package com.example.rubikanschallenge.data.local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rubikanschallenge.model.Users
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<Users>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<Users>)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}