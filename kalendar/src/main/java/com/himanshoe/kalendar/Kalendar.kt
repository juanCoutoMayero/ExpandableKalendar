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
package com.himanshoe.kalendar

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.datetime.LocalDate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.himanshoe.kalendar.color.KalendarColors
import com.himanshoe.kalendar.color.KalendarThemeColor
import com.himanshoe.kalendar.component.day.config.KalendarDayColors
import com.himanshoe.kalendar.component.day.config.KalendarDayDefaultColors
import com.himanshoe.kalendar.component.header.config.KalendarHeaderConfig
import com.himanshoe.kalendar.model.KalendarDay
import com.himanshoe.kalendar.model.KalendarEvent
import com.himanshoe.kalendar.model.KalendarType
import com.himanshoe.kalendar.ui.firey.KalendarExpanded
import com.himanshoe.kalendar.ui.oceanic.KalendarCollapsed
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import java.time.YearMonth

@Composable
fun CustomCalendar(collapsed: Boolean, onCollapseToggle: () -> Unit) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val currentMonth = today.month
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Crossfade(targetState = collapsed) { targetCollapsed ->
        if (targetCollapsed) {
            // Week mode layout
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (dayOfWeek in daysOfWeek) {
                        Text(
                            text = dayOfWeek,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
                val daysInWeek = 7
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
//                    for (day in daysInWeek) {
//                        Day(day = day)
//                    }
                }
            }
        } else {
            // Month mode layout
            Column(modifier = Modifier.padding(16.dp)) {
                // Header row displaying the month and year
                Text(
                    text = currentMonth.toString() + " " + today.year,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )
                // Row displaying the days of the week
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (dayOfWeek in daysOfWeek) {
                        Text(
                            text = dayOfWeek,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
                // Rows displaying each week of the month
                val weeks = 4
                for (week in 1..weeks) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val daysInWeek = 7
//                        for (day in daysInWeek) {
//                            Day(day = day)
//                        }
                    }
                }
            }
        }
    }

    fun LocalDate.daysInWeek(): Int {
        return 7
    }

    IconButton(
        onClick = onCollapseToggle,
    ) {
        Icon(
            imageVector = if (collapsed) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
            contentDescription = "Toggle Calendar Mode"
        )
    }
}

@Composable
fun Day(day: LocalDate) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    val isCurrentMonth = day.month == today.month
    Text(
        text = day.dayOfMonth.toString(),
        fontSize = 16.sp,
        modifier = Modifier
            .padding(8.dp)
            .background(if (isCurrentMonth) Color.White else Color.LightGray, shape = CircleShape)
            .width(48.dp)
            .height(48.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .wrapContentHeight(Alignment.CenterVertically),
        color = if (isCurrentMonth) Color.Black else Color.Gray
    )
}

// Extension functions to calculate the number of weeks in a month and the days in a particular week



@Composable
fun Kalendar(
    modifier: Modifier = Modifier,
    kalendarThemeColor: KalendarThemeColor,
    kalendarType: KalendarType = KalendarType.Oceanic(true),
    kalendarEvents: List<KalendarEvent> = emptyList(),
    onCurrentDayClick: (KalendarDay, List<KalendarEvent>) -> Unit = { _, _ -> },
    kalendarDayColors: KalendarDayColors = KalendarDayDefaultColors.defaultColors(),
    kalendarHeaderConfig: KalendarHeaderConfig? = null,
    takeMeToDate: LocalDate? = null,
) {
    when (kalendarType) {
        is KalendarType.Oceanic -> KalendarCollapsed(
            modifier = modifier.wrapContentHeight(),
            kalendarEvents = kalendarEvents,
            onCurrentDayClick = onCurrentDayClick,
            kalendarDayColors = kalendarDayColors,
            kalendarThemeColor = kalendarThemeColor,
            takeMeToDate = takeMeToDate,
            kalendarHeaderConfig = kalendarHeaderConfig,
            showWeekDays = kalendarType.showWeekDays,
            weekDaysLength = 3,
            minDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        )
        KalendarType.Firey -> {
            KalendarExpanded(
                modifier = modifier.wrapContentHeight(),
                kalendarEvents = kalendarEvents,
                onCurrentDayClick = onCurrentDayClick,
                kalendarDayColors = kalendarDayColors,
                kalendarThemeColor = kalendarThemeColor,
                takeMeToDate = takeMeToDate,
                kalendarHeaderConfig = kalendarHeaderConfig
            )
        }
    }
}

@Composable
fun Expandablekalendar(
    modifier: Modifier = Modifier,
    kalendarThemeColors: List<KalendarThemeColor> = KalendarColors.defaultColors(),
    kalendarEvents: List<KalendarEvent> = emptyList(),
    onCurrentDayClick: (KalendarDay, List<KalendarEvent>) -> Unit = { _, _ -> },
    kalendarDayColors: KalendarDayColors = KalendarDayDefaultColors.defaultColors(),
    kalendarHeaderConfig: KalendarHeaderConfig? = null,
    takeMeToDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    showWeekDays: Boolean = true,
    startExpanded: Boolean = false,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    weekDaysLength: Int = 1,
    ){

    val isExpanded = remember {
        mutableStateOf(startExpanded)
    }
    val selectedKalendarDate = remember { mutableStateOf(takeMeToDate) }

    Column(modifier) {
        AnimatedVisibility(
            visible = isExpanded.value,
        ){
                KalendarExpanded(
                    modifier = modifier.wrapContentHeight(),
                    kalendarEvents = kalendarEvents,
                    onCurrentDayClick = onCurrentDayClick,
                    kalendarDayColors = kalendarDayColors,
                    kalendarThemeColors = kalendarThemeColors,
                    takeMeToDate = takeMeToDate,
                    kalendarHeaderConfig = kalendarHeaderConfig,
                    currentDateSelected = selectedKalendarDate,

                    )
            }
        AnimatedVisibility(
            visible = !isExpanded.value,
        ) {
            KalendarCollapsed(
                modifier = modifier.wrapContentHeight(),
                kalendarEvents = kalendarEvents,
                onCurrentDayClick = onCurrentDayClick,
                kalendarDayColors = kalendarDayColors,
                kalendarThemeColors = kalendarThemeColors,
                takeMeToDate = takeMeToDate,
                kalendarHeaderConfig = kalendarHeaderConfig,
                showWeekDays = showWeekDays,
                weekDaysLength = weekDaysLength,
                currentDateSelected = selectedKalendarDate,
                minDate = minDate,
                maxDate = maxDate
            )
        }

        Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Divider(
                Modifier
                    .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
                    .size(width = 50.dp, height = 10.dp)
                    .padding(2.dp)
                    .clickable {
                        isExpanded.value = !isExpanded.value
                    }, color = Color.Black)
        }
    }

}

