package com.example.baskstatsapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(navController: NavController, eventId: String?) { // Recibimos el eventId

    // -----------------------------------------------------------------
    // Datos de prueba (Mock Data) para un solo evento
    // En una aplicación real, usarías el eventId para buscar el evento
    // y sus estadísticas de un ViewModel o repositorio.
    // -----------------------------------------------------------------
    val dummyEventsData = remember {
        listOf(
            Pair(
                Event(
                    id = "e1",
                    type = EventType.MATCH,
                    dateTime = LocalDateTime.of(2023, 11, 10, 18, 0),
                    opponent = "Rival B",
                    teamScore = 90,
                    opponentScore = 85,
                    notes = "Partido de liga muy intenso"
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
                    blocks = 1,
                    turnovers = 3,
                    fouls = 2,
                    twoPointersMade = 7,
                    twoPointersAttempted = 12,
                    threePointersMade = 3,
                    threePointersAttempted = 6,
                    freeThrowsMade = 2,
                    freeThrowsAttempted = 2,
                    minutesPlayed = 30,
                    plusMinus = 15
                )
            ),
            Pair(
                Event(
                    id = "e2",
                    type = EventType.TRAINING,
                    dateTime = LocalDateTime.of(2023, 11, 8, 19, 0),
                    notes = "Entrenamiento de tiro y técnica individual, con énfasis en los tiros libres."
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
                    blocks = 0,
                    turnovers = 1,
                    fouls = 0,
                    twoPointersMade = 5,
                    twoPointersAttempted = 10,
                    threePointersMade = 1,
                    threePointersAttempted = 5,
                    freeThrowsMade = 2,
                    freeThrowsAttempted = 2,
                    minutesPlayed = 60,
                    plusMinus = 0
                )
            ),
            // ... (Añadir más eventos si quieres que el preview muestre otros IDs válidos)
        )
    }

    // Buscar el evento y sus estadísticas por el eventId
    val (event, playerStats) = remember(eventId) {
        dummyEventsData.firstOrNull { it.first.id == eventId } ?: Pair(
            Event(id = "error", type = EventType.TRAINING, dateTime = LocalDateTime.now(), notes = "Evento no encontrado"),
            PlayerStats(playerId = "error", eventId = "error", points = 0, assists = 0, totalRebounds = 0, steals = 0, blocks = 0)
        )
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (event.type == EventType.MATCH) "Detalle del Partido" else "Detalle del Entrenamiento",
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
                    .verticalScroll(rememberScrollState()) // Habilita el scroll si el contenido es largo
                    .padding(16.dp)
            ) {
                // Sección de Información General del Evento
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = event.dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")),
                            style = MaterialTheme.typography.bodyLarge,
                            color = DarkText.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (event.type == EventType.MATCH) {
                            Text(
                                text = "vs ${event.opponent}",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = DarkText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Resultado: ${event.teamScore} - ${event.opponentScore}",
                                style = MaterialTheme.typography.titleMedium,
                                color = PrimaryOrange
                            )
                        } else {
                            Text(
                                text = "Entrenamiento",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = DarkText
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (event.notes != null && event.notes.isNotBlank()) {
                            Text(
                                text = "Notas: ${event.notes}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DarkText.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sección de Estadísticas Individuales del Jugador
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Tus Estadísticas",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = DarkText
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Fila 1 de Estadísticas
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatItem("Puntos", playerStats.points)
                            StatItem("Asist.", playerStats.assists)
                            StatItem("Reb. Total", playerStats.totalRebounds)
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        // Fila 2 de Estadísticas
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatItem("Reb. Ofen.", playerStats.offensiveRebounds)
                            StatItem("Reb. Def.", playerStats.defensiveRebounds)
                            StatItem("Robos", playerStats.steals)
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        // Fila 3 de Estadísticas
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatItem("Bloq.", playerStats.blocks)
                            StatItem("Pérd.", playerStats.turnovers)
                            StatItem("Faltas", playerStats.fouls)
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        // Fila de Lanzamientos
                        Divider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = "Lanzamientos:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = DarkText.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        StatRow("T2:", "${playerStats.twoPointersMade}/${playerStats.twoPointersAttempted}", "${(playerStats.twoPointersMade.toFloat() / if (playerStats.twoPointersAttempted > 0) playerStats.twoPointersAttempted else 1 * 100).toInt()}%")
                        StatRow("T3:", "${playerStats.threePointersMade}/${playerStats.threePointersAttempted}", "${(playerStats.threePointersMade.toFloat() / if (playerStats.threePointersAttempted > 0) playerStats.threePointersAttempted else 1 * 100).toInt()}%")
                        StatRow("TL:", "${playerStats.freeThrowsMade}/${playerStats.freeThrowsAttempted}", "${(playerStats.freeThrowsMade.toFloat() / if (playerStats.freeThrowsAttempted > 0) playerStats.freeThrowsAttempted else 1 * 100).toInt()}%")

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 8.dp))

                        // Otras estadísticas
                        StatRow("Minutos Jugados:", "${playerStats.minutesPlayed} min")
                        StatRow("Plus/Minus:", "${playerStats.plusMinus}")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}

// Helper Composable para mostrar filas de estadísticas detalladas
@Composable
fun StatRow(label: String, value: String, percentage: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = DarkText)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = PrimaryOrange)
            percentage?.let {
                Text(text = " ($it)", style = MaterialTheme.typography.bodySmall, color = DarkText.copy(alpha = 0.7f), modifier = Modifier.padding(start = 4.dp))
            }
        }
    }
}
