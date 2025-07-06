package br.com.app.src.main.kotlin.com.habitus.data.repository

import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    suspend fun insertHabit(habit: HabitEntity)

    fun getAllHabits(userId: String): Flow<List<HabitEntity>> // Retorna um Flow e não é mais suspend

    suspend fun getCompletedHabits(): Int

    suspend fun deleteHabit(habitId: Long)

    suspend fun updateHabit(habit: HabitEntity)

    suspend fun insertLog(log: HabitLogEntity)

    suspend fun filterHabitsByPeriod(inicioPeriodo: Long, fimPeriodo: Long): List<HabitLogEntity>

    suspend fun getHabitsCount() : Int

    suspend fun getHabitsReport(): String

}