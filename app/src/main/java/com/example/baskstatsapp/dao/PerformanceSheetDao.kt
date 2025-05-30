package com.example.baskstatsapp.dao

import androidx.room.*
import com.example.baskstatsapp.model.PerformanceSheet
import kotlinx.coroutines.flow.Flow

@Dao
interface PerformanceSheetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sheet: PerformanceSheet): Long // <-- ¡ASEGÚRATE DE QUE DEVUELVE LONG!

    @Update
    suspend fun update(sheet: PerformanceSheet)

    @Delete
    suspend fun delete(sheet: PerformanceSheet)

    @Query("SELECT * FROM performance_sheets ORDER BY date DESC")
    fun getAllPerformanceSheets(): Flow<List<PerformanceSheet>>

    @Query("SELECT * FROM performance_sheets WHERE id = :sheetId")
    fun getPerformanceSheetById(sheetId: Long): Flow<PerformanceSheet?>

    @Query("SELECT * FROM performance_sheets WHERE eventId = :eventId ORDER BY date DESC")
    fun getPerformanceSheetsForEvent(eventId: Long): Flow<List<PerformanceSheet>>

    @Query("SELECT * FROM performance_sheets WHERE playerId = :playerId ORDER BY date DESC")
    fun getPerformanceSheetsForPlayer(playerId: Long): Flow<List<PerformanceSheet>>

    @Query("SELECT * FROM performance_sheets WHERE playerId = :playerId AND eventId = :eventId")
    fun getPerformanceSheetsForPlayerAndEvent(playerId: Long, eventId: Long): Flow<PerformanceSheet?>
}