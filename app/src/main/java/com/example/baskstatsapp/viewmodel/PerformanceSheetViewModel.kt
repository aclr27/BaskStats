package com.example.baskstatsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baskstatsapp.dao.PerformanceSheetDao
import com.example.baskstatsapp.model.PerformanceSheet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PerformanceSheetViewModel(private val performanceSheetDao: PerformanceSheetDao) : ViewModel() {

    // Método para añadir una nueva ficha de rendimiento
    suspend fun addPerformanceSheet(sheet: PerformanceSheet): Long { // <-- ¡ASEGÚRATE DE QUE DEVUELVE LONG!
        return performanceSheetDao.insert(sheet)
    }

    fun getAllPerformanceSheets(): Flow<List<PerformanceSheet>> {
        return performanceSheetDao.getAllPerformanceSheets()
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

    fun getPerformanceSheetsForPlayerAndEvent(playerId: Long, eventId: Long): Flow<PerformanceSheet?> {
        return performanceSheetDao.getPerformanceSheetsForPlayerAndEvent(playerId, eventId)
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
}