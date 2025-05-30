package com.example.baskstatsapp

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete // <-- Importación para el icono de borrar
import androidx.compose.material.icons.filled.Edit // <-- Importación para el icono de editar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf // <-- Importación para el estado del diálogo
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue // <-- Importación para el estado del diálogo
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.flowOf // Para el preview
import kotlinx.coroutines.launch // <-- Importación para la corrutina

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceSheetDetailScreen(
    navController: NavController,
    sheetId: Long?, // El ID de la ficha de rendimiento se pasa como argumento
    performanceSheetViewModel: PerformanceSheetViewModel
) {
    // Observa la ficha de rendimiento específica por su ID
    val performanceSheet by performanceSheetViewModel.getPerformanceSheetById(sheetId ?: -1L).collectAsState(initial = null)

    // Añadir estado para mostrar el diálogo de confirmación de borrado
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope() // Para lanzar corrutinas de eliminación

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = performanceSheet?.let { "Ficha de Rendimiento: ${it.date.format(DateTimeFormatter.ofLocalizedDate(java.time.format.FormatStyle.MEDIUM))}" }
                            ?: "Detalles de Ficha",
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
                    .verticalScroll(rememberScrollState()) // Permite el scroll para muchas stats
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                performanceSheet?.let { sheet ->
                    PerformanceSheetDetailsCard(sheet)

                    // --- ¡AÑADIDA SECCIÓN DE BOTONES DE ACCIÓN! ---
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                // Navega a la pantalla de edición, pasando el ID de la ficha
                                navController.navigate("edit_performance_sheet_screen/${sheet.id}")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar Ficha", tint = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text("Editar", color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { showDeleteConfirmationDialog = true }, // Muestra el diálogo de confirmación
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error), // Color de error para borrar
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar Ficha", tint = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text("Eliminar", color = Color.White)
                        }
                    }

                    // --- DIÁLOGO DE CONFIRMACIÓN DE ELIMINACIÓN ---
                    if (showDeleteConfirmationDialog) {
                        AlertDialog(
                            onDismissRequest = { showDeleteConfirmationDialog = false },
                            title = { Text("Confirmar Eliminación") },
                            text = { Text("¿Estás seguro de que quieres eliminar esta ficha de rendimiento? Esta acción no se puede deshacer.") },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            performanceSheetViewModel.deletePerformanceSheet(sheet)
                                            showDeleteConfirmationDialog = false
                                            navController.popBackStack() // Volver a la pantalla anterior
                                        }
                                    }
                                ) {
                                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDeleteConfirmationDialog = false }) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }
                    // --- FIN SECCIÓN DE BOTONES DE ACCIÓN Y DIÁLOGO ---

                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ficha de rendimiento no encontrada.",
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            color = DarkText.copy(alpha = 0.7f),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PerformanceSheetDetailsCard(sheet: PerformanceSheet) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Fecha: ${sheet.date.format(DateTimeFormatter.ofLocalizedDate(java.time.format.FormatStyle.FULL))}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryOrange,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Usamos un Column para las estadísticas para mejor organización
            Spacer(modifier = Modifier.height(8.dp)) // Reducido de 24.dp para que no haya tanto espacio en blanco.
            StatRow("Puntos", sheet.points)
            StatRow("Asistencias", sheet.assists)
            StatRow("Rebotes Totales", sheet.rebounds)
            StatRow("Rebotes Ofensivos", sheet.offensiveRebounds)
            StatRow("Rebotes Defensivos", sheet.defensiveRebounds)
            StatRow("Robos", sheet.steals)
            StatRow("Tapones", sheet.blocks)
            StatRow("Pérdidas", sheet.turnovers)
            StatRow("Faltas", sheet.fouls)
            StatRow("Tiros de 2 Anotados", sheet.twoPointersMade)
            StatRow("Tiros de 2 Intentados", sheet.twoPointersAttempted)
            StatRow("Tiros de 3 Anotados", sheet.threePointersMade)
            StatRow("Tiros de 3 Intentados", sheet.threePointersAttempted)
            StatRow("Tiros Libres Anotados", sheet.freeThrowsMade)
            StatRow("Tiros Libres Intentados", sheet.freeThrowsAttempted)
            StatRow("Minutos Jugados", sheet.minutesPlayed)
            StatRow("Plus/Minus", sheet.plusMinus)

            // Puedes añadir más cálculos aquí si lo deseas, como porcentajes de tiro
            Spacer(modifier = Modifier.height(16.dp))
            if (sheet.twoPointersAttempted > 0) {
                val percentage = (sheet.twoPointersMade.toDouble() / sheet.twoPointersAttempted) * 100
                Text(
                    text = "Porcentaje T2: ${"%.1f".format(percentage)}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkText.copy(alpha = 0.8f)
                )
            }
            if (sheet.threePointersAttempted > 0) {
                val percentage = (sheet.threePointersMade.toDouble() / sheet.threePointersAttempted) * 100
                Text(
                    text = "Porcentaje T3: ${"%.1f".format(percentage)}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkText.copy(alpha = 0.8f)
                )
            }
            if (sheet.freeThrowsAttempted > 0) {
                val percentage = (sheet.freeThrowsMade.toDouble() / sheet.freeThrowsAttempted) * 100
                Text(
                    text = "Porcentaje TL: ${"%.1f".format(percentage)}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkText.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = DarkText
        )
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = PrimaryOrange
        )
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PreviewPerformanceSheetDetailScreen() {
    BaskStatsAppTheme {
        val mockPerformanceSheetDao = object : com.example.baskstatsapp.dao.PerformanceSheetDao {
            // Implementa los métodos del DAO para el Preview
            override suspend fun insert(sheet: PerformanceSheet): Long = 1L
            override suspend fun update(sheet: PerformanceSheet) {} // Implementa para el preview
            override suspend fun delete(sheet: PerformanceSheet) {}
            override fun getAllPerformanceSheets(): kotlinx.coroutines.flow.Flow<List<PerformanceSheet>> = flowOf(emptyList())
            override fun getPerformanceSheetById(sheetId: Long): kotlinx.coroutines.flow.Flow<PerformanceSheet?> = flowOf(
                PerformanceSheet(
                    id = 1L,
                    date = LocalDate.now(),
                    playerId = 1L,
                    eventId = 1L,
                    points = 25, assists = 5, rebounds = 10, offensiveRebounds = 2, defensiveRebounds = 8,
                    steals = 3, blocks = 1, turnovers = 2, fouls = 3, twoPointersMade = 8,
                    twoPointersAttempted = 15, threePointersMade = 3, threePointersAttempted = 6,
                    freeThrowsMade = 2, freeThrowsAttempted = 2, minutesPlayed = 35, plusMinus = 15
                )
            )
            override fun getPerformanceSheetsForEvent(eventId: Long): kotlinx.coroutines.flow.Flow<List<PerformanceSheet>> = flowOf(emptyList())
            override fun getPerformanceSheetsForPlayer(playerId: Long): kotlinx.coroutines.flow.Flow<List<PerformanceSheet>> = flowOf(emptyList())
            override fun getPerformanceSheetsForPlayerAndEvent(playerId: Long, eventId: Long): kotlinx.coroutines.flow.Flow<PerformanceSheet?> = flowOf(null)
        }
        val mockPerformanceSheetViewModel = PerformanceSheetViewModel(mockPerformanceSheetDao)

        PerformanceSheetDetailScreen(
            navController = rememberNavController(),
            sheetId = 1L, // ID de ficha de rendimiento de prueba
            performanceSheetViewModel = mockPerformanceSheetViewModel
        )
    }
}