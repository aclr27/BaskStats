package com.example.baskstatsapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit // <-- Nueva importación
import androidx.compose.material.icons.filled.Delete // <-- Nueva importación
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf // <-- Nueva importación
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue // <-- Nueva importación
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.LocalDate
import kotlinx.coroutines.flow.flowOf // Para el preview
import kotlinx.coroutines.launch // <-- Nueva importación

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    navController: NavController,
    eventId: Long, // El ID del evento se pasa como argumento de navegación
    eventViewModel: EventViewModel,
    performanceSheetViewModel: PerformanceSheetViewModel
) {
    // Observa el evento específico por su ID
    val event by eventViewModel.getEventById(eventId).collectAsState(initial = null)

    // Observa las fichas de rendimiento asociadas a este evento
    val performanceSheets by performanceSheetViewModel.getPerformanceSheetsForEvent(eventId).collectAsState(initial = emptyList())

    // Estado para mostrar el diálogo de confirmación de borrado
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope() // Para lanzar corrutinas de eliminación

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = event?.let {
                            when (it.type) {
                                EventType.MATCH -> "Partido"
                                EventType.TRAINING -> "Entrenamiento"
                                EventType.OTHER -> "Evento"
                            }
                        } ?: "Detalles del Evento", // Título por defecto si el evento es nulo
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
                onClick = { navController.navigate("add_performance_sheet_screen/$eventId") }, // Asegúrate de que esta ruta es correcta
                containerColor = PrimaryOrange,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "Añadir ficha de rendimiento")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightGrayBackground)
                    .padding(16.dp)
            ) {
                // Mostrar detalles del evento
                event?.let { currentEvent ->
                    EventDetailsCard(currentEvent)
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- ¡AÑADIDA SECCIÓN DE BOTONES DE ACCIÓN PARA EL EVENTO! ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                navController.navigate("edit_event_screen/${currentEvent.id}")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar Evento", tint = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text("Editar", color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { showDeleteConfirmationDialog = true }, // Muestra el diálogo de confirmación
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error), // Color de error para borrar
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar Evento", tint = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text("Eliminar", color = Color.White)
                        }
                    }
                    // --- FIN SECCIÓN DE BOTONES DE ACCIÓN PARA EL EVENTO ---

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color(0xFFEEEEEE), thickness = 2.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Fichas de Rendimiento:",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = DarkText,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Lista de fichas de rendimiento
                    if (performanceSheets.isEmpty()) {
                        Text(
                            text = "No hay fichas de rendimiento para este evento. Pulsa '+' para añadir una.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = DarkText.copy(alpha = 0.7f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(performanceSheets) { sheet ->
                                PerformanceSheetSummaryCard(sheet) {
                                    // Navegar al detalle de la ficha de rendimiento
                                    navController.navigate("performance_sheet_detail_screen/${sheet.id}")
                                }
                            }
                        }
                    }

                    // --- DIÁLOGO DE CONFIRMACIÓN DE ELIMINACIÓN DE EVENTO ---
                    if (showDeleteConfirmationDialog) {
                        AlertDialog(
                            onDismissRequest = { showDeleteConfirmationDialog = false },
                            title = { Text("Confirmar Eliminación") },
                            text = { Text("¿Estás seguro de que quieres eliminar este evento? También se eliminarán todas las fichas de rendimiento asociadas a él.") },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            eventViewModel.deleteEvent(currentEvent)
                                            showDeleteConfirmationDialog = false
                                            navController.popBackStack() // Volver a la pantalla anterior (EventsScreen o HomeScreen)
                                        }
                                    }
                                ) {
                                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDeleteConfirmationDialog = false }) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }
                    // --- FIN DIÁLOGO DE CONFIRMACIÓN ---

                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Evento no encontrado.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            color = DarkText.copy(alpha = 0.7f),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDetailsCard(event: Event) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd MMM yyyy - HH:mm") } // Formato corregido
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = event.type.displayName(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryOrange
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Fecha y Hora: ${event.dateTime.format(dateFormatter)}",
                style = MaterialTheme.typography.bodyLarge,
                color = DarkText
            )
            Spacer(modifier = Modifier.height(4.dp))

            if (event.type == EventType.MATCH) {
                event.opponent?.let {
                    Text(
                        text = "Rival: $it",
                        style = MaterialTheme.typography.bodyLarge,
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                if (event.teamScore != null && event.opponentScore != null) {
                    Text(
                        text = "Resultado: ${event.teamScore} - ${event.opponentScore}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            event.notes?.let {
                if (it.isNotBlank()) {
                    Text(
                        text = "Notas: $it",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkText.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}


// Card de resumen de ficha de rendimiento (puede ser similar a EventCard pero mostrando stats clave)
// Asegúrate de que esta función está disponible, si la tienes en PermormanceSheetsScreen.kt, puedes reutilizarla.
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PerformanceSheetSummaryCard(sheet: PerformanceSheet, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Ficha de Rendimiento: ${sheet.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}", // Formato corregido
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryOrange
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Puntos: ${sheet.points} | Asistencias: ${sheet.assists} | Rebotes: ${sheet.rebounds}",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkText
            )
            Text(
                text = "Robos: ${sheet.steals} | Tapones: ${sheet.blocks} | Pérdidas: ${sheet.turnovers}",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkText
            )
            // Puedes añadir más estadísticas clave aquí
        }
    }
}
