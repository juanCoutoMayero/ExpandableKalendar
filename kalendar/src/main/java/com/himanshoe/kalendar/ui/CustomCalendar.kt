package com.himanshoe.kalendar.ui

import androidx.compose.runtime.Composable
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.himanshoe.kalendar.color.KalendarColors
import com.himanshoe.kalendar.color.KalendarThemeColor
import com.himanshoe.kalendar.component.day.KalendarDay
import com.himanshoe.kalendar.component.day.config.KalendarDayColors
import com.himanshoe.kalendar.component.day.config.KalendarDayDefaultColors
import com.himanshoe.kalendar.component.header.KalendarHeader
import com.himanshoe.kalendar.component.header.config.KalendarHeaderConfig
import com.himanshoe.kalendar.component.text.KalendarNormalText
import com.himanshoe.kalendar.component.text.config.KalendarTextColor
import com.himanshoe.kalendar.component.text.config.KalendarTextConfig
import com.himanshoe.kalendar.component.text.config.KalendarTextSize
import com.himanshoe.kalendar.model.KalendarDay
import com.himanshoe.kalendar.model.KalendarEvent
import com.himanshoe.kalendar.model.toKalendarDay
import kotlinx.datetime.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import java.time.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*

val WeekDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

@Composable
fun MyCustomCalendar(
    modifier: Modifier = Modifier,
    kalendarDayColors: KalendarDayColors = KalendarDayDefaultColors.defaultColors(),
    kalendarHeaderConfig: KalendarHeaderConfig? = null,
    kalendarThemeColors: List<KalendarThemeColor> = KalendarColors.defaultColors(),
    kalendarEvents: List<KalendarEvent> = emptyList(),
    onCurrentDayClick: (KalendarDay, List<KalendarEvent>) -> Unit = { _, _ -> },
    takeMeToDate: kotlinx.datetime.LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    isExpanded: MutableState<Boolean>

) {

    val currentDateSelected = remember { mutableStateOf(takeMeToDate) }


    val currentDay = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val displayedMonth = remember {
        mutableStateOf(currentDateSelected.value.month)
    }
    val displayedYear = remember {
        mutableStateOf(currentDateSelected.value.year)
    }

    val currentWeek = remember {
        mutableStateOf(getCurrentWeek(currentDateSelected.value.toJavaLocalDate()))
    }

    var currentMonth = displayedMonth.value
    var currentYear = displayedYear.value

    var daysInMonth = currentMonth.minLength()
    LaunchedEffect(currentDateSelected) {
        displayedMonth.value = currentDateSelected.value.month
        displayedYear.value = currentDateSelected.value.year
        currentMonth = displayedMonth.value
        currentYear = displayedYear.value
        currentWeek.value = getCurrentWeek(currentDateSelected.value.toJavaLocalDate())
        daysInMonth = currentMonth.minLength()
    }
    val monthValue =
        if (currentMonth.value.toString().length == 1) "0" + currentMonth.value.toString() else currentMonth.value.toString()
    val startDayOfMonth = "$currentYear-$monthValue-01".toLocalDate()
    val firstDayOfMonth = startDayOfMonth.dayOfWeek
    val newKalenderHeaderConfig = KalendarHeaderConfig(
        kalendarTextConfig = KalendarTextConfig(
            kalendarTextSize = KalendarTextSize.SubTitle,
            kalendarTextColor = KalendarTextColor(
                kalendarThemeColors[currentDateSelected.value.month.value.minus(1)].headerTextColor,
            )
        )
    )

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colors.background
            )
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        KalendarHeader(
            modifier = Modifier.padding(vertical = 12.dp),
            month = displayedMonth.value,
            onPreviousClick = {
                if (isExpanded.value) {
                    if (displayedMonth.value.value == 1) {
                        displayedYear.value = displayedYear.value.minus(1)
                    }
                    displayedMonth.value = displayedMonth.value.minus(1)
                    val startDayOfMonth = "${displayedYear.value}-${displayedMonth.value.value}-01".toLocalDate()
                    currentWeek.value = getCurrentWeek(startDayOfMonth.toJavaLocalDate())

                } else {
                    currentWeek.value = getPreviousWeek(currentWeek.value[0])
                   if ( currentWeek.value.isNotEmpty() ){
                        displayedMonth.value = currentWeek.value[0].month
                        displayedYear.value = currentWeek.value[0].year
                    }

                }
            },
            onNextClick = {
                if (isExpanded.value) {

                    if (displayedMonth.value.value == 12) {
                        displayedYear.value = displayedYear.value.plus(1)
                    }
                    displayedMonth.value = displayedMonth.value.plus(1)
                    val startDayOfMonth = "${displayedYear.value}-${displayedMonth.value.value}-01".toLocalDate()
                    currentWeek.value = getCurrentWeek(startDayOfMonth.toJavaLocalDate())
                }else{
                    currentWeek.value = getNextWeek(currentWeek.value[0])
                    if ( currentWeek.value.isNotEmpty() ){
                        displayedMonth.value = currentWeek.value[0].month
                        displayedYear.value = currentWeek.value[0].year
                    }
                }
            },
            year = displayedYear.value,
            kalendarHeaderConfig = kalendarHeaderConfig ?: newKalenderHeaderConfig
        )
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(7),
            content = {
                items(WeekDays) {
                    KalendarNormalText(
                        text = it,
                        fontWeight = FontWeight.Normal,
                        textColor = kalendarDayColors.textColor,
                    )
                }
                if (isExpanded.value) {
                    items((getInitialDayOfMonth(firstDayOfMonth)..daysInMonth).toList()) {
                        if (it > 0) {
                            val day = getGeneratedDay(it, currentMonth, currentYear)
                            val isCurrentDay = day == currentDay
                            KalendarDay(
                                kalendarDay = day.toKalendarDay(),
                                modifier = Modifier,
                                kalendarEvents = kalendarEvents,
                                isCurrentDay = isCurrentDay,
                                onCurrentDayClick = { kalendarDay, events ->
                                    currentDateSelected.value = kalendarDay.localDate
                                    onCurrentDayClick(kalendarDay, events)
                                },
                                selectedKalendarDay = currentDateSelected.value,
                                kalendarDayColors = kalendarDayColors,
                                dotColor = kalendarThemeColors[currentMonth.value.minus(1)].headerTextColor,
                                dayBackgroundColor = kalendarThemeColors[currentMonth.value.minus(1)].dayBackgroundColor,
                            )
                        }
                    }
                } else {
                    items(currentWeek.value) { day ->
                        val kotlinLocalDate = day.atStartOfDay().toKotlinLocalDateTime().date
                        val isCurrentDay = kotlinLocalDate == currentDay
                        KalendarDay(
                            kalendarDay = kotlinLocalDate.toKalendarDay(),
                            modifier = Modifier,
                            kalendarEvents = kalendarEvents,
                            isCurrentDay = isCurrentDay,
                            onCurrentDayClick = { kalendarDay, events ->
                                currentDateSelected.value = kalendarDay.localDate
                                onCurrentDayClick(kalendarDay, events)
                            },
                            selectedKalendarDay = currentDateSelected.value,
                            kalendarDayColors = kalendarDayColors,
                            dotColor = kalendarThemeColors[currentMonth.value.minus(1)].headerTextColor,
                            dayBackgroundColor = MaterialTheme.colors.primary,
                        )

                    }
                }
            }
        )
    }
}

