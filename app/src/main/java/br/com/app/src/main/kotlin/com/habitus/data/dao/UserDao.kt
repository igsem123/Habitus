package br.com.app.src.main.kotlin.com.habitus.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.app.src.main.kotlin.com.habitus.data.entity.UserEntity

@Dao
interface UserDao {
     @Insert
     suspend fun insertUser(user: UserEntity)

     @Query("SELECT * FROM users WHERE uid = :userId")
     suspend fun getUserById(userId: String): UserEntity?

     @Update
     suspend fun updateUser(user: UserEntity)

     @Delete
     suspend fun deleteUser(user: UserEntity)
}