package br.com.app.src.main.kotlin.com.habitus.data.repository

import br.com.app.src.main.kotlin.com.habitus.data.entity.UserEntity
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val currentUser: StateFlow<UserEntity?>
    suspend fun insertUser(user: UserEntity)

    suspend fun getUserById(userId: String): UserEntity?

    suspend fun updateUser(user: UserEntity)

    suspend fun deleteUser(user: UserEntity)
}