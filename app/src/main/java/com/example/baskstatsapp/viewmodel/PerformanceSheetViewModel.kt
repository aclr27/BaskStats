package com.example.baskstatsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baskstatsapp.dao.PerformanceSheetDao
import com.example.baskstatsapp.model.PerformanceSheet
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PerformanceSheetViewModel(private val performanceSheetDao: PerformanceSheetDao) : ViewModel() {

    val allPerformanceSheets: StateFlow<List<PerformanceSheet>> = performanceSheetDao.getAllPerformanceSheets()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    suspend fun getPerformanceSheetById(id: Long): PerformanceSheet? {
        return performanceSheetDao.getPerformanceSheetById(id).firstOrNull()
    }

    // Para obtener fichas vinculadas a un evento específico
    fun getPerformanceSheetsForEvent(eventId: Long): StateFlow<List<PerformanceSheet>> {
        return performanceSheetDao.getPerformanceSheetsForEvent(eventId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    fun insertPerformanceSheet(sheet: PerformanceSheet) {
        viewModelScope.launch {
            performanceSheetDao.insert(sheet)
        }
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