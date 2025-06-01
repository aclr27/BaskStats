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

class PerformanceSheetViewModel(private val performanceSheetDao: PerformanceSheetDao) : ViewModel() {

    private val _selectedPlayerId = MutableStateFlow<Long?>(null)
    val selectedPlayerId: StateFlow<Long?> = _selectedPlayerId

    fun setSelectedPlayerId(playerId: Long?) {
        _selectedPlayerId.value = playerId
    }

    val allPerformanceSheets: Flow<List<PerformanceSheet>> = _selectedPlayerId.flatMapLatest { playerId ->
        if (playerId == null) {
            performanceSheetDao.getAllPerformanceSheets()
        } else {
            performanceSheetDao.getPerformanceSheetsForPlayer(playerId)
        }
    }

    suspend fun addPerformanceSheet(sheet: PerformanceSheet): Long {
        return performanceSheetDao.insert(sheet)
    }

    fun getPerformanceSheetById(sheetId: Long): Flow<PerformanceSheet?> {
        return performanceSheetDao.getPerformanceSheetById(sheetId)
    }

    fun getPerformanceSheetsForEvent(eventId: Long): Flow<List<PerformanceSheet>> {
        return performanceSheetDao.getPerformanceSheetsForEvent(eventId)
    }

    fun getPerformanceSheetsForPlayer(playerId: Long): Flow<List<PerformanceSheet>> {
        return performanceSheetDao.getPerformanceSheetsForPlayer(playerId)
    }

    // Corregido: Usar getPerformanceSheetForEventAndPlayer que es el método correcto en PerformanceSheetDao
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

}