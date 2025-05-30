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
    fun getAllEvents(): Flow<List<Event>> // <--- Debe devolver List<Event> no List<Event?>

    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: Long): Flow<Event?> // Aquí sí puede ser Event? porque es un solo elemento

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event): Long

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("SELECT * FROM events WHERE playerId = :playerId ORDER BY dateTime DESC")
    fun getEventsByPlayerId(playerId: Long): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE DATE(dateTime) = :date ORDER BY dateTime DESC")
    fun getEventsByDate(date: LocalDate): Flow<List<Event>>

}