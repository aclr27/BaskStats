package com.example.baskstatsapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast // Asegúrate de esta importación
import android.os.Build // Necesario para @RequiresApi
import androidx.annotation.RequiresApi // Necesario para @RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.AccessTime // <-- ¡Importa este icono para la hora!
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType // Necesario para KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.EventType
import com.example.baskstatsapp.model.PerformanceSheet
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import com.example.baskstatsapp.viewmodel.EventViewModel
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime // Necesario para LocalTime.of
import java.time.format.DateTimeFormatter
import java.util.Calendar

// Asegúrate de que StatInputField y ShotInputField están accesibles.
// Si están en un paquete diferente, ajusta la importación, e.g.:
// import com.example.baskstatsapp.composables.StatInputField
// import com.example.baskstatsapp.composables.ShotInputField
// Dejo las que tienes por ahora, asumiendo que están en el mismo paquete o son de nivel superior.
import com.example.baskstatsapp.StatInputField
import com.example.baskstatsapp.ShotInputField


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEventScreen(
    navController: NavController,
    eventViewModel: EventViewModel,
    performanceSheetViewModel: PerformanceSheetViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Estado para los campos del evento
    var eventType by remember { mutableStateOf(EventType.MATCH) }
    var selectedDateTime by remember { mutableStateOf(LocalDateTime.now()) }
    var opponent by remember { mutableStateOf("") }
    var teamScore by remember { mutableStateOf("") }
    var opponentScore by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Estado para las estadísticas del jugador (inicializadas a 0 o vacías)
    var points by remember { mutableStateOf("") }
    var assists by remember { mutableStateOf("") }
    var totalRebounds by remember { mutableStateOf("") }
    var offensiveRebounds by remember { mutableStateOf("") }
    var defensiveRebounds by remember { mutableStateOf("") }
    var steals by remember { mutableStateOf("") }
    var blocks by remember { mutableStateOf("") }
    var turnovers by remember { mutableStateOf("") } // <-- Corregido: mutableEof -> mutableStateOf
    var fouls by remember { mutableStateOf("") }
    var twoPointersMade by remember { mutableStateOf("") }
    var twoPointersAttempted by remember { mutableStateOf("") }
    var threePointersMade by remember { mutableStateOf("") }
    var threePointersAttempted by remember { mutableStateOf("") }
    var freeThrowsMade by remember { mutableStateOf("") }
    var freeThrowsAttempted by remember { mutableStateOf("") }
    var minutesPlayed by remember { mutableStateOf("") }
    var plusMinus by remember { mutableStateOf("") }

    val formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val formatterTime = DateTimeFormatter.ofPattern("HH:mm")

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis() // Asegurarse de que el calendario está actualizado

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            selectedDateTime = selectedDateTime
                .withYear(selectedYear)
                .withMonth(selectedMonth + 1) // Calendar.MONTH es 0-indexed, LocalDate.monthValue es 1-indexed
                .withDayOfMonth(selectedDayOfMonth)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            selectedDateTime = LocalDateTime.of(
                selectedDateTime.toLocalDate(), // Mantener la fecha actual
                LocalTime.of(selectedHour, selectedMinute) // Actualizar la hora
            )
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Añadir Nuevo Evento",
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = DarkText
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = LightGrayBackground
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightGrayBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "Tipo de Evento:",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedButton(
                        onClick = { eventType = EventType.MATCH },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (eventType == EventType.MATCH) PrimaryOrange else Color.Transparent,
                            contentColor = if (eventType == EventType.MATCH) Color.White else PrimaryOrange
                        ),
                        border = BorderStroke(1.dp, PrimaryOrange)
                    ) {
                        Text("Partido")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = { eventType = EventType.TRAINING },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (eventType == EventType.TRAINING) PrimaryOrange else Color.Transparent,
                            contentColor = if (eventType == EventType.TRAINING) Color.White else PrimaryOrange
                        ),
                        border = BorderStroke(1.dp, PrimaryOrange)
                    ) {
                        Text("Entrenamiento")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = { eventType = EventType.OTHER },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (eventType == EventType.OTHER) PrimaryOrange else Color.Transparent,
                            contentColor = if (eventType == EventType.OTHER) Color.White else PrimaryOrange
                        ),
                        border = BorderStroke(1.dp, PrimaryOrange)
                    ) {
                        Text("Otro")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = selectedDateTime.format(formatterDate),
                    onValueChange = { /* No editable directamente */ },
                    label = { Text("Fecha del Evento") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar Fecha")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryOrange,
                        unfocusedBorderColor = DarkText.copy(alpha = 0.5f),
                        focusedLabelColor = PrimaryOrange,
                        unfocusedLabelColor = DarkText.copy(alpha = 0.7f)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = selectedDateTime.format(formatterTime),
                    onValueChange = { /* No editable directamente */ },
                    label = { Text("Hora del Evento") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { timePickerDialog.show() }) {
                            Icon(Icons.Filled.AccessTime, contentDescription = "Seleccionar Hora")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryOrange,
                        unfocusedBorderColor = DarkText.copy(alpha = 0.5f),
                        focusedLabelColor = PrimaryOrange,
                        unfocusedLabelColor = DarkText.copy(alpha = 0.7f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (eventType == EventType.MATCH) {
                    OutlinedTextField(
                        value = opponent,
                        onValueChange = { opponent = it },
                        label = { Text("Rival") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryOrange,
                            unfocusedBorderColor = DarkText.copy(alpha = 0.5f),
                            focusedLabelColor = PrimaryOrange,
                            unfocusedLabelColor = DarkText.copy(alpha = 0.7f)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedTextField(
                            value = teamScore,
                            onValueChange = { teamScore = it.filter { char -> char.isDigit() } },
                            label = { Text("Puntuación Equipo") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryOrange,
                                unfocusedBorderColor = DarkText.copy(alpha = 0.5f),
                                focusedLabelColor = PrimaryOrange,
                                unfocusedLabelColor = DarkText.copy(alpha = 0.7f)
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = opponentScore,
                            onValueChange = { opponentScore = it.filter { char -> char.isDigit() } },
                            label = { Text("Puntuación Rival") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryOrange,
                                unfocusedBorderColor = DarkText.copy(alpha = 0.5f),
                                focusedLabelColor = PrimaryOrange,
                                unfocusedLabelColor = DarkText.copy(alpha = 0.7f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notas del Evento") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    singleLine = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryOrange,
                        unfocusedBorderColor = DarkText.copy(alpha = 0.5f),
                        focusedLabelColor = PrimaryOrange,
                        unfocusedLabelColor = DarkText.copy(alpha = 0.7f)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))
                Divider(color = Color(0xFFEEEEEE), thickness = 2.dp)
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Estadísticas del Jugador:",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = DarkText,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                StatInputField("Puntos", points, { points = it })
                StatInputField("Asistencias", assists, { assists = it })
                StatInputField("Rebotes Totales", totalRebounds, { totalRebounds = it })
                StatInputField("Reb. Ofensivos", offensiveRebounds, { offensiveRebounds = it })
                StatInputField("Reb. Defensivos", defensiveRebounds, { defensiveRebounds = it })
                StatInputField("Robos", steals, { steals = it })
                StatInputField("Tapones", blocks, { blocks = it })
                StatInputField("Pérdidas de Balón", turnovers, { turnovers = it })
                StatInputField("Faltas", fouls, { fouls = it })

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Lanzamientos:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = DarkText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ShotInputField("Tiros de 2 Anotados", twoPointersMade, { twoPointersMade = it })
                ShotInputField("Tiros de 2 Intentados", twoPointersAttempted, { twoPointersAttempted = it })
                ShotInputField("Tiros de 3 Anotados", threePointersMade, { threePointersMade = it })
                ShotInputField("Tiros de 3 Intentados", threePointersAttempted, { threePointersAttempted = it })
                ShotInputField("Tiros Libres Anotados", freeThrowsMade, { freeThrowsMade = it })
                ShotInputField("Tiros Libres Intentados", freeThrowsAttempted, { freeThrowsAttempted = it })

                Spacer(modifier = Modifier.height(16.dp))
                StatInputField("Minutos Jugados", minutesPlayed, { minutesPlayed = it })
                StatInputField("Plus/Minus", plusMinus, { plusMinus = it })

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            val currentLoggedInPlayerId = MainActivity.currentLoggedInPlayerId

                            if (currentLoggedInPlayerId == null) {
                                Toast.makeText(context, "No hay jugador logueado para guardar estadísticas.", Toast.LENGTH_LONG).show()
                                return@launch
                            }

                            if (eventType == EventType.MATCH) {
                                if (opponent.isBlank()) {
                                    Toast.makeText(context, "El nombre del rival no puede estar vacío para un partido.", Toast.LENGTH_SHORT).show()
                                    return@launch
                                }
                                if (teamScore.toIntOrNull() == null || opponentScore.toIntOrNull() == null) {
                                    Toast.makeText(context, "Las puntuaciones deben ser números válidos.", Toast.LENGTH_SHORT).show()
                                    return@launch
                                }
                            }

                            val newEvent = Event(
                                type = eventType,
                                dateTime = selectedDateTime,
                                opponent = if (eventType == EventType.MATCH) opponent.takeIf { it.isNotBlank() } else null,
                                teamScore = if (eventType == EventType.MATCH) teamScore.toIntOrNull() else null,
                                opponentScore = if (eventType == EventType.MATCH) opponentScore.toIntOrNull() else null,
                                notes = notes.takeIf { it.isNotBlank() },
                                playerId = currentLoggedInPlayerId // <--- Pásalo aquí
                            )

                            val eventId = eventViewModel.insertEvent(newEvent)

                            if (eventId > 0L) { // <-- Comparación de Long con Long
                                val performanceSheet = PerformanceSheet(
                                    date = selectedDateTime.toLocalDate(),
                                    playerId = currentLoggedInPlayerId,
                                    eventId = eventId,
                                    points = points.toIntOrNull() ?: 0,
                                    assists = assists.toIntOrNull() ?: 0,
                                    rebounds = totalRebounds.toIntOrNull() ?: 0,
                                    offensiveRebounds = offensiveRebounds.toIntOrNull() ?: 0,
                                    defensiveRebounds = defensiveRebounds.toIntOrNull() ?: 0,
                                    steals = steals.toIntOrNull() ?: 0,
                                    blocks = blocks.toIntOrNull() ?: 0,
                                    turnovers = turnovers.toIntOrNull() ?: 0,
                                    freeThrowsMade = freeThrowsMade.toIntOrNull() ?: 0,
                                    freeThrowsAttempted = freeThrowsAttempted.toIntOrNull() ?: 0,
                                    twoPointersMade = twoPointersMade.toIntOrNull() ?: 0,
                                    twoPointersAttempted = twoPointersAttempted.toIntOrNull() ?: 0,
                                    threePointersMade = threePointersMade.toIntOrNull() ?: 0,
                                    threePointersAttempted = threePointersAttempted.toIntOrNull() ?: 0,
                                    fouls = fouls.toIntOrNull() ?: 0,
                                    minutesPlayed = minutesPlayed.toIntOrNull() ?: 0,
                                    plusMinus = plusMinus.toIntOrNull() ?: 0
                                )
                                val sheetId = performanceSheetViewModel.addPerformanceSheet(performanceSheet)

                                if (sheetId > 0L) { // <-- Comparación de Long con Long
                                    Toast.makeText(context, "Evento y ficha de rendimiento guardados con éxito!", Toast.LENGTH_SHORT).show()
                                    navController.navigateUp()
                                } else {
                                    Toast.makeText(context, "Error al guardar la ficha de rendimiento.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Error al guardar el evento.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                ) {
                    Text(text = "Guardar Evento", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}

// Asegúrate de que EventTypeSelector y la extensión displayName() están fuera del composable principal
// y que el preview tiene todos los ViewModels necesarios como mocks
@Composable
fun EventTypeSelector(
    selectedType: EventType,
    onTypeSelected: (EventType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        EventType.entries.forEach { type -> // O EventType.values() si no tienes entries
            Button(
                onClick = { onTypeSelected(type) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedType == type) PrimaryOrange else Color.Transparent,
                    contentColor = if (selectedType == type) Color.White else DarkText
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(type.displayName(), fontSize = 14.sp)
            }
        }
    }
}

// Función de extensión para obtener el nombre legible del EventType
fun EventType.displayName(): String {
    return when (this) {
        EventType.MATCH -> "Partido"
        EventType.TRAINING -> "Entrenamiento"
        EventType.OTHER -> "Otro"
    }
}
