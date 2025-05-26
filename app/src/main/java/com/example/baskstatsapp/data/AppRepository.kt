package com.example.baskstatsapp.data

import com.example.baskstatsapp.dao.EventDao
import com.example.baskstatsapp.dao.PerformanceSheetDao
import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.PerformanceSheet
import kotlinx.coroutines.flow.Flow

/**
 * Puente entra la app y la bdd. La interfaz de usuario pasa por el Repository. Así el Repository
 * pasa a los Daos que hagan CRUD en la bdd.
 */
class AppRepository(
    private val eventDao: EventDao,
    private val performanceSheetDao: PerformanceSheetDao
) {
    // Event operations
    suspend fun insertEvent(event: Event): Long {
        return eventDao.insert(event)
    }

    suspend fun updateEvent(event: Event) {
        eventDao.update(event)
    }

    suspend fun deleteEvent(event: Event) {
        eventDao.delete(event)
    }

    fun getAllEvents(): Flow<List<Event>> {
        return eventDao.getAllEvents()
    }

    fun getEventById(id: Long): Flow<Event> {
        return eventDao.getEventById(id)
    }

    // PerformanceSheet operations
    suspend fun insertPerformanceSheet(performanceSheet: PerformanceSheet): Long {
        return performanceSheetDao.insert(performanceSheet)
    }

    suspend fun updatePerformanceSheet(performanceSheet: PerformanceSheet) {
        performanceSheetDao.update(performanceSheet)
    }

    suspend fun deletePerformanceSheet(performanceSheet: PerformanceSheet) {
        performanceSheetDao.delete(performanceSheet)
    }

    fun getAllPerformanceSheets(): Flow<List<PerformanceSheet>> {
        return performanceSheetDao.getAllPerformanceSheets()
    }

    fun getPerformanceSheetById(id: Long): Flow<PerformanceSheet> {
        return performanceSheetDao.getPerformanceSheetById(id)
    }

    fun getPerformanceSheetsForEvent(eventId: Long): Flow<List<PerformanceSheet>> {
        return performanceSheetDao.getPerformanceSheetsForEvent(eventId)
    }
}