package com.example.baskstatsapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.baskstatsapp.data.model.Goal // Asegúrate de que la importación de Goal es correcta
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal)

    @Update
    suspend fun updateGoal(goal: Goal)

    @Delete
    suspend fun deleteGoal(goal: Goal)

    // Obtiene todos los objetivos para un jugador específico, ordenados por fecha de creación descendente
    @Query("SELECT * FROM goals WHERE playerId = :playerId ORDER BY creationDate DESC")
    fun getGoalsForPlayer(playerId: Long): Flow<List<Goal>>

    // Obtiene un objetivo por su ID
    @Query("SELECT * FROM goals WHERE goalId = :goalId")
    suspend fun getGoalById(goalId: Long): Goal?
}