package com.example.baskstatsapp

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // <-- Asegúrate de esta importación para items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember // <-- Asegúrate de esta importación
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.EventType // <-- Asegúrate de esta importación
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import com.example.baskstatsapp.viewmodel.EventViewModel
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.LocalDate // <-- Asegúrate de esta importación para el preview
import kotlinx.coroutines.flow.flowOf // <-- Asegúrate de esta importación para el preview

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    navController: NavController,
    eventViewModel: EventViewModel
) {
    // Filtramos los eventos por el ID del jugador logueado
    val events by eventViewModel.getEventsByPlayerId(MainActivity.currentLoggedInPlayerId ?: -1L).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Mis Eventos",
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
                onClick = { navController.navigate("add_event_screen") },
                containerColor = PrimaryOrange,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "Añadir nuevo evento")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightGrayBackground)
            ) {
                // `events` es una lista, `isEmpty()` funciona correctamente en listas
                if (events.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No tienes eventos registrados.\nPulsa el botón '+' para añadir uno.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            color = DarkText.copy(alpha = 0.7f),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(events) { event ->
                            EventCard(event = event) {
                                // Navegar a la pantalla de detalle del evento
                                // Necesitarás implementar EventDetailScreen.kt
                                navController.navigate("event_detail_screen/${event.id}")
                            }
                        }
                    }
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventCard(event: Event, onClick: () -> Unit) { // <-- ¡¡ESTO ES LO MÁS IMPORTANTE!! `event: Event`
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd MMM yyyy - HH:mm") } // Formato corregido y estandarizado
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
                text = when (event.type) {
                    EventType.MATCH -> "Partido"
                    EventType.TRAINING -> "Entrenamiento"
                    EventType.OTHER -> "Otro"
                },
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryOrange
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = event.dateTime.format(dateFormatter),
                style = MaterialTheme.typography.bodyMedium,
                color = DarkText
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (event.type == EventType.MATCH && event.opponent != null) {
                Text(
                    text = "vs. ${event.opponent}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkText
                )
                if (event.teamScore != null && event.opponentScore != null) {
                    Text(
                        text = "Resultado: ${event.teamScore} - ${event.opponentScore}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkText.copy(alpha = 0.8f)
                    )
                }
            } else if (event.notes != null) {
                Text(
                    text = "Notas: ${event.notes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkText.copy(alpha = 0.7f),
                    maxLines = 2
                )
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PreviewEventsScreen() {
    BaskStatsAppTheme {
        val mockEventDao = object : com.example.baskstatsapp.dao.EventDao {
            override suspend fun insert(event: Event): Long = 1L
            override suspend fun update(event: Event) {}
            override suspend fun delete(event: Event) {}
            override fun getAllEvents(): kotlinx.coroutines.flow.Flow<List<Event>> = kotlinx.coroutines.flow.flowOf(
                listOf(
                    Event(
                        id = 1L,
                        type = EventType.MATCH,
                        dateTime = LocalDateTime.now().minusDays(2),
                        opponent = "Equipo A",
                        teamScore = 85,
                        opponentScore = 78,
                        notes = "Gran victoria en casa.",
                        playerId = 1L // Asegúrate de incluir playerId si tu modelo Event lo tiene
                    ),
                    Event(
                        id = 2L,
                        type = EventType.TRAINING,
                        dateTime = LocalDateTime.now().minusDays(1),
                        opponent = null,
                        teamScore = null,
                        opponentScore = null,
                        notes = "Entrenamiento de tiro y defensa.",
                        playerId = 1L
                    ),
                    Event(
                        id = 3L,
                        type = EventType.OTHER,
                        dateTime = LocalDateTime.now().plusDays(3),
                        opponent = null,
                        teamScore = null,
                        opponentScore = null,
                        notes = "Reunión de equipo y estrategia.",
                        playerId = 1L
                    )
                )
            )
            override fun getEventById(eventId: Long): kotlinx.coroutines.flow.Flow<Event?> = kotlinx.coroutines.flow.flowOf(null)
            override fun getEventsByPlayerId(playerId: Long): kotlinx.coroutines.flow.Flow<List<Event>> = kotlinx.coroutines.flow.flowOf(
                listOf(
                    Event(
                        id = 1L,
                        type = EventType.MATCH,
                        dateTime = LocalDateTime.now().minusDays(2),
                        opponent = "Equipo A",
                        teamScore = 85,
                        opponentScore = 78,
                        notes = "Gran victoria en casa.",
                        playerId = 1L
                    ),
                    Event(
                        id = 2L,
                        type = EventType.TRAINING,
                        dateTime = LocalDateTime.now().minusDays(1),
                        opponent = null,
                        teamScore = null,
                        opponentScore = null,
                        notes = "Entrenamiento de tiro y defensa.",
                        playerId = 1L
                    ),
                    Event(
                        id = 3L,
                        type = EventType.OTHER,
                        dateTime = LocalDateTime.now().plusDays(3),
                        opponent = null,
                        teamScore = null,
                        opponentScore = null,
                        notes = "Reunión de equipo y estrategia.",
                        playerId = 1L
                    )
                )
            )
            override fun getEventsByDate(date: LocalDate): kotlinx.coroutines.flow.Flow<List<Event>> = kotlinx.coroutines.flow.flowOf(emptyList())
        }
        val mockEventViewModel = EventViewModel(mockEventDao)
        EventsScreen(navController = rememberNavController(), eventViewModel = mockEventViewModel)
    }
}