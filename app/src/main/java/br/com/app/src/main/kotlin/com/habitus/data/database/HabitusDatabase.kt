package br.com.app.src.main.kotlin.com.habitus.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.app.src.main.kotlin.com.habitus.data.dao.TaskDao
import br.com.app.src.main.kotlin.com.habitus.data.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = true
)

abstract class HabitusDatabase() : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}