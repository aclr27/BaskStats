package com.example.baskstatsapp.data

import com.example.baskstatsapp.dao.EventDao
import com.example.baskstatsapp.dao.PerformanceSheetDao
import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.PerformanceSheet
import kotlinx.coroutines.flow.Flow

// Esta clase actúa como una capa de abstracción entre tus ViewModels y tus DAOs (Room).
// Es útil para centralizar la lógica de acceso a datos, manejar múltiples fuentes de datos (ej. red + BD),
// y proporcionar una API más limpia a los ViewModels.
class AppRepository(
    private val eventDao: EventDao,
    private val performanceSheetDao: PerformanceSheetDao
) {

    // --- Métodos para Eventos ---

    // Obtiene todos los eventos. El tipo de retorno coincide con el DAO.
    fun getAllEvents(): Flow<List<Event>> {
        return eventDao.getAllEvents()
    }

    // Obtiene un evento por su ID. El tipo de retorno coincide con el DAO.
    fun getEventById(id: Long): Flow<Event?> { // Coincide con el DAO que devuelve Flow<Event?>
        return eventDao.getEventById(id)
    }

    // Inserta un evento.
    suspend fun insertEvent(event: Event): Long {
        return eventDao.insert(event)
    }

    // Actualiza un evento.
    suspend fun updateEvent(event: Event) {
        eventDao.update(event)
    }

    // Elimina un evento.
    suspend fun deleteEvent(event: Event) {
        eventDao.delete(event)
    }

    // --- Métodos para Fichas de Rendimiento ---

    // Obtiene todas las fichas de rendimiento. El tipo de retorno coincide con el DAO.
    fun getAllPerformanceSheets(): Flow<List<PerformanceSheet>> {
        return performanceSheetDao.getAllPerformanceSheets()
    }

    // Obtiene una ficha de rendimiento por su ID. El tipo de retorno coincide con el DAO.
    fun getPerformanceSheetById(id: Long): Flow<PerformanceSheet?> {
        return performanceSheetDao.getPerformanceSheetById(id)
    }

    // Inserta una ficha de rendimiento.
    suspend fun insertPerformanceSheet(sheet: PerformanceSheet): Long {
        return performanceSheetDao.insert(sheet)
    }

    // Actualiza una ficha de rendimiento.
    suspend fun updatePerformanceSheet(sheet: PerformanceSheet) {
        performanceSheetDao.update(sheet)
    }

    // Elimina una ficha de rendimiento.
    suspend fun deletePerformanceSheet(sheet: PerformanceSheet) {
        performanceSheetDao.delete(sheet)
    }

    // Obtiene fichas de rendimiento para un evento específico.
    fun getPerformanceSheetsForEvent(eventId: Long): Flow<List<PerformanceSheet>> {
        return performanceSheetDao.getPerformanceSheetsForEvent(eventId)
    }
}