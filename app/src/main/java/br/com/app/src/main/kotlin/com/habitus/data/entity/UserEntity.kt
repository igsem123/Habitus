package br.com.app.src.main.kotlin.com.habitus.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = false) val uid: String,
    val username: String = "",
    val email: String = "",
    val password: String = "",
)