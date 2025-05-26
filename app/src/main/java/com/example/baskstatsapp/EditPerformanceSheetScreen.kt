package com.example.baskstatsapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.baskstatsapp.model.PerformanceSheet
import com.example.baskstatsapp.viewmodel.EventViewModel
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState // Importar para collectAsState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditPerformanceSheetScreen(
    navController: NavController,
    sheetId: Long?, // El ID de la ficha a editar
    performanceSheetViewModel: PerformanceSheetViewModel,
    eventViewModel: EventViewModel
) {
    val scope = rememberCoroutineScope()
    var performanceSheetToEdit by remember { mutableStateOf<PerformanceSheet?>(null) }
    val allEvents by eventViewModel.allEvents.collectAsState() // Para el selector de eventos

    // Cargar la ficha de rendimiento una vez al entrar en la pantalla
    LaunchedEffect(sheetId) {
        if (sheetId != null) {
            performanceSheetToEdit = performanceSheetViewModel.getPerformanceSheetById(sheetId)
        }
    }

    if (performanceSheetToEdit == null && sheetId != null) {
    } else {
        PerformanceSheetForm(
            title = "Editar Ficha de Rendimiento",
            initialPerformanceSheet = performanceSheetToEdit, // Pasa la ficha cargada para precargar el formulario
            availableEvents = allEvents, // Pasa la lista de eventos
            onSave = { updatedSheet ->
                scope.launch {
                    // Asegúrate de que la fecha de la ficha se mantiene si se vincula a un evento
                    val sheetToSave = if (updatedSheet.eventId != null) {
                        val event = eventViewModel.getEventById(updatedSheet.eventId)
                        updatedSheet.copy(date = event?.dateTime?.toLocalDate() ?: updatedSheet.date)
                    } else {
                        updatedSheet
                    }
                    performanceSheetViewModel.updatePerformanceSheet(sheetToSave)
                    navController.popBackStack() // Vuelve a la pantalla anterior
                }
            },
            onCancel = {
                navController.popBackStack() // Vuelve a la pantalla anterior
            }
        )
    }
}

