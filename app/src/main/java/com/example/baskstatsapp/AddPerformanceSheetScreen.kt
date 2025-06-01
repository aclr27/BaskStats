// app/src/main/java/com/example/baskstatsapp/AddPerformanceSheetScreen.kt
package com.example.baskstatsapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.baskstatsapp.dao.EventDao
import com.example.baskstatsapp.dao.PerformanceSheetDao
import com.example.baskstatsapp.dao.PlayerDao
import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.PerformanceSheet
import com.example.baskstatsapp.model.EventType
import com.example.baskstatsapp.model.Player
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.viewmodel.EventViewModel
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel
import com.example.baskstatsapp.viewmodel.PlayerViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddPerformanceSheetScreen(
    navController: NavController,
    performanceSheetViewModel: PerformanceSheetViewModel,
    eventViewModel: EventViewModel,
    playerViewModel: PlayerViewModel,
    associatedEventId: Long? = null // Este parámetro ya estaba aquí, lo mantengo.
) {
    // El ID del jugador logueado ya se establece en el EventViewModel desde MainActivity.
    // Ahora observamos el 'playerEvents' StateFlow del EventViewModel.
    val availableEvents by eventViewModel.playerEvents.collectAsState(initial = emptyList()) // <-- ¡CORREGIDO AQUÍ!

    val loggedInPlayerId = MainActivity.currentLoggedInPlayerId // Todavía necesario para asignar a la ficha

    PerformanceSheetForm(
        initialPerformanceSheet = null,
        onSave = { newSheet ->
            if (loggedInPlayerId != null) {
                val sheetToSave = newSheet.copy(playerId = loggedInPlayerId)
                performanceSheetViewModel.viewModelScope.launch {
                    performanceSheetViewModel.addPerformanceSheet(sheetToSave)
                    navController.popBackStack()
                }
            } else {
                // Aquí podrías mostrar un Toast o un mensaje de error al usuario
                // Toast.makeText(context, "No hay jugador logueado para guardar la ficha.", Toast.LENGTH_SHORT).show()
            }
        },
        onCancel = { navController.popBackStack() },
        title = "Añadir Ficha de Rendimiento",
        availableEvents = availableEvents,
        initialSelectedEventId = associatedEventId // <-- ¡Paso el parámetro aquí!
    )
}