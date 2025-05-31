package br.com.app.src.main.kotlin.com.habitus.presentation.helpers

import br.com.app.src.main.kotlin.com.habitus.data.entity.Days

fun List<Int>.toWeekdayNamesPt(): String {
    return this.mapNotNull { Days.fromValue(it) }
        .joinToString(", ") {
            when (it) {
                Days.SUNDAY -> "Dom"
                Days.MONDAY -> "Seg"
                Days.TUESDAY -> "Ter"
                Days.WEDNESDAY -> "Qua"
                Days.THURSDAY -> "Qui"
                Days.FRIDAY -> "Sex"
                Days.SATURDAY -> "Sáb"
            }
        }
}

fun List<Int>.toWeekdayNamesPtList(): List<String> {
    return this.mapNotNull { Days.fromValue(it) }
        .map {
            when (it) {
                Days.SUNDAY -> "Dom"
                Days.MONDAY -> "Seg"
                Days.TUESDAY -> "Ter"
                Days.WEDNESDAY -> "Qua"
                Days.THURSDAY -> "Qui"
                Days.FRIDAY -> "Sex"
                Days.SATURDAY -> "Sáb"
            }
        }
}