// app/src/main/java/com/example/baskstatsapp/viewmodel/PerformanceSheetViewModel.kt
package com.example.baskstatsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baskstatsapp.dao.PerformanceSheetDao
import com.example.baskstatsapp.model.PerformanceSheet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import com.example.baskstatsapp.model.PlayerStatsSummary
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn // <--- ¡AÑADIDA ESTA LÍNEA!
import android.util.Log // Para depuración

class PerformanceSheetViewModel(private val performanceSheetDao: PerformanceSheetDao) : ViewModel() {

    private val _selectedPlayerId = MutableStateFlow<Long?>(null)
    val selectedPlayerId: StateFlow<Long?> = _selectedPlayerId

    fun setSelectedPlayerId(playerId: Long?) {
        _selectedPlayerId.value = playerId
    }

    // `allPerformanceSheets` ahora reacciona a `_selectedPlayerId`
    val allPerformanceSheets: StateFlow<List<PerformanceSheet>> = _selectedPlayerId
        .flatMapLatest { playerId ->
            if (playerId != null && playerId > 0) { // Solo si hay un playerId válido
                performanceSheetDao.getPerformanceSheetsForPlayer(playerId)
            } else {
                flowOf(emptyList()) // Si no hay jugador seleccionado, emite una lista vacía
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    // ¡NUEVO! Flow para obtener las últimas N fichas de rendimiento (para la sección "Últimas Fichas")
    fun getLastNPerformanceSheets(n: Int): StateFlow<List<PerformanceSheet>> = _selectedPlayerId
        .flatMapLatest { playerId ->
            if (playerId != null && playerId > 0) {
                performanceSheetDao.getLastNPerformanceSheetsForPlayer(playerId, n)
            } else {
                flowOf(emptyList()) // Si no hay jugador seleccionado, emite una lista vacía
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // ¡NUEVO! Flow para obtener las estadísticas totales del jugador (suma de todas las fichas)
    val totalPlayerStats: StateFlow<Map<String, Int>> = allPerformanceSheets
        .map { sheets ->
            if (sheets.isEmpty()) {
                mapOf("Puntos" to 0, "Asistencias" to 0, "Rebotes" to 0) // Valores por defecto
            } else {
                val totalPoints = sheets.sumOf { it.points }
                val totalAssists = sheets.sumOf { it.assists }
                val totalRebounds = sheets.sumOf { it.defensiveRebounds + it.offensiveRebounds }
                // Añade otras estadísticas aquí si es necesario
                mapOf(
                    "Puntos" to totalPoints,
                    "Asistencias" to totalAssists,
                    "Rebotes" to totalRebounds
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = mapOf("Puntos" to 0, "Asistencias" to 0, "Rebotes" to 0)
        )

    suspend fun addPerformanceSheet(sheet: PerformanceSheet): Long {
        return performanceSheetDao.insert(sheet)
    }

    fun getPerformanceSheetById(sheetId: Long): Flow<PerformanceSheet?> {
        return performanceSheetDao.getPerformanceSheetById(sheetId)
    }

    fun getPerformanceSheetsForEvent(eventId: Long): Flow<List<PerformanceSheet>> {
        return performanceSheetDao.getPerformanceSheetsForEvent(eventId)
    }

    // Esta función está bien, pero ahora `allPerformanceSheets` también la usa internamente.
    // Puedes mantenerla si la usas directamente en otros lugares.
    fun getPerformanceSheetsForPlayer(playerId: Long): Flow<List<PerformanceSheet>> {
        return performanceSheetDao.getPerformanceSheetsForPlayer(playerId)
    }

    fun getPerformanceSheetsForPlayerAndEvent(playerId: Long, eventId: Long): Flow<PerformanceSheet?> {
        return performanceSheetDao.getPerformanceSheetForEventAndPlayer(playerId, eventId)
    }

    fun updatePerformanceSheet(sheet: PerformanceSheet) {
        viewModelScope.launch {
            performanceSheetDao.update(sheet)
        }
    }

    fun deletePerformanceSheet(sheet: PerformanceSheet) {
        viewModelScope.launch {
            performanceSheetDao.delete(sheet)
        }
    }

    fun getPlayerStatsSummary(playerId: Long): Flow<PlayerStatsSummary> {
        return combine(
            performanceSheetDao.getTotalGamesPlayed(playerId),
            performanceSheetDao.getTotalPoints(playerId),
            performanceSheetDao.getTotalAssists(playerId),
            performanceSheetDao.getTotalRebounds(playerId),
            performanceSheetDao.getTotalSteals(playerId),
            performanceSheetDao.getTotalBlocks(playerId),
            performanceSheetDao.getTotalTurnovers(playerId),
            performanceSheetDao.getAvgPoints(playerId),
            performanceSheetDao.getAvgAssists(playerId),
            performanceSheetDao.getAvgRebounds(playerId),
            performanceSheetDao.getAvgSteals(playerId),
            performanceSheetDao.getAvgBlocks(playerId),
            performanceSheetDao.getAvgTurnovers(playerId),
            performanceSheetDao.getTotalTwoPointersMade(playerId),
            performanceSheetDao.getTotalTwoPointersAttempted(playerId),
            performanceSheetDao.getTotalThreePointersMade(playerId),
            performanceSheetDao.getTotalThreePointersAttempted(playerId),
            performanceSheetDao.getTotalFreeThrowsMade(playerId),
            performanceSheetDao.getTotalFreeThrowsAttempted(playerId),
            performanceSheetDao.getTotalFouls(playerId),
            performanceSheetDao.getTotalMinutesPlayed(playerId),
            performanceSheetDao.getTotalPlusMinus(playerId),
            performanceSheetDao.getTwoPointersPercentage(playerId),
            performanceSheetDao.getThreePointersPercentage(playerId),
            performanceSheetDao.getFreeThrowsPercentage(playerId)
        ) { results ->
            PlayerStatsSummary(
                totalGamesPlayed = results[0] as Int,
                totalPoints = (results[1] as Int?) ?: 0,
                totalAssists = (results[2] as Int?) ?: 0,
                totalRebounds = (results[3] as Int?) ?: 0,
                totalSteals = (results[4] as Int?) ?: 0,
                totalBlocks = (results[5] as Int?) ?: 0,
                totalTurnovers = (results[6] as Int?) ?: 0,
                avgPoints = (results[7] as Double?)?.format(2) ?: 0.0,
                avgAssists = (results[8] as Double?)?.format(2) ?: 0.0,
                avgRebounds = (results[9] as Double?)?.format(2) ?: 0.0,
                avgSteals = (results[10] as Double?)?.format(2) ?: 0.0,
                avgBlocks = (results[11] as Double?)?.format(2) ?: 0.0,
                avgTurnovers = (results[12] as Double?)?.format(2) ?: 0.0,
                totalTwoPointersMade = (results[13] as Int?) ?: 0,
                totalTwoPointersAttempted = (results[14] as Int?) ?: 0,
                totalThreePointersMade = (results[15] as Int?) ?: 0,
                totalThreePointersAttempted = (results[16] as Int?) ?: 0,
                totalFreeThrowsMade = (results[17] as Int?) ?: 0,
                totalFreeThrowsAttempted = (results[18] as Int?) ?: 0,
                totalFouls = (results[19] as Int?) ?: 0,
                totalMinutesPlayed = (results[20] as Int?) ?: 0,
                totalPlusMinus = (results[21] as Int?) ?: 0,
                twoPointersPercentage = (results[22] as Double?)?.format(2) ?: 0.0,
                threePointersPercentage = (results[23] as Double?)?.format(2) ?: 0.0,
                freeThrowsPercentage = (results[24] as Double?)?.format(2) ?: 0.0
            )
        }
    }
    private fun Double.format(digits: Int) = "%.${digits}f".format(this).replace(",", ".").toDouble()

    fun getStatDataForChart(statType: String): StateFlow<List<Pair<Float, Float>>> {
        return allPerformanceSheets.map { sheets ->
            if (sheets.isEmpty()) {
                emptyList()
            } else {
                // Ordenar por fecha para que el gráfico tenga sentido temporal
                sheets.sortedBy { it.eventDate } // ASEGÚRATE de que 'date' en PerformanceSheet es comparable
                    .mapIndexed { index, sheet ->
                        val xValue = (index + 1).toFloat() // Eje X: número de ficha (1, 2, 3...)
                        val yValue = when (statType) {
                            "Puntos" -> sheet.points.toFloat()
                            "Asistencias" -> sheet.assists.toFloat()
                            "Rebotes" -> (sheet.defensiveRebounds + sheet.offensiveRebounds).toFloat() // Suma ambos rebotes
                            "Robos" -> sheet.steals.toFloat()
                            "Tapones" -> sheet.blocks.toFloat()
                            "Faltas" -> sheet.fouls.toFloat()
                            // Añade más casos si tienes más estadísticas en PerformanceSheet
                            else -> 0f // Valor por defecto si la estadística no se reconoce
                        }
                        xValue to yValue
                    }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    // Nuevo Flow para calcular las estadísticas totales del jugador
    val totalPlayerStats: StateFlow<Map<String, Int>> = allPerformanceSheets.map { sheets ->
        if (sheets.isEmpty()) {
            mapOf(
                "totalPoints" to 0,
                "totalAssists" to 0,
                "totalRebounds" to 0,
                "totalSteals" to 0,
                "totalBlocks" to 0,
                "totalFouls" to 0
                // Añade más si tienes más estadísticas
            )
        } else {
            val totalPoints = sheets.sumOf { it.points }
            val totalAssists = sheets.sumOf { it.assists }
            val totalRebounds = sheets.sumOf { it.defensiveRebounds + it.offensiveRebounds }
            val totalSteals = sheets.sumOf { it.steals }
            val totalBlocks = sheets.sumOf { it.blocks }
            val totalFouls = sheets.sumOf { it.fouls }

            mapOf(
                "totalPoints" to totalPoints,
                "totalAssists" to totalAssists,
                "totalRebounds" to totalRebounds,
                "totalSteals" to totalSteals,
                "totalBlocks" to totalBlocks,
                "totalFouls" to totalFouls
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = mapOf(
            "totalPoints" to 0, "totalAssists" to 0, "totalRebounds" to 0,
            "totalSteals" to 0, "totalBlocks" to 0, "totalFouls" to 0
        )
    )
}