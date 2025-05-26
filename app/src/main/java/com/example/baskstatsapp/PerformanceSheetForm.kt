package com.example.baskstatsapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.PerformanceSheet
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PerformanceSheetForm(
    // Parámetros para el formulario
    initialPerformanceSheet: PerformanceSheet?, // Para edición, si es nulo es para añadir
    onSave: (PerformanceSheet) -> Unit, // Callback cuando se guarda la ficha
    onCancel: () -> Unit, // Callback para cancelar
    title: String,
    // Puedes pasar los eventos para un selector si quieres vincular la ficha
    availableEvents: List<Event>? = null // Lista de eventos disponibles para vincular la ficha
) {
    // Estados del formulario
    var points by remember { mutableStateOf(initialPerformanceSheet?.points?.toString() ?: "0") }
    var assists by remember { mutableStateOf(initialPerformanceSheet?.assists?.toString() ?: "0") }
    var rebounds by remember { mutableStateOf(initialPerformanceSheet?.rebounds?.toString() ?: "0") }
    var offensiveRebounds by remember { mutableStateOf(initialPerformanceSheet?.offensiveRebounds?.toString() ?: "0") }
    var defensiveRebounds by remember { mutableStateOf(initialPerformanceSheet?.defensiveRebounds?.toString() ?: "0") }
    var steals by remember { mutableStateOf(initialPerformanceSheet?.steals?.toString() ?: "0") }
    var blocks by remember { mutableStateOf(initialPerformanceSheet?.blocks?.toString() ?: "0") }
    var turnovers by remember { mutableStateOf(initialPerformanceSheet?.turnovers?.toString() ?: "0") }
    var fouls by remember { mutableStateOf(initialPerformanceSheet?.fouls?.toString() ?: "0") }
    var freeThrowsMade by remember { mutableStateOf(initialPerformanceSheet?.freeThrowsMade?.toString() ?: "0") }
    var freeThrowsAttempted by remember { mutableStateOf(initialPerformanceSheet?.freeThrowsAttempted?.toString() ?: "0") }
    var twoPointersMade by remember { mutableStateOf(initialPerformanceSheet?.twoPointersMade?.toString() ?: "0") }
    var twoPointersAttempted by remember { mutableStateOf(initialPerformanceSheet?.twoPointersAttempted?.toString() ?: "0") }
    var threePointersMade by remember { mutableStateOf(initialPerformanceSheet?.threePointersMade?.toString() ?: "0") }
    var threePointersAttempted by remember { mutableStateOf(initialPerformanceSheet?.threePointersAttempted?.toString() ?: "0") }
    var minutesPlayed by remember { mutableStateOf(initialPerformanceSheet?.minutesPlayed?.toString() ?: "0") }
    var plusMinus by remember { mutableStateOf(initialPerformanceSheet?.plusMinus?.toString() ?: "0") }

    // Estado para la validación (ej. campos vacíos que no deberían serlo)
    var isValid by remember { mutableStateOf(true) }

    // Para la fecha de la ficha (si no está vinculada a un evento)
    var selectedDate by remember { mutableStateOf(initialPerformanceSheet?.date ?: LocalDate.now()) }

    // Para seleccionar evento si es relevante
    var selectedEvent by remember { mutableStateOf(
        if (initialPerformanceSheet?.eventId != null && availableEvents != null) {
            availableEvents.firstOrNull { it.id == initialPerformanceSheet.eventId }
        } else {
            null
        }
    )}
    var expanded by remember { mutableStateOf(false) } // Para el DropdownMenu

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
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
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()), // Permite scroll en el formulario
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Selector de evento (si hay eventos disponibles y la ficha no está vinculada a uno)
                if (availableEvents != null && (initialPerformanceSheet?.eventId == null || selectedEvent == null)) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = selectedEvent?.let { "${it.type} - ${it.dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))}" } ?: "Seleccionar Evento (Opcional)",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Vincular a Evento") },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            availableEvents.forEach { event ->
                                DropdownMenuItem(
                                    text = { Text("${event.type} - ${event.dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))} ${event.opponent?.let { " vs $it" } ?: ""}") },
                                    onClick = {
                                        selectedEvent = event
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                } else if (selectedEvent != null) {
                    Text(
                        text = "Vinculado a: ${selectedEvent?.type} - ${selectedEvent?.dateTime?.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))} ${selectedEvent?.opponent?.let { " vs $it" } ?: ""}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkText,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Campos de entrada para las estadísticas
                // Puedes agruparlos o hacerlos más bonitos
                StatInputField(label = "Puntos", value = points, onValueChange = { points = it })
                StatInputField(label = "Asistencias", value = assists, onValueChange = { assists = it })
                StatInputField(label = "Rebotes Totales", value = rebounds, onValueChange = { rebounds = it })
                StatInputField(label = "Rebotes Ofensivos", value = offensiveRebounds, onValueChange = { offensiveRebounds = it })
                StatInputField(label = "Rebotes Defensivos", value = defensiveRebounds, onValueChange = { defensiveRebounds = it })
                StatInputField(label = "Robos", value = steals, onValueChange = { steals = it })
                StatInputField(label = "Tapones", value = blocks, onValueChange = { blocks = it })
                StatInputField(label = "Pérdidas", value = turnovers, onValueChange = { turnovers = it })
                StatInputField(label = "Faltas", value = fouls, onValueChange = { fouls = it })

                Spacer(modifier = Modifier.height(16.dp))
                Text("Tiros Libres", fontWeight = FontWeight.Bold, color = DarkText, fontSize = 16.sp)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    StatInputField(label = "Anotados", value = freeThrowsMade, onValueChange = { freeThrowsMade = it }, modifier = Modifier.weight(1f))
                    Spacer(Modifier.width(8.dp))
                    StatInputField(label = "Intentados", value = freeThrowsAttempted, onValueChange = { freeThrowsAttempted = it }, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Tiros de 2 Puntos", fontWeight = FontWeight.Bold, color = DarkText, fontSize = 16.sp)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    StatInputField(label = "Anotados", value = twoPointersMade, onValueChange = { twoPointersMade = it }, modifier = Modifier.weight(1f))
                    Spacer(Modifier.width(8.dp))
                    StatInputField(label = "Intentados", value = twoPointersAttempted, onValueChange = { twoPointersAttempted = it }, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Tiros de 3 Puntos", fontWeight = FontWeight.Bold, color = DarkText, fontSize = 16.sp)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    StatInputField(label = "Anotados", value = threePointersMade, onValueChange = { threePointersMade = it }, modifier = Modifier.weight(1f))
                    Spacer(Modifier.width(8.dp))
                    StatInputField(label = "Intentados", value = threePointersAttempted, onValueChange = { threePointersAttempted = it }, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))
                StatInputField(label = "Minutos Jugados", value = minutesPlayed, onValueChange = { minutesPlayed = it })
                StatInputField(label = "+/-", value = plusMinus, onValueChange = { plusMinus = it })


                if (!isValid) {
                    Text(
                        text = "Por favor, revisa los valores introducidos. Asegúrate de que sean números.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        try {
                            // Convertir todos los strings a Int (o Long para IDs si fuera necesario)
                            // Si falla alguna conversión, se lanza una excepción y isValid se pondrá en false
                            val newSheet = PerformanceSheet(
                                id = initialPerformanceSheet?.id, // Mantiene el ID si estamos editando
                                date = selectedDate, // Usa la fecha seleccionada o la actual si no se vincular a evento
                                playerId = initialPerformanceSheet?.playerId ?: "player1", // Por ahora fijo, luego el actual del usuario
                                eventId = selectedEvent?.id, // ID del evento seleccionado
                                points = points.toInt(),
                                assists = assists.toInt(),
                                rebounds = rebounds.toInt(),
                                offensiveRebounds = offensiveRebounds.toInt(),
                                defensiveRebounds = defensiveRebounds.toInt(),
                                steals = steals.toInt(),
                                blocks = blocks.toInt(),
                                turnovers = turnovers.toInt(),
                                fouls = fouls.toInt(),
                                freeThrowsMade = freeThrowsMade.toInt(),
                                freeThrowsAttempted = freeThrowsAttempted.toInt(),
                                twoPointersMade = twoPointersMade.toInt(),
                                twoPointersAttempted = twoPointersAttempted.toInt(),
                                threePointersMade = threePointersMade.toInt(),
                                threePointersAttempted = threePointersAttempted.toInt(),
                                minutesPlayed = minutesPlayed.toInt(),
                                plusMinus = plusMinus.toInt()
                            )
                            isValid = true
                            onSave(newSheet)
                        } catch (e: NumberFormatException) {
                            isValid = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                ) {
                    Text(text = "Guardar Ficha", color = Color.White)
                }
            }
        }
    )
}

// Contenido de PerformanceSheetForm.kt (parte final)
// ... (resto del código de PerformanceSheetForm Composable)

// --- StatInputField ---
@Composable
fun StatInputField(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() } || newValue.isEmpty() || (label == "Plus/Minus" && newValue == "-")) {
                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryOrange,
            unfocusedBorderColor = DarkText.copy(alpha = 0.5f),
            focusedLabelColor = PrimaryOrange,
            unfocusedLabelColor = DarkText.copy(alpha = 0.7f)
        )
    )
}

// --- ShotInputField ---
@Composable
fun ShotInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryOrange,
            unfocusedBorderColor = DarkText.copy(alpha = 0.5f),
            focusedLabelColor = PrimaryOrange,
            unfocusedLabelColor = DarkText.copy(alpha = 0.7f)
        )
    )
}

