// app/src/main/java/com/example/baskstatsapp/viewmodel/GoalViewModel.kt
package com.example.baskstatsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baskstatsapp.dao.GoalDao
import com.example.baskstatsapp.data.model.Goal
import com.example.baskstatsapp.MainActivity // Asegúrate de que esta importación es correcta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoalViewModel(private val goalDao: GoalDao) : ViewModel() {

    private val _goals = MutableStateFlow<List<Goal>>(emptyList())
    val goals: StateFlow<List<Goal>> = _goals.asStateFlow()

    init {
        // Carga los objetivos del jugador logueado al iniciar el ViewModel
        MainActivity.currentLoggedInPlayerId?.let { playerId ->
            viewModelScope.launch {
                goalDao.getGoalsForPlayer(playerId).collect { goalsList ->
                    _goals.value = goalsList
                }
            }
        }
        // Si no hay jugador logueado al inicio, la lista permanece vacía, lo cual es correcto.
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
    fun loadGoalsForCurrentPlayer() {
        MainActivity.currentLoggedInPlayerId?.let { playerId ->
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
