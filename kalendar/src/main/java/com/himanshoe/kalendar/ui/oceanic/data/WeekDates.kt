/*
 * Copyright 2022 Kalendar Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.himanshoe.kalendar.ui.oceanic.data

import kotlinx.datetime.*
import java.time.DayOfWeek
import java.time.YearMonth

internal fun LocalDate.getNext7Dates(): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    repeat(7) { day ->
        dates.add(this.plus(day, DateTimeUnit.DAY))
    }
    return dates
}

internal fun LocalDate.getPrevious7Dates(): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    repeat(7) { day ->
        dates.add(this.minus(day.plus(1), DateTimeUnit.DAY))
    }
    return dates.reversed()
}
//
//fun Month.weeksInMonth(): Int {
//    val firstDayOfMonth = this.
//    val lastDayOfMonth = atEndOfMonth()
//    val firstDayOfFirstWeek = firstDayOfMonth.startOfWeek()
//    val lastDayOfLastWeek = lastDayOfMonth.endOfWeek()
//    val days = lastDayOfLastWeek.dayOfYear - firstDayOfFirstWeek.dayOfYear + 1
//    return days / 7 + if (days % 7 == 0) 0 else 1
//}
//fun Month.daysInMonth(): Int {
//    return lengthOfMonth()
//}
//
//fun LocalDate.startOfWeek(): LocalDate {
//    return this.minus(dayOfWeek.value.toLong() % 7,  DateTimeUnit.DAY)
//}
//
//fun LocalDate.endOfWeek(): LocalDate {
//    return this.plus(6 - dayOfWeek.value.toLong() % 7,  DateTimeUnit.DAY)
//}
//
//fun LocalDate.daysInWeek(): Int {
//    val firstDayOfWeek = dayOfWeek.value
//    val lastDayOfWeek = DayOfWeek.of((firstDayOfWeek + 6) % 7 + 1).value
//    return lastDayOfWeek - firstDayOfWeek + 1
//}


internal fun LocalDate.getCurrentWeekStartingOn(weekDay: DayOfWeek): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    if ( weekDay == this.dayOfWeek) {
        repeat(7) { day ->
            dates.add(this.plus(day, DateTimeUnit.DAY))
        }
    }else {
        when (this.dayOfWeek) {
            DayOfWeek.MONDAY -> {


            }
            DayOfWeek.TUESDAY -> TODO()
            DayOfWeek.WEDNESDAY -> TODO()
            DayOfWeek.THURSDAY -> TODO()
            DayOfWeek.FRIDAY -> TODO()
            DayOfWeek.SATURDAY -> TODO()
            DayOfWeek.SUNDAY -> TODO()
        }
    }
    return dates
}