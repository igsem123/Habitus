package br.com.app.src.main.kotlin.com.habitus.data.repository

import br.com.app.src.main.kotlin.com.habitus.data.database.HabitusDatabase
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitLogEntity
import br.com.app.src.main.kotlin.com.habitus.data.relation.HabitWithLogs
// import br.com.app.src.main.kotlin.com.habitus.domain.repository.HabitLogRepository
import javax.inject.Inject

class HabitLogRepositoryImpl @Inject constructor(
    private val db: HabitusDatabase
) : HabitLogRepository {

    override suspend fun insertLog(log: HabitLogEntity) {
        db.habitDao().insertHabitLog(log)
    }

    override suspend fun getLogForHabitAndDate(habitId: Long, date: Long): HabitLogEntity? {
        return db.habitDao().getHabitLogByHabitAndDate(habitId, date)
    }

    override suspend fun getLogsForDate(date: Long): List<HabitLogEntity> {
        return db.habitDao().getHabitLogsByDate(date)
    }

    override suspend fun getHabitsWithLogs(): List<HabitWithLogs> {
        return db.habitDao().getHabitsWithLogs()
    }
}