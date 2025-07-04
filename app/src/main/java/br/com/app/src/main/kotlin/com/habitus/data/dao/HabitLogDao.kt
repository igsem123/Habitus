package br.com.app.src.main.kotlin.com.habitus.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitLogEntity
import br.com.app.src.main.kotlin.com.habitus.data.relation.HabitWithLogs

@Dao
interface HabitLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HabitLogEntity)

    @Query("SELECT * FROM habit_log WHERE habitId = :habitId AND date = :date LIMIT 1")
    suspend fun getLogForHabitAndDate(habitId: Long, date: Long): HabitLogEntity?

    @Query("SELECT * FROM habit_log WHERE date = :date")
    suspend fun getLogsForDate(date: Long): List<HabitLogEntity>

    @Transaction
    @Query("SELECT * FROM habits")
    suspend fun getHabitsWithLogs(): List<HabitWithLogs>
}