package com.example.baskstatsapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.baskstatsapp.model.PlayerStats
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(navController: NavController) {

    // Aquí simulamos datos de eventos con sus PlayerStats asociadas.
    // En una aplicación real, esto provendría de un ViewModel que cargaría datos.
    val eventsWithStats = remember {
        listOf(
            Pair(
                Event(
                    id = "e1",
                    type = EventType.MATCH,
                    dateTime = LocalDateTime.of(2023, 11, 10, 18, 0),
                    opponent = "Rival B",
                    teamScore = 90,
                    opponentScore = 85,
                    notes = "Partido de liga"
                ),
                PlayerStats(
                    playerId = "player1",
                    eventId = "e1",
                    points = 25,
                    assists = 8,
                    totalRebounds = 5,
                    offensiveRebounds = 1,
                    defensiveRebounds = 4,
                    steals = 2,
                    blocks = 1
                )
            ),
            Pair(
                Event(
                    id = "e2",
                    type = EventType.TRAINING,
                    dateTime = LocalDateTime.of(2023, 11, 8, 19, 0),
                    notes = "Entrenamiento de tiro"
                ),
                PlayerStats(
                    playerId = "player1",
                    eventId = "e2",
                    points = 15,
                    assists = 3,
                    totalRebounds = 2,
                    offensiveRebounds = 0,
                    defensiveRebounds = 2,
                    steals = 0,
                    blocks = 0
                )
            ),
            Pair(
                Event(
                    id = "e3",
                    type = EventType.MATCH,
                    dateTime = LocalDateTime.of(2023, 11, 5, 18, 0),
                    opponent = "Rival A",
                    teamScore = 107,
                    opponentScore = 98,
                    notes = "Partido amistoso"
                ),
                PlayerStats(
                    playerId = "player1",
                    eventId = "e3",
                    points = 30,
                    assists = 7,
                    totalRebounds = 10,
                    offensiveRebounds = 3,
                    defensiveRebounds = 7,
                    steals = 3,
                    blocks = 2
                )
            ),
            Pair(
                Event(
                    id = "e4",
                    type = EventType.MATCH,
                    dateTime = LocalDateTime.of(2023, 10, 30, 20, 0),
                    opponent = "Los Leones",
                    teamScore = 78,
                    opponentScore = 80,
                    notes = "Partido de copa"
                ),
                PlayerStats(
                    playerId = "player1",
                    eventId = "e4",
                    points = 18,
                    assists = 6,
                    totalRebounds = 4,
                    offensiveRebounds = 1,
                    defensiveRebounds = 3,
                    steals = 1,
                    blocks = 0
                )
            ),
            Pair(
                Event(
                    id = "e5",
                    type = EventType.TRAINING,
                    dateTime = LocalDateTime.of(2023, 10, 25, 17, 0),
                    notes = "Entrenamiento físico"
                ),
                PlayerStats(
                    playerId = "player1",
                    eventId = "e5",
                    points = 5,
                    assists = 1,
                    totalRebounds = 1,
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
                    IconButton(onClick = { navController.navigateUp() }) { // Botón de retroceso
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Icono de flecha hacia atrás
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightGrayBackground)
                    .padding(horizontal = 16.dp, vertical = 8.dp), // Ajuste de padding
                verticalArrangement = Arrangement.spacedBy(12.dp) // Espacio entre elementos
            ) {
                // Asumimos un nombre de jugador para la tarjeta, en un futuro vendría del estado de la app
                val playerNameForCards = "Tu Jugador" // Podría venir de un ViewModel o un estado de autenticación

                items(eventsWithStats) { (event, playerStats) ->
                    EventItemCard(
                        event = event,
                        playerStats = playerStats,
                        playerName = playerNameForCards,
                        modifier = Modifier.fillMaxWidth().clickable {
                            // Implementar navegación a EventDetailScreen
                            navController.navigate("event_detail_screen/${event.id}") // <-- ESTO ES EL CAMBIO
                            println("Navegando al detalle del evento: ${event.id}")
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    )
}