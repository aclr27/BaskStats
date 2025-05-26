package com.example.baskstatsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Representa una ficha o resumen de rendimiento de un jugador en una fecha específica.
 * Puede ser una instantánea de sus estadísticas en un evento o un resumen de un periodo.
 */

@Entity(tableName = "performance_sheets")
data class PerformanceSheet(
    @PrimaryKey(autoGenerate = true)
    /** Identificador único para esta ficha de rendimiento. */
    val id: Long = 0, //Importante el tipo Long para autoGenerar
    /** Fecha a la que corresponde esta ficha de rendimiento. */
    val date: LocalDate,
    /** Identificador del jugador al que pertenece esta ficha de rendimiento. */
    val playerId: String,
    /** Opcional: Identificador del evento del que se derivan estas estadísticas, si aplica. */
    val eventId: Long? = null,
    /** Total de puntos anotados. */
    val points: Int,
    /** Total de asistencias realizadas. */
    val assists: Int,
    /** Total de rebotes conseguidos (ofensivos + defensivos). */
    val rebounds: Int,
    /** Total de robos de balón realizados. */
    val steals: Int,
    /** Total de tapones realizados. */
    val blocks: Int,
    /** Total de pérdidas de balón. */
    val turnovers: Int = 0,
    /** Total de tiros libres anotados. */
    val freeThrowsMade: Int = 0,
    /** Total de tiros libres intentados. */
    val freeThrowsAttempted: Int = 0,
    /** Tiros de dos metidos */
    val twoPointersMade: Int = 0,
    /** Tiros de dos intentados*/
    val twoPointersAttempted: Int = 0,
    /** Tiros de tres metidos*/
    val threePointersMade: Int = 0,
    /** Tiros de tres intentas*/
    val threePointersAttempted: Int = 0,
    /** Faltas cometidas*/
    val fouls: Int = 0,
    /** Minutos jugados*/
    val minutesPlayed: Int = 0,
    /** mas menos*/
    val plusMinus: Int = 0,
    /** Rebotes ofensivos*/
    val offensiveRebounds: Int = 0, // Añadidos para el AddEventScreen
    /** rebotes defensivos*/
    val defensiveRebounds: Int = 0 // Añadidos para el AddEventScreen
)