package com.example.baskstatsapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.example.baskstatsapp.model.PerformanceSheet
import com.example.baskstatsapp.viewmodel.EventViewModel
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState // Importar para collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddPerformanceSheetScreen(
    navController: NavController,
    eventId: Long?, // Puede venir un ID de evento si se añade desde la pantalla de detalle de evento
    performanceSheetViewModel: PerformanceSheetViewModel,
    eventViewModel: EventViewModel
) {
    val scope = rememberCoroutineScope()
    // Observar todos los eventos para el selector (si aplica)
    val allEvents by eventViewModel.allEvents.collectAsState()

    // Si viene un eventId, podemos precargar la fecha de la ficha con la fecha del evento
    val initialPerformanceSheet = remember(eventId) {
        if (eventId != null) {
            // No podemos hacer una llamada suspendida aquí directamente.
            // La fecha se establecerá por defecto en el formulario, o si el evento
            // es seleccionado en el dropdown, se podría usar su fecha.
            // Para un caso simple, no precargamos la fecha a menos que se vincule a un evento al seleccionar.
            // La PerformanceSheet inicial es nula para añadir.
            null
        } else {
            null
        }
    }

    PerformanceSheetForm(
        title = "Añadir Ficha de Rendimiento",
        initialPerformanceSheet = initialPerformanceSheet, // Nulo para añadir
        availableEvents = allEvents, // Pasamos la lista de eventos para el selector
        onSave = { newSheet ->
            scope.launch {
                // Si la ficha no tiene eventId, la fecha la cogerá del formulario (LocalDate.now() por defecto)
                // Si la ficha sí tiene eventId (porque se seleccionó en el dropdown),
                // asegúrate de que la fecha de la ficha sea la del evento si es tu regla de negocio.
                // Aquí el formulario ya establece la fecha.
                val sheetToSave = if (newSheet.eventId != null) {
                    // Si se seleccionó un evento, podemos asegurarnos de que la fecha de la ficha
                    // coincida con la fecha del evento (o dejar la que el usuario puso si prefieres)
                    val event = eventViewModel.getEventById(newSheet.eventId)
                    newSheet.copy(date = event?.dateTime?.toLocalDate() ?: newSheet.date)
                } else {
                    newSheet
                }
                performanceSheetViewModel.insertPerformanceSheet(sheetToSave)
                navController.popBackStack() // Vuelve a la pantalla anterior
            }
        },
        onCancel = {
            navController.popBackStack() // Vuelve a la pantalla anterior
        }
    )
}