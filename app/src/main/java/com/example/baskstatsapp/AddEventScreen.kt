// app/src/main/java/com/example/baskstatsapp/AddEventScreen.kt
package com.example.baskstatsapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

// Asumo que StatInputField y ShotInputField están definidos en este mismo archivo
// o importados desde otro archivo dentro del mismo paquete.

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

    // Estado para las estadísticas del jugador
    var points by remember { mutableStateOf("") }
    var assists by remember { mutableStateOf("") }
    var offensiveRebounds by remember { mutableStateOf("") }
    var defensiveRebounds by remember { mutableStateOf("") }
    var steals by remember { mutableStateOf("") }
    var blocks by remember { mutableStateOf("") }
    var turnovers by remember { mutableStateOf("") }
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
    // Asegurarse de que el calendario está actualizado a la fecha/hora actual del estado
    calendar.set(selectedDateTime.year, selectedDateTime.monthValue - 1, selectedDateTime.dayOfMonth, selectedDateTime.hour, selectedDateTime.minute)

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
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        val currentLoggedInPlayerId = MainActivity.currentLoggedInPlayerId

                        if (currentLoggedInPlayerId == null) {
                            Toast.makeText(context, "No hay jugador logueado para guardar estadísticas.", Toast.LENGTH_LONG).show()
                            return@launch
                        }

                        // Validación básica del evento
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

                        // Crear y guardar el Evento
                        val newEvent = Event(
                            type = eventType,
                            dateTime = selectedDateTime,
                            opponent = if (eventType == EventType.MATCH) opponent.takeIf { it.isNotBlank() } else null,
                            teamScore = if (eventType == EventType.MATCH) teamScore.toIntOrNull() else null,
                            opponentScore = if (eventType == EventType.MATCH) opponentScore.toIntOrNull() else null,
                            notes = notes.takeIf { it.isNotBlank() },
                            playerId = currentLoggedInPlayerId
                        )

                        val eventId = eventViewModel.insertEvent(newEvent)

                        if (eventId > 0L) {
                            // Calcular el total de rebotes
                            val totalReboundsValue = (offensiveRebounds.toIntOrNull() ?: 0) + (defensiveRebounds.toIntOrNull() ?: 0)

                            // Crear y guardar la PerformanceSheet
                            val performanceSheet = PerformanceSheet(
                                date = selectedDateTime.toLocalDate(),
                                playerId = currentLoggedInPlayerId,
                                eventId = eventId, // Asociar la ficha al evento recién creado
                                points = points.toIntOrNull() ?: 0,
                                assists = assists.toIntOrNull() ?: 0,
                                rebounds = totalReboundsValue, // Se calcula aquí
                                offensiveRebounds = offensiveRebounds.toIntOrNull() ?: 0,
                                defensiveRebounds = defensiveRebounds.toIntOrNull() ?: 0,
                                steals = steals.toIntOrNull() ?: 0,
                                blocks = blocks.toIntOrNull() ?: 0,
                                turnovers = turnovers.toIntOrNull() ?: 0,
                                fouls = fouls.toIntOrNull() ?: 0,
                                twoPointersMade = twoPointersMade.toIntOrNull() ?: 0,
                                twoPointersAttempted = twoPointersAttempted.toIntOrNull() ?: 0,
                                threePointersMade = threePointersMade.toIntOrNull() ?: 0,
                                threePointersAttempted = threePointersAttempted.toIntOrNull() ?: 0,
                                freeThrowsMade = freeThrowsMade.toIntOrNull() ?: 0,
                                freeThrowsAttempted = freeThrowsAttempted.toIntOrNull() ?: 0,
                                minutesPlayed = minutesPlayed.toIntOrNull() ?: 0,
                                plusMinus = plusMinus.toIntOrNull() ?: 0
                            )
                            val sheetId = performanceSheetViewModel.addPerformanceSheet(performanceSheet)

                            if (sheetId > 0L) {
                                Toast.makeText(context, "Evento y ficha de rendimiento guardados con éxito!", Toast.LENGTH_SHORT).show()
                                navController.navigateUp() // Volver a la pantalla anterior
                            } else {
                                // Si la ficha no se guarda, considerar eliminar el evento también para mantener la consistencia
                                Toast.makeText(context, "Error al guardar la ficha de rendimiento. Se ha guardado el evento.", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(context, "Error al guardar el evento.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                containerColor = PrimaryOrange,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Save, "Guardar Evento y Ficha")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightGrayBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Ajuste de padding
            ) {
                Spacer(modifier = Modifier.height(8.dp)) // Espacio superior

                Text(
                    text = "Tipo de Evento:",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                // Integración del EventTypeSelector que ya habías definido
                EventTypeSelector(
                    selectedType = eventType,
                    onTypeSelected = { newType ->
                        eventType = newType
                        // Limpiar campos de partido si el tipo no es MATCH
                        if (newType != EventType.MATCH) {
                            opponent = ""
                            teamScore = ""
                            opponentScore = ""
                        }
                    }
                )

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
                        placeholder = { Text("Nombre del equipo oponente") },
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
                // Usando los StatInputField y ShotInputField que ahora están en este mismo archivo
                StatInputField("Puntos", points, { points = it })
                StatInputField("Asistencias", assists, { assists = it })
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

                Spacer(modifier = Modifier.height(16.dp)) // Espacio inferior antes del FAB
            }
        }
    )
}

// Mantenemos EventTypeSelector y displayName() aquí, si no los tienes en otro archivo.
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