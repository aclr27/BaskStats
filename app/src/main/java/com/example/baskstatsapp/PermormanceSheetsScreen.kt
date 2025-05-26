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

    val samplePerformanceSheets = remember {
        listOf(
            PerformanceSheet(
                id = 101L, // <-- CAMBIO CLAVE: ID como Long
                date = LocalDate.of(2023, 11, 2),
                playerId = "player1",
                eventId = 1L, // <-- CAMBIO CLAVE: eventId como Long
                points = 25,
                assists = 8,
                rebounds = 5,
                steals = 2,
                blocks = 1,
                turnovers = 3,
                freeThrowsMade = 2,
                freeThrowsAttempted = 2,
                // Asegúrate de inicializar todos los campos adicionales de PerformanceSheet
                fouls = 0, twoPointersMade = 0, twoPointersAttempted = 0,
                threePointersMade = 0, threePointersAttempted = 0,
                minutesPlayed = 0, plusMinus = 0
            ),
            PerformanceSheet(
                id = 102L, // <-- CAMBIO CLAVE: ID como Long
                date = LocalDate.of(2023, 10, 29),
                playerId = "player1",
                eventId = 3L, // <-- CAMBIO CLAVE: eventId como Long
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
                id = 103L, // <-- CAMBIO CLAVE: ID como Long
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
            ),
            PerformanceSheet(
                id = 104L, // <-- CAMBIO CLAVE: ID como Long
                date = LocalDate.of(2023, 10, 20),
                playerId = "player1",
                eventId = 5L, // <-- CAMBIO CLAVE: eventId como Long
                points = 10,
                assists = 2,
                rebounds = 3,
                steals = 0,
                blocks = 0,
                turnovers = 1,
                freeThrowsMade = 0,
                freeThrowsAttempted = 0,
                fouls = 0, twoPointersMade = 0, twoPointersAttempted = 0,
                threePointersMade = 0, threePointersAttempted = 0,
                minutesPlayed = 0, plusMinus = 0
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightGrayBackground)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val playerNameForCards = "Tu Jugador"

                items(samplePerformanceSheets) { sheet ->
                    PerformanceItemCard(
                        performanceSheet = sheet,
                        playerName = playerNameForCards,
                        modifier = Modifier.fillMaxWidth().clickable {
                            // Ahora sheet.id es un Long, lo que coincide con la ruta en MainActivity.kt
                            navController.navigate("performance_sheet_detail_screen/${sheet.id}")
                            println("Navegando al detalle de la ficha: ${sheet.id}")
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    )
}