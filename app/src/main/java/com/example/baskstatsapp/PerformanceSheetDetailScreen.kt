// app/src/main/java/com/example/baskstatsapp/PerformanceSheetDetailScreen.kt
package com.example.baskstatsapp

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.baskstatsapp.model.PerformanceSheet
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel
import com.example.baskstatsapp.viewmodel.EventViewModel
import com.example.baskstatsapp.viewmodel.PlayerViewModel // Necesario para el preview si lo usas
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.flow.flowOf

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceSheetDetailScreen(
    navController: NavController,
    performanceSheetViewModel: PerformanceSheetViewModel,
    eventViewModel: EventViewModel,
    sheetId: Long?
) {
    val sheetFlow = if (sheetId != null && sheetId != -1L) {
        performanceSheetViewModel.getPerformanceSheetById(sheetId)
    } else {
        flowOf(null)
    }
    val performanceSheet by sheetFlow.collectAsState(initial = null)

    val associatedEvent by remember(performanceSheet) {
        if (performanceSheet?.eventId != null) {
            eventViewModel.getEventById(performanceSheet!!.eventId!!)
        } else {
            flowOf(null)
        }
    }.collectAsState(initial = null)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Detalles de Ficha",
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
                onClick = { navController.navigate("edit_performance_sheet_screen/${sheetId}") },
                containerColor = PrimaryOrange,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Edit, "Editar Ficha")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightGrayBackground)
                    .padding(16.dp)
            ) {
                if (performanceSheet == null) {
                    Text("Cargando ficha o ficha no encontrada...", color = DarkText.copy(alpha = 0.7f))
                } else {
                    Text(text = "Fecha: ${performanceSheet!!.date}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Puntos: ${performanceSheet!!.points}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Asistencias: ${performanceSheet!!.assists}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Rebotes Ofensivos: ${performanceSheet!!.offensiveRebounds}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Rebotes Defensivos: ${performanceSheet!!.defensiveRebounds}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Robos: ${performanceSheet!!.steals}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Tapones: ${performanceSheet!!.blocks}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Pérdidas: ${performanceSheet!!.turnovers}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Faltas: ${performanceSheet!!.fouls}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "T2M/A: ${performanceSheet!!.twoPointersMade}/${performanceSheet!!.twoPointersAttempted}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "T3M/A: ${performanceSheet!!.threePointersMade}/${performanceSheet!!.threePointersAttempted}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "TLM/A: ${performanceSheet!!.freeThrowsMade}/${performanceSheet!!.freeThrowsAttempted}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Minutos Jugados: ${performanceSheet!!.minutesPlayed}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Plus/Minus: ${performanceSheet!!.plusMinus}", style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Evento asociado:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    if (associatedEvent != null) {
                        Text("Tipo de Evento: ${associatedEvent!!.type}", style = MaterialTheme.typography.bodyLarge)
                        associatedEvent!!.opponent?.let { Text("vs. $it", style = MaterialTheme.typography.bodyLarge) }
                        Text("Fecha: ${associatedEvent!!.dateTime.toLocalDate()}", style = MaterialTheme.typography.bodyLarge)
                    } else {
                        Text("No hay evento asociado o el evento no se encontró.", color = DarkText.copy(alpha = 0.7f))
                    }
                }
            }
        }
    )
}
