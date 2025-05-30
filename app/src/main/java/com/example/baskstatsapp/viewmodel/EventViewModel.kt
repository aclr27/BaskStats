package com.example.baskstatsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.baskstatsapp.dao.EventDao
import com.example.baskstatsapp.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate // Asegúrate de esta importación para getEventsByDate

class EventViewModel(private val eventDao: EventDao) : ViewModel() {

    // Puedes mantener val allEvents o simplemente usar la función getAllEvents()
    // val allEvents: Flow<List<Event>> = eventDao.getAllEvents()

    fun getAllEvents(): Flow<List<Event>> = eventDao.getAllEvents()

    suspend fun insertEvent(event: Event): Long {
        return eventDao.insert(event)
    }

    fun getEventById(id: Long): Flow<Event?> {
        return eventDao.getEventById(id)
    }

    // ¡¡¡ESTA ES LA FUNCIÓN QUE FALTABA Y DEBES AÑADIR/CONFIRMAR!!!
    fun getEventsByPlayerId(playerId: Long): Flow<List<Event>> {
        return eventDao.getEventsByPlayerId(playerId)
    }

    fun getEventsByDate(date: LocalDate): Flow<List<Event>> {
        return eventDao.getEventsByDate(date)
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            eventDao.update(event)
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            eventDao.delete(event)
        }
    }
}

// Tu EventViewModelFactory está bien, no necesita cambios
class EventViewModelFactory(private val eventDao: EventDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(eventDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}