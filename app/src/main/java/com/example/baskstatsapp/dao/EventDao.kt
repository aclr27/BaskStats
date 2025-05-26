package com.example.baskstatsapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.baskstatsapp.model.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event): Long

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("SELECT * FROM events ORDER BY dateTime DESC")
    fun getAllEvents(): Flow<List<Event>>

    // CAMBIO AQUI: Ahora devuelve Flow<Event?> para manejar el caso de que no se encuentre
    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: Long): Flow<Event?> // <-- CAMBIO AQUI
}