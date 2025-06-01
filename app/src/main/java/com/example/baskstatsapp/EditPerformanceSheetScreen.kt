// app/src/main/java/com/example/baskstatsapp/EditPerformanceSheetScreen.kt
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import com.example.baskstatsapp.viewmodel.EventViewModel
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel
import com.example.baskstatsapp.viewmodel.PlayerViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditPerformanceSheetScreen(
    navController: NavController,
    sheetId: Long?, // El ID de la ficha a editar
    performanceSheetViewModel: PerformanceSheetViewModel,
    eventViewModel: EventViewModel,
    playerViewModel: PlayerViewModel
) {
    // Carga la ficha de rendimiento existente
    val existingSheet by performanceSheetViewModel.getPerformanceSheetById(sheetId ?: -1L).collectAsState(initial = null)

    // Estados para los campos de la ficha de rendimiento, inicializados con los valores existentes
    var points by remember(existingSheet) { mutableStateOf(existingSheet?.points?.toString() ?: "") }
    var assists by remember(existingSheet) { mutableStateOf(existingSheet?.assists?.toString() ?: "") }
    var offensiveRebounds by remember(existingSheet) { mutableStateOf(existingSheet?.offensiveRebounds?.toString() ?: "") }
    var defensiveRebounds by remember(existingSheet) { mutableStateOf(existingSheet?.defensiveRebounds?.toString() ?: "") }
    var steals by remember(existingSheet) { mutableStateOf(existingSheet?.steals?.toString() ?: "") }
    var blocks by remember(existingSheet) { mutableStateOf(existingSheet?.blocks?.toString() ?: "") }
    var turnovers by remember(existingSheet) { mutableStateOf(existingSheet?.turnovers?.toString() ?: "") }
    var fouls by remember(existingSheet) { mutableStateOf(existingSheet?.fouls?.toString() ?: "") }
    var twoPointersMade by remember(existingSheet) { mutableStateOf(existingSheet?.twoPointersMade?.toString() ?: "") }
    var twoPointersAttempted by remember(existingSheet) { mutableStateOf(existingSheet?.twoPointersAttempted?.toString() ?: "") }
    var threePointersMade by remember(existingSheet) { mutableStateOf(existingSheet?.threePointersMade?.toString() ?: "") }
    var threePointersAttempted by remember(existingSheet) { mutableStateOf(existingSheet?.threePointersAttempted?.toString() ?: "") }
    var freeThrowsMade by remember(existingSheet) { mutableStateOf(existingSheet?.freeThrowsMade?.toString() ?: "") }
    var freeThrowsAttempted by remember(existingSheet) { mutableStateOf(existingSheet?.freeThrowsAttempted?.toString() ?: "") }
    var minutesPlayed by remember(existingSheet) { mutableStateOf(existingSheet?.minutesPlayed?.toString() ?: "") }
    var plusMinus by remember(existingSheet) { mutableStateOf(existingSheet?.plusMinus?.toString() ?: "") }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Editar Ficha de Rendimiento",
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
            // Si la ficha no se encuentra, mostrar mensaje de error
            if (existingSheet == null && sheetId != null && sheetId != -1L) {
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
            } else if (existingSheet != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(LightGrayBackground)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Muestra el evento al que pertenece, pero no permite cambiarlo aquí
                    Text(
                        text = "Ficha del evento: ${existingSheet?.eventId ?: "Desconocido"}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkText.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Campos para las estadísticas (reutilizando StatInputField)
                    StatInputField(value = points, onValueChange = { points = it }, label = "Puntos")
                    StatInputField(value = assists, onValueChange = { assists = it }, label = "Asistencias")
                    // YA NO SE MUESTRA "Rebotes Totales" DIRECTAMENTE COMO CAMPO EDITABLE,
                    // SINO QUE SE EDITA POR OFENSIVOS Y DEFENSIVOS
                    // StatInputField(value = rebounds, onValueChange = { rebounds = it }, label = "Rebotes Totales")
                    StatInputField(value = offensiveRebounds, onValueChange = { offensiveRebounds = it }, label = "Rebotes Ofensivos")
                    StatInputField(value = defensiveRebounds, onValueChange = { defensiveRebounds = it }, label = "Rebotes Defensivos")
                    StatInputField(value = steals, onValueChange = { steals = it }, label = "Robos")
                    StatInputField(value = blocks, onValueChange = { blocks = it }, label = "Tapones")
                    StatInputField(value = turnovers, onValueChange = { turnovers = it }, label = "Pérdidas")
                    StatInputField(value = fouls, onValueChange = { fouls = it }, label = "Faltas")
                    StatInputField(value = twoPointersMade, onValueChange = { twoPointersMade = it }, label = "T2 Anotados")
                    StatInputField(value = twoPointersAttempted, onValueChange = { twoPointersAttempted = it }, label = "T2 Intentados")
                    StatInputField(value = threePointersMade, onValueChange = { threePointersMade = it }, label = "T3 Anotados")
                    StatInputField(value = threePointersAttempted, onValueChange = { threePointersAttempted = it }, label = "T3 Intentados")
                    StatInputField(value = freeThrowsMade, onValueChange = { freeThrowsMade = it }, label = "TL Anotados")
                    StatInputField(value = freeThrowsAttempted, onValueChange = { freeThrowsAttempted = it }, label = "TL Intentados")
                    StatInputField(value = minutesPlayed, onValueChange = { minutesPlayed = it }, label = "Minutos Jugados")
                    StatInputField(value = plusMinus, onValueChange = { plusMinus = it }, label = "+/-")


                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            existingSheet?.let { sheet ->
                                val updatedSheet = sheet.copy(
                                    points = points.toIntOrNull() ?: 0,
                                    assists = assists.toIntOrNull() ?: 0,
                                    // RECALCULAR REBOTES TOTALES AL GUARDAR
                                    // Ya no se pasa un 'rebounds' directo, se calcula
                                    // Esta línea no es necesaria si el modelo no tiene 'rebounds'
                                    // rebounds = (offensiveRebounds.toIntOrNull() ?: 0) + (defensiveRebounds.toIntOrNull() ?: 0),
                                    offensiveRebounds = offensiveRebounds.toIntOrNull() ?: 0,
                                    defensiveRebounds = defensiveRebounds.toIntOrNull() ?: 0,
                                    steals = steals.toIntOrNull() ?: 0,
                                    blocks = blocks.toIntOrNull() ?: 0,
                                    turnovers = turnovers.toIntOrNull() ?: 0,
                                    fouls = fouls.toIntOrNull() ?: 0,
                                    twoPointersMade = twoPointersMade.toIntOrNull() ?: 0,
                                    twoPointersAttempted = twoPointersAttempted.toIntOrNull() ?: 0,
                                    threePointersMade = threePointersMade.toIntOrNull() ?: 0,
                                    threePointersAttempted = threePointersAttempted.toIntOrNull() ?: 0,
                                    freeThrowsMade = freeThrowsMade.toIntOrNull() ?: 0,
                                    freeThrowsAttempted = freeThrowsAttempted.toIntOrNull() ?: 0,
                                    minutesPlayed = minutesPlayed.toIntOrNull() ?: 0,
                                    plusMinus = plusMinus.toIntOrNull() ?: 0
                                )
                                coroutineScope.launch {
                                    performanceSheetViewModel.updatePerformanceSheet(updatedSheet)
                                    navController.popBackStack() // Volver a la pantalla de detalle
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                    ) {
                        Text("Guardar Cambios", color = Color.White)
                    }
                }
            }
        }
    )
}