package br.com.app.src.main.kotlin.com.habitus.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitLogEntity

data class HabitWithLogs(
    @Embedded val habit: HabitEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    val logs: List<HabitLogEntity>
)