@Composable
fun DateItem(
    date: LocalDate,
    isToday: Boolean,
    isSelected: Boolean,
    onDateClick: (LocalDate) -> Unit
) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colors.primary
        isToday -> MaterialTheme.colors.secondary
        else -> Color.Transparent
    }
    val contentColor = when {
        isSelected -> MaterialTheme.colors.onPrimary
        isToday -> MaterialTheme.colors.onSecondary
        else -> MaterialTheme.colors.onSurface
    }

    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(color = backgroundColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                shape = CircleShape
            )
            .clickable(onClick = { onDateClick })
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                color = contentColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                color = contentColor,
                fontSize = 12.sp
            )
        }
    }
}

enum class CalendarMode {
    MONTH, WEEK
}


private fun getInitialDayOfMonth(firstDayOfMonth: DayOfWeek) = -(firstDayOfMonth.value).minus(2)

private fun getBeginningOfWeek(selectedDate: LocalDate): LocalDate? {
    val startOfWeek = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    return startOfWeek
}

private fun getCurrentWeek(selectedDate: LocalDate): List<LocalDate> {
    var days = listOf<LocalDate>()

    val startOfWeek = getBeginningOfWeek(selectedDate = selectedDate)
    startOfWeek?.let {
        days = getNextSevenDaysFromDate(it)
    }

    return days
}


private fun getNextSevenDaysFromDate(date: LocalDate): List<LocalDate> {
    val days = mutableListOf<LocalDate>()

    for (i in 0 until 7) {
        val day = date.plusDays(i.toLong())
        days.add(day)
    }

    return days
}

private fun getNextWeek(date: LocalDate): List<LocalDate> {
    val startOfNextWeek = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
    return getNextSevenDaysFromDate(startOfNextWeek)
}

private fun getPreviousWeek(date: LocalDate): List<LocalDate> {
    val startOfPreviousWeek = date.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
    return getNextSevenDaysFromDate(startOfPreviousWeek)
}

private fun getInitialDayOfWeek(firstDayOfWeek: DayOfWeek): Int {
    return -(firstDayOfWeek.value).minus(2)
}

private fun getGeneratedDay(
    day: Int,
    currentMonth: Month,
    currentYear: Int
): kotlinx.datetime.LocalDate {
    val monthValue =
        if (currentMonth.value.toString().length == 1) "0${currentMonth.value}" else currentMonth.value.toString()
    val newDay = if (day.toString().length == 1) "0$day" else day
    return "$currentYear-$monthValue-$newDay".toLocalDate()
}

