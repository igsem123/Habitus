package br.com.app.src.main.kotlin.com.habitus.data.repository

import br.com.app.src.main.kotlin.com.habitus.data.database.HabitusDatabase
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitLogEntity
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HabitRepositoryImpl @Inject constructor(
    private val db: HabitusDatabase
) : HabitRepository {
    override suspend fun insertHabit(habit: HabitEntity) {
        db.habitDao().insertHabit(habit)
    }

    override suspend fun getAllHabits(): List<HabitEntity> {
        return db.habitDao().getAllHabits()
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
    private fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return formatter.format(Date())
    }


    override suspend fun getHabitsReport(): String {
        val habitDao = db.habitDao()

        // Busca todos os hábitos com todos os logs (sem filtro)
        val habitsWithLogs = habitDao.getHabitsWithLogs()

        val totalHabits = habitsWithLogs.size
        val completedHabits = habitsWithLogs.count { it.habit.isCompleted }

        val completionRate = if (totalHabits > 0) {
            (completedHabits * 100) / totalHabits
        } else 0

        val report = StringBuilder()
        report.appendLine("RELATÓRIO DE HÁBITOS")
        report.appendLine("Data de geração: ${getCurrentDate()}")
        report.appendLine()
        report.appendLine("Resumo Geral")
        report.appendLine()
        report.appendLine("Total de hábitos: $totalHabits")
        report.appendLine("Hábitos concluídos: $completedHabits")
        report.appendLine("Taxa de conclusão: $completionRate%")
        report.appendLine()
        report.appendLine("Detalhamento por Hábito")


        habitsWithLogs.forEach { hw ->
            val habit = hw.habit
            val status = if (habit.isCompleted) "✅ Concluído" else "❌ Em andamento"
            report.appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            report.appendLine("Título     : ${habit.title}")
            report.appendLine("Categoria  : ${habit.category}")
            report.appendLine("Pontuação  : ${habit.pontuation}")
            report.appendLine("Status     : $status")
        }


        return report.toString()
    }




}