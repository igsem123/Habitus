package br.com.app.src.main.kotlin.com.habitus.data.repository

import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitLogEntity
import br.com.app.src.main.kotlin.com.habitus.data.relation.HabitWithLogs

interface HabitLogRepository {
    suspend fun insertLog(log: HabitLogEntity)
    suspend fun getLogForHabitAndDate(habitId: Long, date: Long): HabitLogEntity?
    suspend fun getLogsForDate(date: Long): List<HabitLogEntity>
    suspend fun getHabitsWithLogs(): List<HabitWithLogs>
}