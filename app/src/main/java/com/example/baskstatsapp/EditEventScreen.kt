package com.example.baskstatsapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.baskstatsapp.model.EventType
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import com.example.baskstatsapp.viewmodel.EventViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    navController: NavController,
    eventId: Long?, // ID del evento a editar
    eventViewModel: EventViewModel
) {
    // Carga el evento existente por su ID
    val existingEvent by eventViewModel.getEventById(eventId ?: -1L).collectAsState(initial = null)

    // Estados para los campos del evento, inicializados con los valores del evento existente
    var selectedType by remember(existingEvent) { mutableStateOf(existingEvent?.type ?: EventType.MATCH) }
    var opponent by remember(existingEvent) { mutableStateOf(existingEvent?.opponent ?: "") }
    var teamScore by remember(existingEvent) { mutableStateOf(existingEvent?.teamScore?.toString() ?: "") }
    var opponentScore by remember(existingEvent) { mutableStateOf(existingEvent?.opponentScore?.toString() ?: "") }
    var notes by remember(existingEvent) { mutableStateOf(existingEvent?.notes ?: "") }

    // Estados para el DatePicker y TimePicker
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    var selectedDate by remember(existingEvent) { mutableStateOf(existingEvent?.dateTime?.toLocalDate() ?: LocalDate.now()) }
    var selectedTime by remember(existingEvent) { mutableStateOf(existingEvent?.dateTime?.toLocalTime() ?: LocalTime.now()) }

    // CoroutineScope para operaciones asíncronas
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Editar Evento",
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
            // Muestra mensaje si el evento no se encuentra
            if (existingEvent == null && eventId != null && eventId != -1L) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Evento no encontrado.",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkText.copy(alpha = 0.7f),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else if (existingEvent != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(LightGrayBackground)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Selector de tipo de evento
                    EventTypeDropdown(
                        selectedType = selectedType,
                        onTypeSelected = { selectedType = it },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de oponente (solo para MATCH)
                    if (selectedType == EventType.MATCH) {
                        OutlinedTextField(
                            value = opponent,
                            onValueChange = { opponent = it },
                            label = { Text("Oponente") },
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
                        Spacer(modifier = Modifier.height(8.dp))

                        // Campos de puntuación
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedTextField(
                                value = teamScore,
                                onValueChange = { newValue ->
                                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*$"))) {
                                        teamScore = newValue
                                    }
                                },
                                label = { Text("Tu Puntuación") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f).padding(end = 8.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = PrimaryOrange,
                                    unfocusedBorderColor = DarkText.copy(alpha = 0.5f),
                                    focusedLabelColor = PrimaryOrange,
                                    unfocusedLabelColor = DarkText.copy(alpha = 0.7f),
                                    cursorColor = PrimaryOrange,
                                    focusedTextColor = DarkText
                                )
                            )
                            OutlinedTextField(
                                value = opponentScore,
                                onValueChange = { newValue ->
                                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*$"))) {
                                        opponentScore = newValue
                                    }
                                },
                                label = { Text("Puntuación Rival") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f).padding(start = 8.dp),
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
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Campo de notas
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notas") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp)
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
                    Spacer(modifier = Modifier.height(16.dp))

                    // Selector de fecha y hora
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = selectedDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                            onValueChange = { /* No editable directamente */ },
                            label = { Text("Fecha") },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(Icons.Default.CalendarMonth, contentDescription = "Seleccionar Fecha")
                                }
                            },
                            modifier = Modifier.weight(1f).padding(end = 8.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = PrimaryOrange,
                                unfocusedBorderColor = DarkText.copy(alpha = 0.5f),
                                focusedLabelColor = PrimaryOrange,
                                unfocusedLabelColor = DarkText.copy(alpha = 0.7f),
                                cursorColor = PrimaryOrange,
                                focusedTextColor = DarkText
                            )
                        )
                        OutlinedTextField(
                            value = selectedTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                            onValueChange = { /* No editable directamente */ },
                            label = { Text("Hora") },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showTimePicker = true }) {
                                    Icon(Icons.Default.CalendarMonth, contentDescription = "Seleccionar Hora")
                                }
                            },
                            modifier = Modifier.weight(1f).padding(start = 8.dp),
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

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            existingEvent?.let { event ->
                                val updatedEvent = event.copy(
                                    type = selectedType,
                                    opponent = if (selectedType == EventType.MATCH) opponent.takeIf { it.isNotBlank() } else null,
                                    teamScore = if (selectedType == EventType.MATCH) teamScore.toIntOrNull() else null,
                                    opponentScore = if (selectedType == EventType.MATCH) opponentScore.toIntOrNull() else null,
                                    notes = notes.takeIf { it.isNotBlank() },
                                    dateTime = LocalDateTime.of(selectedDate, selectedTime)
                                )
                                coroutineScope.launch {
                                    eventViewModel.updateEvent(updatedEvent)
                                    navController.popBackStack() // Volver a la pantalla de detalle
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                    ) {
                        Text("Guardar Cambios", color = Color.White)
                    }
                }
            }
        }
    )

    // DatePicker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val newDateMillis = datePickerState.selectedDateMillis
                    if (newDateMillis != null) {
                        selectedDate = LocalDate.ofEpochDay(newDateMillis / (1000 * 60 * 60 * 24))
                    }
                    showDatePicker = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // TimePicker Dialog
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Seleccionar Hora") },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                TextButton(onClick = {
                    selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// Reutilizamos el EventTypeDropdown de AddEventScreen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTypeDropdown(
    selectedType: EventType,
    onTypeSelected: (EventType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedType.displayName(),
            onValueChange = { /* Read-only */ },
            readOnly = true,
            label = { Text("Tipo de Evento") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(), // Importante para que actúe como ancla
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = PrimaryOrange,
                unfocusedBorderColor = DarkText.copy(alpha = 0.5f),
                focusedLabelColor = PrimaryOrange,
                unfocusedLabelColor = DarkText.copy(alpha = 0.7f),
                cursorColor = PrimaryOrange,
                focusedTextColor = DarkText
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            EventType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.displayName()) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}