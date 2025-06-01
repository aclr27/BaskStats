package com.example.baskstatsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baskstatsapp.dao.GoalDao
import com.example.baskstatsapp.data.model.Goal
// import com.example.baskstatsapp.MainActivity // Generalmente evitamos importar MainActivity en ViewModels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoalViewModel(private val goalDao: GoalDao) : ViewModel() {

    private val _goals = MutableStateFlow<List<Goal>>(emptyList())
    val goals: StateFlow<List<Goal>> = _goals.asStateFlow()

    // Nuevo StateFlow para el playerId logueado
    private val _loggedInPlayerId = MutableStateFlow<Long?>(null)

    init {
        // Observar el playerId logueado y cargar los objetivos cuando cambie
        viewModelScope.launch {
            _loggedInPlayerId.collect { playerId ->
                if (playerId != null) {
                    goalDao.getGoalsForPlayer(playerId).collect { goalsList ->
                        _goals.value = goalsList
                    }
                } else {
                    _goals.value = emptyList() // Si no hay jugador logueado, la lista está vacía
                }
            }
        }
    }

    // Método público para que MainActivity establezca el ID del jugador logueado
    fun setLoggedInPlayerId(playerId: Long?) {
        _loggedInPlayerId.value = playerId
    }

    fun addGoal(goal: Goal) {
        viewModelScope.launch {
            goalDao.insertGoal(goal)
            // Después de insertar, volvemos a cargar para actualizar la lista de objetivos en la UI
            loadGoalsForCurrentPlayer()
        }
    }

    fun updateGoal(goal: Goal) {
        viewModelScope.launch {
            goalDao.updateGoal(goal)
            // Después de actualizar, volvemos a cargar
            loadGoalsForCurrentPlayer()
        }
    }

    fun deleteGoal(goal: Goal) {
        viewModelScope.launch {
            goalDao.deleteGoal(goal)
            // Después de eliminar, volvemos a cargar
            loadGoalsForCurrentPlayer()
        }
    }

    suspend fun getGoalById(goalId: Long): Goal? {
        return goalDao.getGoalById(goalId)
    }

    // Este método es útil para asegurarse de que la lista de objetivos se actualiza
    // después de cualquier operación CRUD o si el estado del jugador logueado cambia.
    private fun loadGoalsForCurrentPlayer() { // Cambiado a private, ya que setLoggedInPlayerId ahora maneja la carga
        _loggedInPlayerId.value?.let { playerId ->
            viewModelScope.launch {
                goalDao.getGoalsForPlayer(playerId).collect { goalsList ->
                    _goals.value = goalsList
                }
            }
        } ?: run {
            _goals.value = emptyList() // Si no hay jugador logueado, la lista de objetivos está vacía
        }
    }
}