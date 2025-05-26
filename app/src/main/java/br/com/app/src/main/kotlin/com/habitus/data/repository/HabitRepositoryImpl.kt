package br.com.app.src.main.kotlin.com.habitus.data.repository

import br.com.app.src.main.kotlin.com.habitus.data.database.HabitusDatabase
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val db: HabitusDatabase
) : HabitRepository {
    override suspend fun insertHabit(habit: HabitEntity) {
        db.habitDao().insertHabit(habit)
    }

    override suspend fun getAllHabits(): List<HabitEntity> {
        return db.habitDao().getAllHabits()
    }

    override suspend fun deleteHabit(habitId: String) {
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
            habit.days.toString()
        )
    }
}