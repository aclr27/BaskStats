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
import com.example.baskstatsapp.model.PerformanceSheet // Importa el modelo PerformanceSheet
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceSheetsScreen(navController: NavController) {

    // Datos de prueba (Mock Data) para las fichas de rendimiento.
    // En una aplicación real, esto provendría de un ViewModel que cargaría datos.
    val samplePerformanceSheets = remember {
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
            ),
            PerformanceSheet(
                id = "ps4",
                date = LocalDate.of(2023, 10, 20),
                playerId = "player1",
                eventId = "e5",
                points = 10,
                assists = 2,
                rebounds = 3,
                steals = 0,
                blocks = 0,
                turnovers = 1,
                freeThrowsMade = 0,
                freeThrowsAttempted = 0
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Todas las Fichas",
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) { // Botón de retroceso
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightGrayBackground)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Asumimos un nombre de jugador para la tarjeta, en un futuro vendría del estado de la app
                val playerNameForCards = "Tu Jugador" // Podría venir de un ViewModel o un estado de autenticación

                items(samplePerformanceSheets) { sheet ->
                    // Reutilizamos la composable PerformanceItemCard de HomeScreen
                    // Si clicamos en una tarjeta, navegamos a la pantalla de detalle de la ficha
                    // TODO: Pasar el ID de la ficha para la pantalla de detalle
                    PerformanceItemCard(
                        performanceSheet = sheet,
                        playerName = playerNameForCards,
                        modifier = Modifier.fillMaxWidth().clickable {
                            // Implementar navegación a PerformanceSheetDetailScreen
                            // navController.navigate("performance_sheet_detail_screen/${sheet.id}")
                            println("Navegando al detalle de la ficha: ${sheet.id}")
                        }
                    )
                }
            }
        }
    )
}

