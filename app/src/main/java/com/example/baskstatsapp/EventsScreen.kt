package com.example.baskstatsapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.EventType
// Importamos PerformanceSheet, no PlayerStats, ya que PerformanceSheet es la entidad de Room
import com.example.baskstatsapp.model.PerformanceSheet // ¡CAMBIO IMPORTANTE AQUÍ!
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(navController: NavController) {

    // CAMBIOS IMPORTANTES AQUÍ:
    // 1. Los IDs de los Eventos deben ser de tipo Long (o null), no String.
    //    Room usa Long para los IDs autogenerados.
    // 2. Usamos PerformanceSheet en lugar de PlayerStats para la simulación,
    //    ya que PerformanceSheet es la entidad que Room maneja.
    val eventsWithStats = remember {
        listOf(
            Pair(
                Event(
                    id = 1L, // ID de tipo Long
                    type = EventType.MATCH,
                    dateTime = LocalDateTime.of(2023, 11, 10, 18, 0),
                    opponent = "Rival B",
                    teamScore = 90,
                    opponentScore = 85,
                    notes = "Partido de liga"
                ),
                PerformanceSheet( // Usamos PerformanceSheet
                    id = 101L, // ID para la ficha de rendimiento
                    date = LocalDateTime.of(2023, 11, 10, 18, 0).toLocalDate(), // Fecha del evento
                    playerId = "player1",
                    eventId = 1L, // Asegúrate de que este ID de evento coincida con el Evento
                    points = 25,
                    assists = 8,
                    rebounds = 5,
                    offensiveRebounds = 1,
                    defensiveRebounds = 4,
                    steals = 2,
                    blocks = 1
                    // El resto de campos de PerformanceSheet inicializados a 0 o valor por defecto si no son relevantes para el ejemplo.
                )
            ),
            Pair(
                Event(
                    id = 2L, // ID de tipo Long
                    type = EventType.TRAINING,
                    dateTime = LocalDateTime.of(2023, 11, 8, 19, 0),
                    notes = "Entrenamiento de tiro"
                ),
                PerformanceSheet( // Usamos PerformanceSheet
                    id = 102L,
                    date = LocalDateTime.of(2023, 11, 8, 19, 0).toLocalDate(),
                    playerId = "player1",
                    eventId = 2L,
                    points = 15,
                    assists = 3,
                    rebounds = 2,
                    offensiveRebounds = 0,
                    defensiveRebounds = 2,
                    steals = 0,
                    blocks = 0
                )
            ),
            Pair(
                Event(
                    id = 3L, // ID de tipo Long
                    type = EventType.MATCH,
                    dateTime = LocalDateTime.of(2023, 11, 5, 18, 0),
                    opponent = "Rival A",
                    teamScore = 107,
                    opponentScore = 98,
                    notes = "Partido amistoso"
                ),
                PerformanceSheet( // Usamos PerformanceSheet
                    id = 103L,
                    date = LocalDateTime.of(2023, 11, 5, 18, 0).toLocalDate(),
                    playerId = "player1",
                    eventId = 3L,
                    points = 30,
                    assists = 7,
                    rebounds = 10,
                    offensiveRebounds = 3,
                    defensiveRebounds = 7,
                    steals = 3,
                    blocks = 2
                )
            ),
            Pair(
                Event(
                    id = 4L, // ID de tipo Long
                    type = EventType.MATCH,
                    dateTime = LocalDateTime.of(2023, 10, 30, 20, 0),
                    opponent = "Los Leones",
                    teamScore = 78,
                    opponentScore = 80,
                    notes = "Partido de copa"
                ),
                PerformanceSheet( // Usamos PerformanceSheet
                    id = 104L,
                    date = LocalDateTime.of(2023, 10, 30, 20, 0).toLocalDate(),
                    playerId = "player1",
                    eventId = 4L,
                    points = 18,
                    assists = 6,
                    rebounds = 4,
                    offensiveRebounds = 1,
                    defensiveRebounds = 3,
                    steals = 1,
                    blocks = 0
                )
            ),
            Pair(
                Event(
                    id = 5L, // ID de tipo Long
                    type = EventType.TRAINING,
                    dateTime = LocalDateTime.of(2023, 10, 25, 17, 0),
                    notes = "Entrenamiento físico"
                ),
                PerformanceSheet( // Usamos PerformanceSheet
                    id = 105L,
                    date = LocalDateTime.of(2023, 10, 25, 17, 0).toLocalDate(),
                    playerId = "player1",
                    eventId = 5L,
                    points = 5,
                    assists = 1,
                    rebounds = 1,
                    offensiveRebounds = 0,
                    defensiveRebounds = 1,
                    steals = 0,
                    blocks = 0
                )
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Todos los Eventos",
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightGrayBackground)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val playerNameForCards = "Tu Jugador"

                items(eventsWithStats) { (event, playerStats) ->
                    EventItemCard(
                        event = event,
                        playerStats = playerStats, // Ahora playerStats es de tipo PerformanceSheet
                        playerName = playerNameForCards,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Aquí enviamos el ID como Long, directamente en la ruta
                                // Esto ASUME que tu NavGraph está configurado para recibir un Long.
                                navController.navigate("event_detail_screen/${event.id}")
                                println("Navegando al detalle del evento: ${event.id}")
                            }
                    )
                }
            }
        }
    )
}

// Asegúrate de que EventItemCard también espera PerformanceSheet, no PlayerStats.
// Si no la tienes, aquí una versión de ejemplo.
@Composable
fun EventItemCard(
    event: Event,
    playerStats: PerformanceSheet, // ¡Cambio importante aquí!
    playerName: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Fecha y hora del evento
            Text(
                text = "${event.dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Tipo de evento y Rival (si es partido)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = if (event.type == EventType.MATCH) "Partido" else "Entrenamiento",
                    fontSize = 14.sp,
                    color = PrimaryOrange,
                    fontWeight = FontWeight.Bold
                )
                if (event.type == EventType.MATCH && event.opponent != null) {
                    Text(
                        text = "vs ${event.opponent}",
                        fontSize = 14.sp,
                        color = DarkText
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Puntuación del partido (si es partido)
            if (event.type == EventType.MATCH && event.teamScore != null && event.opponentScore != null) {
                Text(
                    text = "Resultado: ${event.teamScore} - ${event.opponentScore}",
                    fontSize = 14.sp,
                    color = DarkText
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Estadísticas clave del jugador
            Text(
                text = "$playerName - Puntos: ${playerStats.points}",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
            Text(
                text = "Asistencias: ${playerStats.assists}",
                fontSize = 13.sp,
                color = DarkText
            )
            Text(
                text = "Rebotes: ${playerStats.rebounds}",
                fontSize = 13.sp,
                color = DarkText
            )
        }
    }
}
