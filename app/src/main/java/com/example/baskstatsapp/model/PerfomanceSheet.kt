package com.example.baskstatsapp.model

import java.time.LocalDate

/**
 * Representa una ficha o resumen de rendimiento de un jugador en una fecha específica.
 * Puede ser una instantánea de sus estadísticas en un evento o un resumen de un periodo.
 */
data class PerformanceSheet(
    /** Identificador único para esta ficha de rendimiento. */
    val id: String,
    /** Fecha a la que corresponde esta ficha de rendimiento. */
    val date: LocalDate,
    /** Identificador del jugador al que pertenece esta ficha de rendimiento. */
    val playerId: String,
    /** Opcional: Identificador del evento del que se derivan estas estadísticas, si aplica. */
    val eventId: String? = null,
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
    val freeThrowsAttempted: Int = 0
)