package br.com.app.src.main.kotlin.com.habitus.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity

@Dao
interface HabitDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Query("SELECT * FROM habits")
    suspend fun getAllHabits(): List<HabitEntity>

    @Query("DELETE FROM habits WHERE id = :habitId")
    suspend fun deleteHabit(habitId: String)

    @Query("UPDATE habits SET title = :title, description = :description, isCompleted = :isCompleted, category = :category, pontuation = :pontuation, days = :days WHERE id = :habitId")
    suspend fun updateHabit(
        habitId: Long,
        title: String,
        description: String,
        isCompleted: Boolean,
        category: String,
        pontuation: Int,
        days: String
    )
}