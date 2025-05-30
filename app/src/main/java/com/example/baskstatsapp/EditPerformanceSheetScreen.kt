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
import com.example.baskstatsapp.model.PerformanceSheet
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel
import java.time.LocalDate
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPerformanceSheetScreen(
    navController: NavController,
    sheetId: Long?, // El ID de la ficha a editar
    performanceSheetViewModel: PerformanceSheetViewModel
) {
    // Carga la ficha de rendimiento existente
    val existingSheet by performanceSheetViewModel.getPerformanceSheetById(sheetId ?: -1L).collectAsState(initial = null)

    // Estados para los campos de la ficha de rendimiento, inicializados con los valores existentes
    var points by remember(existingSheet) { mutableStateOf(existingSheet?.points?.toString() ?: "") }
    var assists by remember(existingSheet) { mutableStateOf(existingSheet?.assists?.toString() ?: "") }
    var rebounds by remember(existingSheet) { mutableStateOf(existingSheet?.rebounds?.toString() ?: "") }
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

                    // Campos para las estadísticas (reutilizando NumericInputField)
                    NumericInputField(value = points, onValueChange = { points = it }, label = "Puntos")
                    NumericInputField(value = assists, onValueChange = { assists = it }, label = "Asistencias")
                    NumericInputField(value = rebounds, onValueChange = { rebounds = it }, label = "Rebotes Totales")
                    NumericInputField(value = offensiveRebounds, onValueChange = { offensiveRebounds = it }, label = "Rebotes Ofensivos")
                    NumericInputField(value = defensiveRebounds, onValueChange = { defensiveRebounds = it }, label = "Rebotes Defensivos")
                    NumericInputField(value = steals, onValueChange = { steals = it }, label = "Robos")
                    NumericInputField(value = blocks, onValueChange = { blocks = it }, label = "Tapones")
                    NumericInputField(value = turnovers, onValueChange = { turnovers = it }, label = "Pérdidas")
                    NumericInputField(value = fouls, onValueChange = { fouls = it }, label = "Faltas")
                    NumericInputField(value = twoPointersMade, onValueChange = { twoPointersMade = it }, label = "T2 Anotados")
                    NumericInputField(value = twoPointersAttempted, onValueChange = { twoPointersAttempted = it }, label = "T2 Intentados")
                    NumericInputField(value = threePointersMade, onValueChange = { threePointersMade = it }, label = "T3 Anotados")
                    NumericInputField(value = threePointersAttempted, onValueChange = { threePointersAttempted = it }, label = "T3 Intentados")
                    NumericInputField(value = freeThrowsMade, onValueChange = { freeThrowsMade = it }, label = "TL Anotados")
                    NumericInputField(value = freeThrowsAttempted, onValueChange = { freeThrowsAttempted = it }, label = "TL Intentados")
                    NumericInputField(value = minutesPlayed, onValueChange = { minutesPlayed = it }, label = "Minutos Jugados")
                    NumericInputField(value = plusMinus, onValueChange = { plusMinus = it }, label = "+/-", isSigned = true)


                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            existingSheet?.let { sheet ->
                                val updatedSheet = sheet.copy(
                                    points = points.toIntOrNull() ?: 0,
                                    assists = assists.toIntOrNull() ?: 0,
                                    rebounds = rebounds.toIntOrNull() ?: 0,
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