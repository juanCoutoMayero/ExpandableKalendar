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
package com.himanshoe.sample.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.himanshoe.kalendar.Expandablekalendar
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.model.KalendarEvent
import com.himanshoe.kalendar.model.KalendarType
import com.himanshoe.kalendar.ui.MyCustomCalendar
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Composable
fun KalendarDemo() {
    val isExpanded = remember {
        mutableStateOf(true)
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Expandablekalendar(
            modifier = Modifier,
            weekDaysLength = 3,
            minDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        )
        MyCustomCalendar(isExpanded= isExpanded)
        Button(onClick = {isExpanded.value = !isExpanded.value}) {

        }
    }
}
