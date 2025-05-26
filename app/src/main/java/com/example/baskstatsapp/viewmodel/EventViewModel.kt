package com.example.baskstatsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.baskstatsapp.dao.EventDao
import com.example.baskstatsapp.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EventViewModel(private val eventDao: EventDao) : ViewModel() {

    val allEvents: Flow<List<Event>> = eventDao.getAllEvents()

    // Asegúrate de que insertEvent devuelve Long
    suspend fun insertEvent(event: Event): Long { // <--- Importante: Devuelve Long
        return eventDao.insert(event) // El método insert del DAO devuelve Long
    }

    suspend fun getEventById(id: Long): Flow<Event?> {
        return eventDao.getEventById(id)
    }

    suspend fun updateEvent(event: Event) {
        eventDao.update(event)
    }

    suspend fun deleteEvent(event: Event) {
        eventDao.delete(event)
    }
}

class EventViewModelFactory(private val eventDao: EventDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(eventDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}