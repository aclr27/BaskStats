package com.example.baskstatsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baskstatsapp.dao.PlayerDao
import com.example.baskstatsapp.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PlayerViewModel(private val playerDao: PlayerDao) : ViewModel() {

    fun getAllPlayers(): Flow<List<Player>> {
        return playerDao.getAllPlayers()
    }

    fun getPlayerById(playerId: Long): Flow<Player?> {
        return playerDao.getPlayerById(playerId)
    }

    suspend fun registerPlayer(player: Player): Long {
        return playerDao.insert(player)
    }

    // Usar 'email' en lugar de 'username' para el login
    suspend fun loginPlayer(email: String, passwordHash: String): Player? {
        return playerDao.getPlayerByEmail(email)?.takeIf { it.passwordHash == passwordHash }
    }

    fun updatePlayer(player: Player) {
        viewModelScope.launch {
            playerDao.update(player)
        }
    }

    fun deletePlayer(player: Player) {
        viewModelScope.launch {
            playerDao.delete(player)
        }
    }
}