package br.com.app.src.main.kotlin.com.habitus.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id : Long = 0L,
    val title: String = "",
    val description: String = "",
    var isCompleted: Boolean = false,
    val category: String = "",
    val pontuation: Int = 0,
    val days: List<Int>,
    val icon: String = "LineAwesomeIcons.HandPointUp",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val userId: String = "",
)

data class HabitWithUser(
    @Embedded val habit: HabitEntity,

    @Relation(
        parentColumn = "userId",
        entityColumn = "uid"
    )
    val user: UserEntity
)

enum class Days(val value: Int) {
    SUNDAY(1),
    MONDAY(2),
    TUESDAY(3),
    WEDNESDAY(4),
    THURSDAY(5),
    FRIDAY(6),
    SATURDAY(7);

    companion object {
        fun fromValue(value: Int): Days? {
            return Days.entries.find { it.value == value }
        }
    }
}