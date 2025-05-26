package com.example.baskstatsapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.baskstatsapp.model.Event
import kotlinx.coroutines.flow.Flow

/**
 * Encargado del CRUD de los eventos. Room crea automáticamente el código que lo hace directamente.
 */

@Dao
interface EventDao {
    /**
     * Añade un nuevo Evento a la base de datos.
     * Si ya hay un Evento con el mismo ID, lo reemplaza.
     * @return Devuelve el número de ID que la base de datos le asignó al Evento.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Cuando insertes, si hay un conflicto, reemplaza lo viejo
    suspend fun insert(event: Event): Long // 'suspend' = puede tardar, no bloquees la app

    /**
     * Actualiza la información de un Evento que ya está en la base de datos.
     */
    @Update
    suspend fun update(event: Event)

    /**
     * Borra un Evento de la base de datos.
     */
    @Delete
    suspend fun delete(event: Event)

    /**
     * Consigue una "lista viva" de TODOS los Eventos, ordenada de los más nuevos a los más viejos.
     * Si algo cambia en la base de datos (se añade o borra un Evento), esta lista se actualiza sola.
     */
    @Query("SELECT * FROM events ORDER BY dateTime DESC") // Aquí escribes una "pregunta" en lenguaje de base de datos
    fun getAllEvents(): Flow<List<Event>>

    /**
     * Consigue la información de UN Evento específico usando su número de ID.
     */
    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: Long): Flow<Event>
}