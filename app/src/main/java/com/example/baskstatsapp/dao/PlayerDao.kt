package com.example.baskstatsapp.dao

import androidx.room.*
import com.example.baskstatsapp.model.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(player: Player): Long

    // Cambiado 'username' por 'email'
    @Query("SELECT * FROM players WHERE email = :email")
    suspend fun getPlayerByEmail(email: String): Player? // <-- Nombre del método cambiado

    @Query("SELECT * FROM players WHERE id = :playerId")
    fun getPlayerById(playerId: Long): Flow<Player?>

    // Cambiado 'username' por 'email' (o el campo por el que quieras ordenar)
    @Query("SELECT * FROM players ORDER BY email ASC") // O "name ASC" si prefieres
    fun getAllPlayers(): Flow<List<Player>>

    @Update
    suspend fun update(player: Player)

    @Delete
    suspend fun delete(player: Player)
}