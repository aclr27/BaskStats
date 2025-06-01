// app/src/main/java/com/example/baskstatsapp/PerformanceSheetsScreen.kt
package com.example.baskstatsapp

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.baskstatsapp.model.PerformanceSheet
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.Instant // Importar Instant
import java.time.ZoneId // Importar ZoneId
import kotlinx.coroutines.flow.flowOf

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceSheetsScreen(
    navController: NavController,
    performanceSheetViewModel: PerformanceSheetViewModel
) {
    // Filtramos las fichas de rendimiento por el ID del jugador logueado
    val performanceSheets by performanceSheetViewModel.getPerformanceSheetsForPlayer(
        MainActivity.currentLoggedInPlayerId ?: -1L
    ).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Mis Fichas de Rendimiento",
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
                onClick = { navController.navigate("add_performance_sheet_screen") },
                containerColor = PrimaryOrange,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "Añadir nueva ficha")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightGrayBackground)
            ) {
                if (performanceSheets.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No tienes fichas de rendimiento registradas.\nPulsa el botón '+' para añadir una.",
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
                        items(performanceSheets) { sheet ->
                            PerformanceSheetCard(sheet = sheet) {
                                // CORRECCIÓN: Usar sheet.sheetId en lugar de sheet.id
                                navController.navigate("performance_sheet_detail_screen/${sheet.sheetId}")
                            }
                        }
                    }
                }
            }
        }
    )
}

// Esta PerformanceSheetCard es local para este archivo.
// Considera moverla a un archivo de composables para reutilización.
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PerformanceSheetCard(sheet: PerformanceSheet, onClick: () -> Unit) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd MMM yyyy") } // Formato de fecha

    // CORRECCIÓN: Convertir sheet.eventDate (Long) a LocalDate para formatear
    val sheetLocalDate = remember(sheet.eventDate) {
        Instant.ofEpochMilli(sheet.eventDate)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

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
            // CORRECCIÓN: Usar sheetLocalDate formateado
            Text(
                text = "Ficha del ${sheetLocalDate.format(dateFormatter)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryOrange
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Puntos: ${sheet.points}, Asistencias: ${sheet.assists}, Rebotes: ${sheet.offensiveRebounds + sheet.defensiveRebounds}",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkText
            )
            Text(
                text = "Robos: ${sheet.steals}, Tapones: ${sheet.blocks}, Pérdidas: ${sheet.turnovers}",
                style = MaterialTheme.typography.bodySmall,
                color = DarkText.copy(alpha = 0.7f)
            )
            // Puedes añadir más detalles aquí según sea relevante
        }
    }
}