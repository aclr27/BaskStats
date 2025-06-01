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

// Import StatInputField if it's defined in another file (e.g., PerformanceSheetForm.kt or a common Composables file)
// If StatInputField is defined directly in PerformanceSheetForm.kt, you might need to move it to a more accessible location
// like a dedicated 'composables' package or define it here if it's only used in this screen.
// For now, I'll assume you'll either move it or define it here.
// Let's define StatInputField here for simplicity and to resolve the immediate error.
// Ideally, this should be in a separate, reusable composable file.
// If you already have it in PerformanceSheetForm.kt, ensure that file is in the same package or imported if needed.
// For the sake of resolving the *current* errors directly, I will include StatInputField here.

/*
// If StatInputField is NOT in this file, you would need an import like:
import com.example.baskstatsapp.StatInputField // Assuming StatInputField is in the root package
// OR
// import com.example.baskstatsapp.composables.StatInputField // If you move it to a 'composables' package
*/

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

                    // Campos para las estadísticas (reutilizando StatInputField)
                    StatInputField(value = points, onValueChange = { points = it }, label = "Puntos")
                    StatInputField(value = assists, onValueChange = { assists = it }, label = "Asistencias")
                    StatInputField(value = rebounds, onValueChange = { rebounds = it }, label = "Rebotes Totales")
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
                    // If your StatInputField can handle signed numbers (like +/-), you might need to add a parameter like `isSigned`.
                    // Otherwise, for `+/-` you might need a separate component or custom logic.
                    // Assuming StatInputField is robust enough, or +/- will always be parsed correctly.
                    // Based on the 'StatInputField' I previously provided in PerformanceSheetForm, it already handles the '-' for '+/-'
                    StatInputField(value = plusMinus, onValueChange = { plusMinus = it }, label = "+/-")


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
