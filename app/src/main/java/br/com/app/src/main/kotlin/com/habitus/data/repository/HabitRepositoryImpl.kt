package br.com.app.src.main.kotlin.com.habitus.data.repository

import br.com.app.src.main.kotlin.com.habitus.data.database.HabitusDatabase
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitLogEntity
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val db: HabitusDatabase
) : HabitRepository {
    override suspend fun insertHabit(habit: HabitEntity) {
        db.habitDao().insertHabit(habit)
    }

    override suspend fun getAllHabits(userId: String): List<HabitEntity> {
        return db.habitDao().getAllHabits(userId)
    }

    override suspend fun getCompletedHabits(): Int {
        return db.habitDao().getCompletedHabits()
    }

    override suspend fun deleteHabit(habitId: Long) {
        db.habitDao().deleteHabit(habitId)
    }

    override suspend fun updateHabit(habit: HabitEntity) { // Preciso verificar o funcionamento disso depois de implementar o ViewModel
        db.habitDao().updateHabit(
            habit.id,
            habit.title,
            habit.description,
            habit.isCompleted,
            habit.category,
            habit.pontuation,
            habit.days
        )
    }

    override suspend fun insertLog(
        log: HabitLogEntity
    ) {
        db.habitDao().insertHabitLog(log)
    }

    override suspend fun filterHabitsByPeriod(
        inicioPeriodo: Long,
        fimPeriodo: Long
    ): List<HabitLogEntity> {
        return db.habitDao().filterHabitsByPeriod(inicioPeriodo, fimPeriodo)
    }

    override suspend fun getHabitsCount(): Int {
        return db.habitDao().getHabitsCount()
    }
}