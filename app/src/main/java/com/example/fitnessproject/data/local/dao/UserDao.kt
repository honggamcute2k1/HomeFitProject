package com.example.fitnessproject.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fitnessproject.data.local.entity.User

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Insert
    fun insertUserList(users: MutableList<User>)

    @Query("SELECT * FROM user")
    fun getAllUser(): List<User>

    @Query("SELECT * FROM user")
    fun getAllUserTest(): LiveData<List<User>>
}