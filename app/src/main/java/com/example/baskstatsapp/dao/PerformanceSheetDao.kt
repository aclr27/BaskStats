// app/src/main/java/com/example/baskstatsapp/dao/PerformanceSheetDao.kt
package com.example.baskstatsapp.dao

import androidx.room.*
import com.example.baskstatsapp.model.PerformanceSheet
import kotlinx.coroutines.flow.Flow

@Dao
interface PerformanceSheetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sheet: PerformanceSheet): Long

    @Update
    suspend fun update(sheet: PerformanceSheet)

    @Delete
    suspend fun delete(sheet: PerformanceSheet)

    // Asegúrate de que 'eventDate' es el nombre de la columna en tu modelo PerformanceSheet
    @Query("SELECT * FROM performance_sheets ORDER BY eventDate DESC")
    fun getAllPerformanceSheets(): Flow<List<PerformanceSheet>>

    // Asegúrate de que 'sheetId' es el nombre de la columna de ID en tu modelo PerformanceSheet
    @Query("SELECT * FROM performance_sheets WHERE sheetId = :sheetId")
    fun getPerformanceSheetById(sheetId: Long): Flow<PerformanceSheet?>

    @Query("SELECT * FROM performance_sheets WHERE eventId = :eventId ORDER BY eventDate DESC")
    fun getPerformanceSheetsForEvent(eventId: Long): Flow<List<PerformanceSheet>>

    @Query("SELECT * FROM performance_sheets WHERE playerId = :playerId ORDER BY eventDate DESC")
    fun getPerformanceSheetsForPlayer(playerId: Long): Flow<List<PerformanceSheet>>

    @Query("SELECT * FROM performance_sheets WHERE playerId = :playerId AND eventId = :eventId")
    fun getPerformanceSheetForEventAndPlayer(playerId: Long, eventId: Long): Flow<PerformanceSheet?>

    // Método para obtener las últimas N fichas de rendimiento de un jugador
    @Query("SELECT * FROM performance_sheets WHERE playerId = :playerId ORDER BY eventDate DESC LIMIT :limit")
    fun getLastNPerformanceSheetsForPlayer(playerId: Long, limit: Int): Flow<List<PerformanceSheet>>


    // --- CONSULTAS PARA ESTADÍSTICAS GLOBALES ---

    @Query("SELECT COUNT(DISTINCT eventId) FROM performance_sheets WHERE playerId = :playerId AND eventId IS NOT NULL")
    fun getTotalGamesPlayed(playerId: Long): Flow<Int>

    @Query("SELECT SUM(points) FROM performance_sheets WHERE playerId = :playerId")
    fun getTotalPoints(playerId: Long): Flow<Int?>

    @Query("SELECT SUM(assists) FROM performance_sheets WHERE playerId = :playerId")
    fun getTotalAssists(playerId: Long): Flow<Int?>

    @Query("SELECT SUM(defensiveRebounds + offensiveRebounds) FROM performance_sheets WHERE playerId = :playerId")
    fun getTotalRebounds(playerId: Long): Flow<Int?>

    @Query("SELECT SUM(steals) FROM performance_sheets WHERE playerId = :playerId")
    fun getTotalSteals(playerId: Long): Flow<Int?>

    @Query("SELECT SUM(blocks) FROM performance_sheets WHERE playerId = :playerId")
    fun getTotalBlocks(playerId: Long): Flow<Int?>

    @Query("SELECT SUM(turnovers) FROM performance_sheets WHERE playerId = :playerId")
    fun getTotalTurnovers(playerId: Long): Flow<Int?>

    @Query("SELECT AVG(points) FROM performance_sheets WHERE playerId = :playerId")
    fun getAvgPoints(playerId: Long): Flow<Double?>

    @Query("SELECT AVG(assists) FROM performance_sheets WHERE playerId = :playerId")
    fun getAvgAssists(playerId: Long): Flow<Double?>

    @Query("SELECT AVG(defensiveRebounds + offensiveRebounds) FROM performance_sheets WHERE playerId = :playerId")
    fun getAvgRebounds(playerId: Long): Flow<Double?>

    @Query("SELECT AVG(steals) FROM performance_sheets WHERE playerId = :playerId")
    fun getAvgSteals(playerId: Long): Flow<Double?>

    @Query("SELECT AVG(blocks) FROM performance_sheets WHERE playerId = :playerId")
    fun getAvgBlocks(playerId: Long): Flow<Double?>

    @Query("SELECT AVG(turnovers) FROM performance_sheets WHERE playerId = :playerId")
    fun getAvgTurnovers(playerId: Long): Flow<Double?>

    @Query("SELECT SUM(twoPointersMade) FROM performance_sheets WHERE playerId = :playerId")
    fun getTotalTwoPointersMade(playerId: Long): Flow<Int?>

    @Query("SELECT SUM(twoPointersAttempted) FROM performance_sheets WHERE playerId = :playerId")
    fun getTotalTwoPointersAttempted(playerId: Long): Flow<Int?>

    @Query("SELECT SUM(threePointersMade) FROM performance_sheets WHERE playerId = :playerId")
    fun getTotalThreePointersMade(playerId: Long): Flow<Int?>

    @Query("SELECT SUM(threePointersAttempted) FROM performance_sheets WHERE playerId = :playerId")
    fun getTotalThreePointersAttempted(playerId: Long): Flow<Int?>

    @Query("SELECT SUM(freeThrowsMade) FROM performance_sheets WHERE playerId = :playerId")
    fun getTotalFreeThrowsMade(playerId: Long): Flow<Int?>

    @Query("SELECT SUM(freeThrowsAttempted) FROM performance_sheets WHERE playerId = :playerId")
    fun getTotalFreeThrowsAttempted(playerId: Long): Flow<Int?>

    @Query("SELECT SUM(fouls) FROM performance_sheets WHERE playerId = :playerId")
    fun getTotalFouls(playerId: Long): Flow<Int?>

    @Query("SELECT SUM(minutesPlayed) FROM performance_sheets WHERE playerId = :playerId")
    fun getTotalMinutesPlayed(playerId: Long): Flow<Int?>

    @Query("SELECT SUM(plusMinus) FROM performance_sheets WHERE playerId = :playerId")
    fun getTotalPlusMinus(playerId: Long): Flow<Int?>

    @Query("""
        SELECT
            CASE
                WHEN SUM(twoPointersAttempted) > 0 THEN CAST(SUM(twoPointersMade) AS REAL) * 100 / SUM(twoPointersAttempted)
                ELSE 0.0
            END
        FROM performance_sheets
        WHERE playerId = :playerId
    """)
    fun getTwoPointersPercentage(playerId: Long): Flow<Double?>

    @Query("""
        SELECT
            CASE
                WHEN SUM(threePointersAttempted) > 0 THEN CAST(SUM(threePointersMade) AS REAL) * 100 / SUM(threePointersAttempted)
                ELSE 0.0
            END
        FROM performance_sheets
        WHERE playerId = :playerId
    """)
    fun getThreePointersPercentage(playerId: Long): Flow<Double?>

    @Query("""
        SELECT
            CASE
                WHEN SUM(freeThrowsAttempted) > 0 THEN CAST(SUM(freeThrowsMade) AS REAL) * 100 / SUM(freeThrowsAttempted)
                ELSE 0.0
            END
        FROM performance_sheets
        WHERE playerId = :playerId
    """)
    fun getFreeThrowsPercentage(playerId: Long): Flow<Double?>
}