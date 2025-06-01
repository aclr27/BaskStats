// app/src/main/java/com/example/baskstatsapp/viewmodel/EventViewModel.kt
package com.example.baskstatsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baskstatsapp.dao.EventDao
import com.example.baskstatsapp.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EventViewModel(private val eventDao: EventDao) : ViewModel() {

    fun getAllEvents(): Flow<List<Event>> {
        return eventDao.getAllEvents()
    }

    fun getEventById(id: Long): Flow<Event?> {
        return eventDao.getEventById(id)
    }

    suspend fun insertEvent(event: Event): Long {
        return eventDao.insert(event)
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

    fun getEventsForPlayer(playerId: Long): Flow<List<Event>> {
        return eventDao.getEventsByPlayerId(playerId)
    }

    fun getEventsByPlayerId(playerId: Long): Flow<List<Event>> {
        return eventDao.getEventsByPlayerId(playerId)
    }
}