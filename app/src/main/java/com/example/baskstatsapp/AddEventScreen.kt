package com.example.baskstatsapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.baskstatsapp.model.EventType
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlinx.coroutines.launch
import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.PerformanceSheet
import com.example.baskstatsapp.viewmodel.EventViewModel
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel // Necesitas este si vas a insertar una PerformanceSheet aquí
// Importa tus componentes StatInputField y ShotInputField desde donde los hayas centralizado
import com.example.baskstatsapp.StatInputField // Asume que StatInputField está en el mismo paquete o es de nivel superior
import com.example.baskstatsapp.ShotInputField // Asume que ShotInputField está en el mismo paquete o es de nivel superior

import android.os.Build // Para @RequiresApi
import androidx.annotation.RequiresApi // Para @RequiresApi

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O) // Añadir esta anotación
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
    var turnovers by remember { mutableStateOf("") }
    var fouls by remember { mutableStateOf("") }
    var twoPointersMade by remember { mutableStateOf("") }
    var twoPointersAttempted by remember { mutableStateOf("") }
    var threePointersMade by remember { mutableStateOf("") }
    var threePointersAttempted by remember { mutableStateOf("") }
    var freeThrowsMade by remember { mutableStateOf("") }
    var freeThrowsAttempted by remember { mutableStateOf("") }
    var minutesPlayed by remember { mutableStateOf("") }
    var plusMinus by remember { mutableStateOf("") } // Nota: Plus/Minus puede ser negativo. Tu StatInputField ya lo maneja si se hizo como en PerformanceSheetForm.

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
            selectedDateTime = selectedDateTime
                .withHour(selectedHour)
                .withMinute(selectedMinute)
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
                            Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar Hora")
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

                // Usar la StatInputField centralizada
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
                // Reutiliza ShotInputField de PerformanceSheetForm si la definiste genérica allí
                // Si no, copia su lógica aquí, pero intenta mantenerla centralizada.
                ShotInputField("Tiros de 2 Anotados", twoPointersMade, { twoPointersMade = it })
                ShotInputField("Tiros de 2 Intentados", twoPointersAttempted, { twoPointersAttempted = it })
                ShotInputField("Tiros de 3 Anotados", threePointersMade, { threePointersMade = it })
                ShotInputField("Tiros de 3 Intentados", threePointersAttempted, { threePointersAttempted = it })
                ShotInputField("Tiros Libres Anotados", freeThrowsMade, { freeThrowsMade = it })
                ShotInputField("Tiros Libres Intentados", freeThrowsAttempted, { freeThrowsAttempted = it })

                Spacer(modifier = Modifier.height(16.dp))
                StatInputField("Minutos Jugados", minutesPlayed, { minutesPlayed = it })
                StatInputField("Plus/Minus", plusMinus, { plusMinus = it }) // Tu StatInputField ya maneja el "-" si lo implementaste como te sugerí

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            val event = Event(
                                type = eventType,
                                dateTime = selectedDateTime,
                                opponent = if (eventType == EventType.MATCH) opponent.takeIf { it.isNotBlank() } else null,
                                teamScore = if (eventType == EventType.MATCH) teamScore.toIntOrNull() else null,
                                opponentScore = if (eventType == EventType.MATCH) opponentScore.toIntOrNull() else null,
                                notes = notes.takeIf { it.isNotBlank() }
                            )

                            // Insertar el evento y obtener su ID
                            val eventId = eventViewModel.insertEvent(event)

                            val performanceSheet = PerformanceSheet(
                                date = selectedDateTime.toLocalDate(), // Usar LocalDate del evento
                                playerId = "jugador_actual", // TODO: Reemplazar con el ID del jugador real del usuario loggeado
                                eventId = eventId, // Asociar al evento recién creado
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
                            // Insertar la ficha de rendimiento usando el performanceSheetViewModel
                            performanceSheetViewModel.insertPerformanceSheet(performanceSheet)

                            println("Evento y ficha de rendimiento guardados con IDs: Evento=$eventId, Ficha=${performanceSheet.id}")
                            navController.navigateUp() // Volver a la pantalla anterior después de guardar
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
