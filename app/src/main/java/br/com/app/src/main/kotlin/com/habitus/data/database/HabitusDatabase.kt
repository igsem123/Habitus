package br.com.app.src.main.kotlin.com.habitus.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.app.src.main.kotlin.com.habitus.data.dao.HabitDao
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitLogEntity

@Database(
    entities = [HabitEntity::class, HabitLogEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class HabitusDatabase() : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}