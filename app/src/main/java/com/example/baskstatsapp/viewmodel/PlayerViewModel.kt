package com.example.baskstatsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baskstatsapp.dao.PlayerDao
import com.example.baskstatsapp.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers // Importar Dispatchers
import kotlinx.coroutines.withContext // Importar withContext
import org.mindrot.jbcrypt.BCrypt // Importar BCrypt
import android.util.Log // Importar Log para depuración

class PlayerViewModel(private val playerDao: PlayerDao) : ViewModel() {

    fun getAllPlayers(): Flow<List<Player>> {
        return playerDao.getAllPlayers()
    }

    fun getPlayerById(playerId: Long): Flow<Player?> {
        return playerDao.getPlayerById(playerId)
    }

    // El registro ya está bien, hashea la contraseña antes de insertar.
    suspend fun registerPlayer(player: Player): Long {
        // Asegúrate de que la contraseña ya viene hasheada desde RegistrationScreen
        return playerDao.insert(player)
    }

    // MODIFICADO: La función loginPlayer ahora recibe la contraseña en texto plano (passwordAttempt)
    suspend fun loginPlayer(email: String, passwordAttempt: String): Player? {
        return withContext(Dispatchers.IO) { // Ejecutar en hilo de IO para operaciones de BD y BCrypt
            Log.d("LoginProcess", "Intentando login para email: ${email.trim()}")
            val player = playerDao.getPlayerByEmail(email.trim()) // Buscar jugador por email
            if (player != null) {
                Log.d("LoginProcess", "Jugador encontrado: ${player.email}")
                // MODIFICADO: Usa BCrypt.checkpw para verificar la contraseña
                // Compara el texto plano introducido por el usuario con el hash guardado
                if (BCrypt.checkpw(passwordAttempt.trim(), player.passwordHash)) {
                    Log.d("LoginProcess", "Contraseña CORRECTA para ${player.email}")
                    player
                } else {
                    Log.d("LoginProcess", "Contraseña INCORRECTA para ${player.email}")
                    null // Contraseña no coincide
                }
            } else {
                Log.d("LoginProcess", "No se encontró jugador con el email: ${email.trim()}")
                null // Jugador no encontrado
            }
        }
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