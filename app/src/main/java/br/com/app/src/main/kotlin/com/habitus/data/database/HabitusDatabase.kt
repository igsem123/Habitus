package br.com.app.src.main.kotlin.com.habitus.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.app.src.main.kotlin.com.habitus.data.dao.HabitDao
import br.com.app.src.main.kotlin.com.habitus.data.dao.UserDao
import br.com.app.src.main.kotlin.com.habitus.data.dao.HabitLogDao
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitLogEntity
import br.com.app.src.main.kotlin.com.habitus.data.entity.UserEntity

@Database(
    entities = [HabitEntity::class, HabitLogEntity::class, UserEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class HabitusDatabase() : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun userDao(): UserDao
    abstract fun habitLogDao(): HabitLogDao
}