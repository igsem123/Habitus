package br.com.app.src.main.kotlin.com.habitus.data.repository

import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitLogEntity

interface HabitRepository {
    suspend fun insertHabit(habit: HabitEntity)

    suspend fun getAllHabits(): List<HabitEntity>

    suspend fun getCompletedHabits(): Int

    suspend fun deleteHabit(habitId: Long)

    suspend fun updateHabit(habit: HabitEntity)

    suspend fun insertLog(log: HabitLogEntity)

    suspend fun filterHabitsByPeriod(inicioPeriodo: Long, fimPeriodo: Long): List<HabitLogEntity>

    suspend fun getHabitsCount() : Int

    suspend fun getHabitsReport(): String

}