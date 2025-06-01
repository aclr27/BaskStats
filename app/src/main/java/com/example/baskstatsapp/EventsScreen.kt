// C:/Users/ancal/OneDrive/Escritorio/BaskStats/app/src/main/java/com/example/baskstatsapp/EventsScreen.kt
package com.example.baskstatsapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Importación necesaria para items(list)
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember // Required for remember to ensure flow creation is stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.baskstatsapp.dao.EventDao
import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.EventType
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import com.example.baskstatsapp.viewmodel.EventViewModel
import kotlinx.coroutines.flow.flowOf // Required for flowOf
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    navController: NavController,
    eventViewModel: EventViewModel
) {
    // Manejo de la obtención de eventos basado en el jugador loggeado
    // Usa remember para crear el flujo solo cuando loggedInPlayerId cambia
    val eventsFlow = remember(MainActivity.currentLoggedInPlayerId) {
        val playerId = MainActivity.currentLoggedInPlayerId
        if (playerId != null) {
            // Asegúrate de que este método exista en tu EventViewModel
            eventViewModel.getEventsByPlayerId(playerId)
        } else {
            flowOf(emptyList()) // Si no hay jugador loggeado, devuelve una lista vacía
        }
    }
    // Collect the events from the flow
    val events by eventsFlow.collectAsState(initial = emptyList())

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
                Icon(Icons.Filled.Add, "Añadir Evento")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightGrayBackground)
                    .padding(horizontal = 16.dp) // Adjusted padding for better consistency
            ) {
                // Corrected: events.isEmpty() should now resolve correctly
                if (events.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (MainActivity.currentLoggedInPlayerId == null) "Por favor, inicia sesión para ver tus eventos." else "No hay eventos registrados. Pulsa '+' para añadir uno.",
                            color = DarkText.copy(alpha = 0.7f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp), // Added vertical padding
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Corrected: items(list) instead of items(list.size)
                        items(events) { eventItem -> // 'eventItem' here is directly an Event object
                            EventListItem(
                                event = eventItem, // Pass the Event object
                                onClick = { navController.navigate("event_detail_screen/${eventItem.id}") } // eventItem.id is now correctly accessible
                            )
                        }
                    }
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventListItem(event: Event, onClick: (Event) -> Unit) { // Changed onClick signature to take Event
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(event) }, // Pass the actual event object on click
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = event.type.name.replace("_", " ").lowercase().capitalize(), // Format event type
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                event.opponent?.let {
                    Text(
                        text = "vs. $it",
                        fontSize = 16.sp,
                        color = DarkText.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Text(
                    text = event.dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)),
                    fontSize = 14.sp,
                    color = DarkText.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            // Optional: Add an icon or score display here
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PreviewEventsScreen() {
    BaskStatsAppTheme {
        val mockEventDao = object : EventDao {
            override fun getAllEvents(): kotlinx.coroutines.flow.Flow<List<Event>> = flowOf(
                listOf(
                    Event(1, EventType.MATCH, LocalDateTime.now(), "Team A", 80, 75, "Notas 1", 1L),
                    Event(2, EventType.TRAINING, LocalDateTime.now().minusDays(1), null, null, null, "Notas 2", 1L),
                    Event(3, EventType.MATCH, LocalDateTime.now().minusDays(2), "Team B", 90, 85, "Notas 3", 1L)
                )
            )
            override fun getEventById(id: Long): kotlinx.coroutines.flow.Flow<Event?> = flowOf(null)
            override suspend fun insert(event: Event): Long = 0L
            override suspend fun update(event: Event) {}
            override suspend fun delete(event: Event) {}
            // Mock para getEventsByPlayerId - asegúrate de que este método existe en tu EventDao real
            override fun getEventsByPlayerId(playerId: Long): kotlinx.coroutines.flow.Flow<List<Event>> = getAllEvents()
            override fun getEventsByDate(date: LocalDate): kotlinx.coroutines.flow.Flow<List<Event>> = flowOf(emptyList())
        }
        val eventViewModel = EventViewModel(mockEventDao)
        MainActivity.currentLoggedInPlayerId = 1L // Simulate logged in user
        EventsScreen(navController = rememberNavController(), eventViewModel = eventViewModel)
    }
}