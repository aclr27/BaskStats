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
import com.example.baskstatsapp.model.PerformanceSheet // Importa el modelo PerformanceSheet
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceSheetDetailScreen(navController: NavController, sheetId: String?) { // Recibimos el sheetId

    // -----------------------------------------------------------------
    // Datos de prueba (Mock Data) para una sola ficha de rendimiento
    // En una aplicación real, usarías el sheetId para buscar la ficha
    // de un ViewModel o repositorio.
    // -----------------------------------------------------------------
    val dummyPerformanceSheetsData = remember {
        listOf(
            PerformanceSheet(
                id = "ps1",
                date = LocalDate.of(2023, 11, 2),
                playerId = "player1",
                eventId = "e1", // Asociado a un evento si aplica
                points = 25,
                assists = 8,
                rebounds = 5,
                steals = 2,
                blocks = 1,
                turnovers = 3,
                freeThrowsMade = 2,
                freeThrowsAttempted = 2
            ),
            PerformanceSheet(
                id = "ps2",
                date = LocalDate.of(2023, 10, 29),
                playerId = "player1",
                eventId = "e3", // Asociado a otro evento
                points = 21,
                assists = 7,
                rebounds = 3,
                steals = 1,
                blocks = 0,
                turnovers = 2,
                freeThrowsMade = 1,
                freeThrowsAttempted = 2
            ),
            PerformanceSheet(
                id = "ps3",
                date = LocalDate.of(2023, 10, 25),
                playerId = "player1",
                eventId = null, // Ficha general, no de un evento específico
                points = 18,
                assists = 5,
                rebounds = 7,
                steals = 1,
                blocks = 1,
                turnovers = 2,
                freeThrowsMade = 4,
                freeThrowsAttempted = 5
            )
        )
    }

    // Buscar la ficha de rendimiento por el sheetId
    val performanceSheet = remember(sheetId) {
        dummyPerformanceSheetsData.firstOrNull { it.id == sheetId } ?: PerformanceSheet(
            id = "error", date = LocalDate.now(), playerId = "error",
            points = 0, assists = 0, rebounds = 0, steals = 0, blocks = 0
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Detalle de Ficha",
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
                // Información general de la ficha
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Ficha del ${
                                performanceSheet.date.format(
                                    DateTimeFormatter.ofPattern(
                                        "dd/MM/yyyy"
                                    )
                                )
                            }",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = DarkText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (performanceSheet.eventId != null) {
                            Text(
                                text = "Derivada del evento ID: ${performanceSheet.eventId}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DarkText.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sección de Estadísticas Detalladas
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Estadísticas Clave",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = DarkText
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Filas de estadísticas
                        StatRow("Puntos:", "${performanceSheet.points}")
                        StatRow("Asistencias:", "${performanceSheet.assists}")
                        StatRow("Rebotes Totales:", "${performanceSheet.rebounds}")
                        StatRow("Robos:", "${performanceSheet.steals}")
                        StatRow("Tapones:", "${performanceSheet.blocks}")
                        StatRow("Pérdidas de Balón:", "${performanceSheet.turnovers}")

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(
                            color = Color(0xFFEEEEEE),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Text(
                            text = "Tiros Libres:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = DarkText.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        StatRow("TL Anotados:", "${performanceSheet.freeThrowsMade}")
                        StatRow("TL Intentados:", "${performanceSheet.freeThrowsAttempted}")
                        StatRow(
                            "Porcentaje TL:",
                            "${(performanceSheet.freeThrowsMade.toFloat() / if (performanceSheet.freeThrowsAttempted > 0) performanceSheet.freeThrowsAttempted else 1 * 100).toInt()}%"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}