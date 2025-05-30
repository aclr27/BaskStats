package com.example.baskstatsapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.EventType
import com.example.baskstatsapp.model.PerformanceSheet
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import com.example.baskstatsapp.viewmodel.EventViewModel
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPerformanceSheetScreen(
    navController: NavController,
    performanceSheetViewModel: PerformanceSheetViewModel,
    eventViewModel: EventViewModel, // Se necesita para seleccionar el evento si no se pasa por argumento
    eventId: Long? = null // Puede venir de EventDetailScreen
) {
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    var showEventSelectionDialog by remember { mutableStateOf(false) }

    // Si se pasa un eventId, intenta cargar ese evento
    val eventFromNav by eventViewModel.getEventById(eventId ?: -1L).collectAsState(initial = null)
    LaunchedEffect(eventFromNav) {
        if (eventId != null && eventFromNav != null) {
            selectedEvent = eventFromNav
        }
    }

    val allEvents by eventViewModel.getAllEvents().collectAsState(initial = emptyList())

    // Estados para los campos de la ficha de rendimiento
    var points by remember { mutableStateOf("") }
    var assists by remember { mutableStateOf("") }
    var rebounds by remember { mutableStateOf("") }
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

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Añadir Ficha de Rendimiento",
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
                    .verticalScroll(rememberScrollState()) // Para poder hacer scroll
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Selector de Evento
                Button(
                    onClick = { showEventSelectionDialog = true },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                ) {
                    Text(
                        text = selectedEvent?.let { "Evento: ${it.type.displayName()} - ${it.dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT))}" } ?: "Seleccionar Evento (Obligatorio)",
                        color = Color.White
                    )
                }

                if (showEventSelectionDialog) {
                    EventSelectionDialog(
                        events = allEvents,
                        onDismiss = { showEventSelectionDialog = false },
                        onEventSelected = { event ->
                            selectedEvent = event
                            showEventSelectionDialog = false
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Campos para las estadísticas
                NumericInputField(value = points, onValueChange = { points = it }, label = "Puntos")
                NumericInputField(value = assists, onValueChange = { assists = it }, label = "Asistencias")
                NumericInputField(value = rebounds, onValueChange = { rebounds = it }, label = "Rebotes Totales")
                NumericInputField(value = offensiveRebounds, onValueChange = { offensiveRebounds = it }, label = "Rebotes Ofensivos")
                NumericInputField(value = defensiveRebounds, onValueChange = { defensiveRebounds = it }, label = "Rebotes Defensivos")
                NumericInputField(value = steals, onValueChange = { steals = it }, label = "Robos")
                NumericInputField(value = blocks, onValueChange = { blocks = it }, label = "Tapones")
                NumericInputField(value = turnovers, onValueChange = { turnovers = it }, label = "Pérdidas")
                NumericInputField(value = fouls, onValueChange = { fouls = it }, label = "Faltas")
                NumericInputField(value = twoPointersMade, onValueChange = { twoPointersMade = it }, label = "T2 Anotados")
                NumericInputField(value = twoPointersAttempted, onValueChange = { twoPointersAttempted = it }, label = "T2 Intentados")
                NumericInputField(value = threePointersMade, onValueChange = { threePointersMade = it }, label = "T3 Anotados")
                NumericInputField(value = threePointersAttempted, onValueChange = { threePointersAttempted = it }, label = "T3 Intentados")
                NumericInputField(value = freeThrowsMade, onValueChange = { freeThrowsMade = it }, label = "TL Anotados")
                NumericInputField(value = freeThrowsAttempted, onValueChange = { freeThrowsAttempted = it }, label = "TL Intentados")
                NumericInputField(value = minutesPlayed, onValueChange = { minutesPlayed = it }, label = "Minutos Jugados")
                NumericInputField(value = plusMinus, onValueChange = { plusMinus = it }, label = "+/-", isSigned = true)


                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        // Validar campos y guardar la ficha
                        val currentEvent = selectedEvent
                        val currentLoggedInPlayerId = MainActivity.currentLoggedInPlayerId

                        if (currentEvent == null || currentLoggedInPlayerId == null) {
                            // Mostrar un Toast o SnackBar indicando que el evento/jugador es obligatorio
                            return@Button
                        }

                        // Convertir a Int o Long. Usar `toIntOrNull()` para manejar entradas vacías/no numéricas
                        val newSheet = PerformanceSheet(
                            id = 0L, // Room autogenerará el ID
                            date = LocalDate.now(), // La fecha de hoy por defecto
                            playerId = currentLoggedInPlayerId,
                            eventId = currentEvent.id,
                            points = points.toIntOrNull() ?: 0,
                            assists = assists.toIntOrNull() ?: 0,
                            rebounds = rebounds.toIntOrNull() ?: 0,
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

                        coroutineScope.launch {
                            val newSheetId = performanceSheetViewModel.addPerformanceSheet(newSheet)
                            if (newSheetId > 0L) {
                                navController.popBackStack() // Volver a la pantalla anterior (EventDetail o PerformanceSheets)
                                // O navegar a PerformanceSheetDetailScreen(newSheetId) si la tienes
                            } else {
                                // Mostrar error al guardar
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                ) {
                    Text("Guardar Ficha", color = Color.White)
                }
            }
        }
    )
}

// Composable auxiliar para campos de entrada numéricos
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumericInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isSigned: Boolean = false // Para permitir números negativos (ej. +/-)
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (isSigned) {
                // Permite '-' al principio, y luego solo dígitos
                if (newValue.isEmpty() || newValue == "-" || newValue.matches(Regex("^-?\\d*$"))) {
                    onValueChange(newValue)
                }
            } else {
                // Solo permite dígitos (o vacío)
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d*$"))) {
                    onValueChange(newValue)
                }
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = PrimaryOrange,
            unfocusedBorderColor = DarkText.copy(alpha = 0.5f),
            focusedLabelColor = PrimaryOrange,
            unfocusedLabelColor = DarkText.copy(alpha = 0.7f),
            cursorColor = PrimaryOrange,
            focusedTextColor = DarkText
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventSelectionDialog(
    events: List<Event>,
    onDismiss: () -> Unit,
    onEventSelected: (Event) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar Evento") },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(events) { event ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEventSelected(event) }
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        Text(
                            text = "${event.type.displayName()} - ${event.dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT))}",
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        event.opponent?.let {
                            Text(text = "vs. $it", color = DarkText.copy(alpha = 0.8f))
                        }
                    }
                    Divider(color = Color.LightGray)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}
