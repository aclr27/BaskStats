// app/src/main/java/com/example/baskstatsapp/PlayerStatsScreen.kt
package com.example.baskstatsapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.baskstatsapp.model.PlayerStatsSummary
import com.example.baskstatsapp.model.Player
import com.example.baskstatsapp.model.PerformanceSheet // Para el Preview
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel
import com.example.baskstatsapp.viewmodel.PlayerViewModel
import kotlinx.coroutines.flow.flowOf // Para el Preview
import java.time.LocalDate // Para el Preview

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerStatsScreen(
    navController: NavController,
    playerId: Long?, // ¡VOLVEMOS A LONG!
    performanceSheetViewModel: PerformanceSheetViewModel,
    playerViewModel: PlayerViewModel
) {
    if (playerId == null || playerId == 0L) { // Verificar si es 0L para IDs autogenerados
        AlertDialog(
            onDismissRequest = { navController.navigateUp() },
            title = { Text("Error") },
            text = { Text("No se ha especificado un jugador para mostrar las estadísticas.") },
            confirmButton = {
                Button(onClick = { navController.navigateUp() }) {
                    Text("Volver")
                }
            }
        )
        return
    }

    val player by playerViewModel.getPlayerById(playerId).collectAsState(initial = null) // ¡VOLVEMOS A LONG!
    val statsSummary by performanceSheetViewModel.getPlayerStatsSummary(playerId).collectAsState(initial = PlayerStatsSummary())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Estadísticas de ${player?.email ?: "Jugador"}",
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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (player == null) {
                    Text(
                        text = "Cargando datos del jugador...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = DarkText
                    )
                } else {
                    Text(
                        text = "Resumen de Rendimiento",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryOrange,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    StatsCard("Partidos Jugados:", statsSummary.totalGamesPlayed.toString())
                    StatsCard("Puntos Totales:", statsSummary.totalPoints.toString())
                    StatsCard("Promedio Puntos:", String.format("%.2f", statsSummary.avgPoints))
                    StatsCard("Asistencias Totales:", statsSummary.totalAssists.toString())
                    StatsCard("Promedio Asistencias:", String.format("%.2f", statsSummary.avgAssists))
                    StatsCard("Rebotes Totales:", statsSummary.totalRebounds.toString())
                    StatsCard("Promedio Rebotes:", String.format("%.2f", statsSummary.avgRebounds))
                    StatsCard("Robos Totales:", statsSummary.totalSteals.toString())
                    StatsCard("Promedio Robos:", String.format("%.2f", statsSummary.avgSteals))
                    StatsCard("Tapones Totales:", statsSummary.totalBlocks.toString())
                    StatsCard("Promedio Tapones:", String.format("%.2f", statsSummary.avgBlocks))
                    StatsCard("Pérdidas Totales:", statsSummary.totalTurnovers.toString())
                    StatsCard("Promedio Pérdidas:", String.format("%.2f", statsSummary.avgTurnovers))

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Eficiencia en Tiros",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryOrange,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    StatsCard("2 Puntos Encestados:", statsSummary.totalTwoPointersMade.toString())
                    StatsCard("2 Puntos Intentados:", statsSummary.totalTwoPointersAttempted.toString())
                    StatsCard("2 Puntos %:", String.format("%.2f%%", statsSummary.twoPointersPercentage))
                    StatsCard("3 Puntos Encestados:", statsSummary.totalThreePointersMade.toString())
                    StatsCard("3 Puntos Intentados:", statsSummary.totalThreePointersAttempted.toString())
                    StatsCard("3 Puntos %:", String.format("%.2f%%", statsSummary.threePointersPercentage))
                    StatsCard("Tiros Libres Encestados:", statsSummary.totalFreeThrowsMade.toString())
                    StatsCard("Tiros Libres Intentados:", statsSummary.totalFreeThrowsAttempted.toString())
                    StatsCard("Tiros Libres %:", String.format("%.2f%%", statsSummary.freeThrowsPercentage))

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Otros",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryOrange,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    StatsCard("Faltas Totales:", statsSummary.totalFouls.toString())
                    StatsCard("Minutos Jugados Totales:", statsSummary.totalMinutesPlayed.toString())
                    StatsCard("Plus/Minus Total:", statsSummary.totalPlusMinus.toString())
                }
            }
        }
    )
}

@Composable
fun StatsCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = DarkText
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryOrange
            )
        }
    }
}
