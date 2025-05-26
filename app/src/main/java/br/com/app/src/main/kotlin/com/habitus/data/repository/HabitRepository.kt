package br.com.app.src.main.kotlin.com.habitus.data.repository

import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity

interface HabitRepository {
    suspend fun insertHabit(habit: HabitEntity)

    suspend fun getAllHabits(): List<HabitEntity>

    suspend fun deleteHabit(habitId: String)

    suspend fun updateHabit(habit: HabitEntity)
}