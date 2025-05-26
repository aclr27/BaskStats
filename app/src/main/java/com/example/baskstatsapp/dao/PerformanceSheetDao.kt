package com.example.baskstatsapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.baskstatsapp.model.PerformanceSheet
import kotlinx.coroutines.flow.Flow
/**
 * Se encarga de las Fichas de Rendimiento (las estadísticas del jugador).
 * Muy parecido al EventDao, pero para otro tipo de datos.
 */
@Dao
interface PerformanceSheetDao {

    /**
     * Añade una nueva Ficha de Rendimiento.
     * @return El ID de la Ficha.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(performanceSheet: PerformanceSheet): Long

    /**
     * Actualiza una Ficha de Rendimiento existente.
     */
    @Update
    suspend fun update(performanceSheet: PerformanceSheet)

    /**
     * Borra una Ficha de Rendimiento.
     */
    @Delete
    suspend fun delete(performanceSheet: PerformanceSheet)

    /**
     * Consigue una "lista viva" de TODAS las Fichas de Rendimiento, ordenadas de las más nuevas a las más viejas.
     */
    @Query("SELECT * FROM performance_sheets ORDER BY date DESC")
    fun getAllPerformanceSheets(): Flow<List<PerformanceSheet>>

    /**
     * Consigue la información de UNA Ficha de Rendimiento específica.
     */
    @Query("SELECT * FROM performance_sheets WHERE id = :id")
    fun getPerformanceSheetById(id: Long): Flow<PerformanceSheet?>

    /**
     * Consigue todas las Fichas de Rendimiento que pertenecen a un Evento específico (por su ID).
     * Esto es útil para ver las estadísticas de un jugador en un partido o entrenamiento concreto.
     */
    @Query("SELECT * FROM performance_sheets WHERE eventId = :eventId ORDER BY date DESC")
    fun getPerformanceSheetsForEvent(eventId: Long): Flow<List<PerformanceSheet>>
}