package br.com.app.src.main.kotlin.com.habitus.data.dao

import androidx.room.Dao
import androidx.room.Insert

import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitLogEntity
import br.com.app.src.main.kotlin.com.habitus.data.relation.HabitWithLogs
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Query("SELECT * FROM habits WHERE userId = :userId")
    fun getAllHabits(userId: String): Flow<List<HabitEntity>>

    @Query("DELETE FROM habits WHERE id = :habitId")
    suspend fun deleteHabit(habitId: Long)

    @Query("UPDATE habits SET title = :title, description = :description, isCompleted = :isCompleted, category = :category, pontuation = :pontuation, days = :days WHERE id = :habitId")
    suspend fun updateHabit(
        habitId: Long,
        title: String,
        description: String,
        isCompleted: Boolean,
        category: String,
        pontuation: Int,
        days: List<Int>
    )

    @Insert(onConflict = REPLACE)
    suspend fun insertHabitLog(habitLog: HabitLogEntity)

    @Query("SELECT * FROM habit_log WHERE date BETWEEN :inicioPeriodo and :fimPeriodo")
    suspend fun filterHabitsByPeriod(inicioPeriodo: Long, fimPeriodo: Long): List<HabitLogEntity>

    @Transaction
    @Query("SELECT * FROM habits")
    suspend fun getHabitsWithLogs(): List<HabitWithLogs>


    @Query("SELECT COUNT(*) FROM habits WHERE isCompleted = 1")
    fun getCompletedHabits(): Int

    @Query("SELECT COUNT(*) FROM habits")
    fun getHabitsCount(): Int
}