package com.canchas.app.presentation.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.canchas.app.data.model.Reservation
import com.canchas.app.data.model.ReservationStatus
import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.Date

@Composable
fun ReservationCalendarScreen(
    venueId: String,
    onBack: () -> Unit,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val ui by viewModel.uiState.collectAsState()

    LaunchedEffect(venueId) { viewModel.load(venueId) }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Reservar") })
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            DateSelector(selectedDate = ui.date, onPrev = { viewModel.prevDay() }, onNext = { viewModel.nextDay() })
            Spacer(Modifier.height(8.dp))
            TimeSlotList(
                date = ui.date,
                reservations = ui.reservations,
                onConfirm = { startHour, endHour -> viewModel.create(venueId, startHour, endHour) }
            )
            ui.message?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp)) }
        }
    }
}

@Composable
private fun DateSelector(selectedDate: Date, onPrev: () -> Unit, onNext: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(onClick = onPrev) { Text("◀") }
        Text(selectedDate.toString(), style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        OutlinedButton(onClick = onNext) { Text("▶") }
    }
}

@Composable
private fun TimeSlotList(
    date: Date,
    reservations: List<Reservation>,
    onConfirm: (Int, Int) -> Unit
) {
    val openHour = 9
    val closeHour = 22
    val hours = (openHour until closeHour).toList()

    var start by remember { mutableStateOf<Int?>(null) }
    var end by remember { mutableStateOf<Int?>(null) }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        items(hours) { hour ->
            val slotStart = calendarTs(date, hour)
            val slotEnd = calendarTs(date, hour + 1)
            val conflicted = reservations.any { it.startTime < slotEnd && it.endTime > slotStart }
            val selected = start != null && end != null && hour in start!! until end!!
            val bg = when {
                conflicted -> MaterialTheme.colorScheme.errorContainer
                selected -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = bg),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable {
                    if (conflicted) return@clickable
                    when {
                        start == null -> start = hour
                        end == null -> end = if (hour > start!!) hour + 1 else start!! + 1
                        else -> { start = hour; end = null }
                    }
                }
            ) { Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${hour}:00 - ${hour+1}:00")
                if (conflicted) Text("Ocupado") else if (selected) Text("Seleccionado") else Text("Disponible")
            } }
        }
        item { Spacer(Modifier.height(12.dp)) }
        item {
            Button(
                onClick = { if (start != null && end != null) onConfirm(start!!, end!!) },
                enabled = start != null && end != null,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Confirmar reservación") }
        }
    }
}

private fun calendarTs(date: Date, hour: Int): Timestamp {
    val cal = Calendar.getInstance()
    cal.time = date
    cal.set(Calendar.HOUR_OF_DAY, hour)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return Timestamp(cal.time)
}