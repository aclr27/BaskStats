// app/src/main/java/com/example/baskstatsapp/viewmodel/EventViewModel.kt
package com.example.baskstatsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baskstatsapp.dao.EventDao
import com.example.baskstatsapp.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf // Importar flowOf
import kotlinx.coroutines.flow.SharingStarted // Importar SharingStarted
import kotlinx.coroutines.flow.stateIn // Importar stateIn
import kotlinx.coroutines.launch

class EventViewModel(private val eventDao: EventDao) : ViewModel() {

    // MutableStateFlow para almacenar el ID del jugador seleccionado
    private val _selectedPlayerId = MutableStateFlow<Long?>(null)
    val selectedPlayerId: StateFlow<Long?> = _selectedPlayerId

    /**
     * Establece el ID del jugador actualmente logueado.
     * Esto actualizará automáticamente los Flows que dependen de él.
     */
    fun setSelectedPlayerId(playerId: Long?) {
        _selectedPlayerId.value = playerId
    }

    // Flow que emite la lista de eventos para el jugador seleccionado
    // Utiliza flatMapLatest para cambiar la fuente del Flow cuando _selectedPlayerId cambia
    val playerEvents: StateFlow<List<Event>> = _selectedPlayerId
        .flatMapLatest { playerId ->
            if (playerId != null && playerId > 0) {
                // Si hay un playerId válido, obtenemos los eventos para ese jugador
                eventDao.getEventsByPlayerId(playerId)
            } else {
                // Si no hay playerId (o es inválido), emitimos una lista vacía
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Mantener el Flow activo mientras haya suscriptores
            initialValue = emptyList() // Valor inicial antes de que se emita el primer elemento
        )


    // Aquí mantienes el resto de tus funciones del ViewModel:

    fun getAllEvents(): Flow<List<Event>> {
        // Podrías considerar filtrar esto también por player ID si getAllEvents solo se usa para el jugador actual
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

    // Si tienes otras funciones en el EventViewModel que necesitan el playerId,
    // asegúrate de que accedan a _selectedPlayerId.value o reciban el playerId como parámetro.
}