package br.com.app.src.main.kotlin.com.habitus.data.entity

import androidx.room.Entity

@Entity(tableName = "users")
data class UserEntity(
    val uid: String,
    val username: String = "",
    val email: String = "",
    val password: String = "",
)