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
import java.time.ZoneId // Importar ZoneId

// Asegúrate de que StatInputField y ShotInputField estén accesibles.
// Si están en un paquete diferente, ajusta la importación, e.g.:
// import com.example.baskstatsapp.composables.StatInputField
// import com.example.baskstatsapp.composables.ShotInputField

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PerformanceSheetForm(
    initialPerformanceSheet: PerformanceSheet?,
    onSave: (PerformanceSheet) -> Unit,
    onCancel: () -> Unit,
    title: String,
    availableEvents: List<Event>? = null,
    initialSelectedEventId: Long? = null
) {
    var points by remember { mutableStateOf(initialPerformanceSheet?.points?.toString() ?: "0") }
    var assists by remember { mutableStateOf(initialPerformanceSheet?.assists?.toString() ?: "0") }
    // Eliminado 'rebounds' como campo de entrada directo. Se calcula desde ofensivos/defensivos.
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

    var isValid by remember { mutableStateOf(true) }

    // Fecha inicial del evento: Si hay una ficha inicial, usa su eventDate; si no, usa la fecha actual.
    // Convertimos de Long a LocalDate para el DatePicker, y de vuelta a Long para guardar.
    var selectedLocalDate by remember {
        mutableStateOf(
            if (initialPerformanceSheet != null) {
                // Convertir Long (milisegundos) a LocalDate
                java.time.Instant.ofEpochMilli(initialPerformanceSheet.eventDate)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            } else {
                LocalDate.now()
            }
        )
    }

    // Lógica para inicializar selectedEvent con initialSelectedEventId
    var selectedEvent by remember { mutableStateOf(
        if (initialSelectedEventId != null && availableEvents != null) {
            availableEvents.firstOrNull { it.id == initialSelectedEventId }
        } else if (initialPerformanceSheet?.eventId != null && availableEvents != null) {
            availableEvents.firstOrNull { it.id == initialPerformanceSheet.eventId }
        } else {
            null
        }
    )}
    var expanded by remember { mutableStateOf(false) }

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
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Selector de evento
                if (availableEvents != null && (initialPerformanceSheet?.eventId == null || selectedEvent == null)) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
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
                                        // Cuando se selecciona un evento, actualiza la fecha de la ficha para que coincida
                                        selectedLocalDate = event.dateTime.toLocalDate()
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

                // Selector de Fecha (para casos donde no hay evento asociado o la fecha es diferente)
                // Usamos el DatePicker de Material3 aquí si quieres permitir cambiar la fecha manualmente
                // val datePickerDialog = rememberDatePickerDialog() // Esto sería una función que lanza el DatePickerDialog
                // Por ahora, solo mostramos la fecha seleccionada.
                Text(
                    text = "Fecha: ${selectedLocalDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            // TODO: Implementar DatePicker para cambiar selectedLocalDate
                            // datePickerDialog.show()
                        },
                    color = DarkText,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))


                StatInputField(label = "Puntos", value = points, onValueChange = { points = it })
                StatInputField(label = "Asistencias", value = assists, onValueChange = { assists = it })
                // Eliminado StatInputField para "Rebotes Totales" ya que no es un campo directo en el modelo
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
                        text = "Por favor, revisa los valores introducidos. Asegúrate de que sean números enteros válidos.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        try {
                            // Convertir todas las entradas a Int, verificando si son números válidos.
                            val p = points.toIntOrNull() ?: throw NumberFormatException("Puntos")
                            val a = assists.toIntOrNull() ?: throw NumberFormatException("Asistencias")
                            val or = offensiveRebounds.toIntOrNull() ?: throw NumberFormatException("Rebotes Ofensivos")
                            val dr = defensiveRebounds.toIntOrNull() ?: throw NumberFormatException("Rebotes Defensivos")
                            val s = steals.toIntOrNull() ?: throw NumberFormatException("Robos")
                            val b = blocks.toIntOrNull() ?: throw NumberFormatException("Tapones")
                            val t = turnovers.toIntOrNull() ?: throw NumberFormatException("Pérdidas")
                            val f = fouls.toIntOrNull() ?: throw NumberFormatException("Faltas")
                            val ftm = freeThrowsMade.toIntOrNull() ?: throw NumberFormatException("Tiros Libres Anotados")
                            val fta = freeThrowsAttempted.toIntOrNull() ?: throw NumberFormatException("Tiros Libres Intentados")
                            val t2m = twoPointersMade.toIntOrNull() ?: throw NumberFormatException("Tiros de 2 Anotados")
                            val t2a = twoPointersAttempted.toIntOrNull() ?: throw NumberFormatException("Tiros de 2 Intentados")
                            val t3m = threePointersMade.toIntOrNull() ?: throw NumberFormatException("Tiros de 3 Anotados")
                            val t3a = threePointersAttempted.toIntOrNull() ?: throw NumberFormatException("Tiros de 3 Intentados")
                            val mp = minutesPlayed.toIntOrNull() ?: throw NumberFormatException("Minutos Jugados")
                            val pm = plusMinus.toIntOrNull() ?: throw NumberFormatException("Plus/Minus")

                            val newSheet = PerformanceSheet(
                                sheetId = initialPerformanceSheet?.sheetId ?: 0L, // Usar sheetId
                                playerId = initialPerformanceSheet?.playerId ?: MainActivity.currentLoggedInPlayerId ?: -1L,
                                eventId = selectedEvent?.id ?: 0L, // Si no hay evento, asigna 0L (o null si el modelo lo permite)
                                // Convertir LocalDate a Long (milisegundos desde la época Unix)
                                eventDate = selectedLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                                points = p,
                                assists = a,
                                offensiveRebounds = or,
                                defensiveRebounds = dr,
                                steals = s,
                                turnovers = t,
                                blocks = b,
                                fouls = f,
                                freeThrowsMade = ftm,
                                freeThrowsAttempted = fta,
                                twoPointersMade = t2m,
                                twoPointersAttempted = t2a,
                                threePointersMade = t3m,
                                threePointersAttempted = t3a,
                                minutesPlayed = mp,
                                plusMinus = pm
                            )
                            isValid = true
                            onSave(newSheet)
                        } catch (e: NumberFormatException) {
                            isValid = false
                            // Puedes usar un Toast o SnackBar aquí para mostrar un mensaje más específico al usuario
                            // por ejemplo, "Valor inválido para: ${e.message}"
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

// StatInputField y ShotInputField se mantienen igual
@Composable
fun StatInputField(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() } || newValue.isEmpty() || (label == "+/-" && newValue == "-") || (label == "+/-" && newValue.length == 1 && newValue == "-")) {
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