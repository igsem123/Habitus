package br.com.app.src.main.kotlin.com.habitus.data.database

import androidx.room.TypeConverter

/**
 * Conversores para o banco de dados Room para lidar com tipos de dados personalizados.
 * Como ex.: converter uma lista de inteiros em uma string
 */
class Converters {

    @TypeConverter
    fun fromIntList(days: List<Int>): String {
        return days.joinToString(",")
    }

    @TypeConverter
    fun toIntList(data: String): List<Int> {
        return if (data.isEmpty()) emptyList() else data.split(",").map { it.toInt() }
    }
}
