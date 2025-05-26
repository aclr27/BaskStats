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
import com.example.baskstatsapp.model.PerformanceSheet
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel
// Importar StatRow desde el archivo común de composables
// Android Studio debería añadir esto automáticamente al eliminar la definición local si existiera
// import com.example.baskstatsapp.StatRow // Asegúrate de que esto se importa si es necesario
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceSheetDetailScreen(
    navController: NavController,
    sheetId: Long?,
    performanceSheetViewModel: PerformanceSheetViewModel
) {

    val dummyPerformanceSheetsData = remember {
        listOf(
            PerformanceSheet(
                id = 101L, // <-- CAMBIO CLAVE: ID es Long
                date = LocalDate.of(2023, 11, 2),
                playerId = "player1",
                eventId = 1L, // <-- CAMBIO CLAVE: eventId es Long?
                points = 25,
                assists = 8,
                rebounds = 5,
                steals = 2,
                blocks = 1,
                turnovers = 3,
                freeThrowsMade = 2,
                freeThrowsAttempted = 2,
                fouls = 0, twoPointersMade = 0, twoPointersAttempted = 0,
                threePointersMade = 0, threePointersAttempted = 0,
                minutesPlayed = 0, plusMinus = 0 // Inicializar todos los campos
            ),
            PerformanceSheet(
                id = 102L, // <-- CAMBIO CLAVE: ID es Long
                date = LocalDate.of(2023, 10, 29),
                playerId = "player1",
                eventId = 3L, // <-- CAMBIO CLAVE: eventId es Long?
                points = 21,
                assists = 7,
                rebounds = 3,
                steals = 1,
                blocks = 0,
                turnovers = 2,
                freeThrowsMade = 1,
                freeThrowsAttempted = 2,
                fouls = 0, twoPointersMade = 0, twoPointersAttempted = 0,
                threePointersMade = 0, threePointersAttempted = 0,
                minutesPlayed = 0, plusMinus = 0
            ),
            PerformanceSheet(
                id = 103L, // <-- CAMBIO CLAVE: ID es Long
                date = LocalDate.of(2023, 10, 25),
                playerId = "player1",
                eventId = null, // Puede ser null
                points = 18,
                assists = 5,
                rebounds = 7,
                steals = 1,
                blocks = 1,
                turnovers = 2,
                freeThrowsMade = 4,
                freeThrowsAttempted = 5,
                fouls = 0, twoPointersMade = 0, twoPointersAttempted = 0,
                threePointersMade = 0, threePointersAttempted = 0,
                minutesPlayed = 0, plusMinus = 0
            )
        )
    }

    // Buscar la ficha de rendimiento por el sheetId
    val performanceSheet = remember(sheetId) {
        dummyPerformanceSheetsData.firstOrNull { it.id == sheetId } ?: PerformanceSheet(
            id = -1L, // <-- Usar un Long para el ID de error
            date = LocalDate.now(),
            playerId = "error",
            eventId = null, // Opcional
            points = 0, assists = 0, rebounds = 0, steals = 0, blocks = 0,
            turnovers = 0, freeThrowsMade = 0, freeThrowsAttempted = 0,
            fouls = 0, twoPointersMade = 0, twoPointersAttempted = 0,
            threePointersMade = 0, threePointersAttempted = 0,
            minutesPlayed = 0, plusMinus = 0
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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
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
                                text = "Derivada del evento ID: ${performanceSheet.eventId}", // Muestra el Long directamente
                                style = MaterialTheme.typography.bodyMedium,
                                color = DarkText.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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