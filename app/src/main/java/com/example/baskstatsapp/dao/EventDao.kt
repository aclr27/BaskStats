// app/src/main/java/com/example/baskstatsapp/dao/EventDao.kt
package com.example.baskstatsapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.baskstatsapp.model.Event
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY dateTime DESC")
    fun getAllEvents(): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: Long): Flow<Event?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event): Long

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("SELECT * FROM events WHERE playerId = :playerId ORDER BY dateTime DESC")
    fun getEventsByPlayerId(playerId: Long): Flow<List<Event>> // Esta query es correcta y usa 'playerId'

    @Query("SELECT * FROM events WHERE DATE(dateTime) = :date ORDER BY dateTime DESC")
    fun getEventsByDate(date: LocalDate): Flow<List<Event>>

    // Eliminamos 'getEventsForPlayer' ya que 'getEventsByPlayerId' hace lo mismo y es coherente con el nombre del campo.
}