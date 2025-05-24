package com.example.baskstatsapp.model

/**
 * Representa las estadísticas individuales de un jugador dentro de un evento específico.
 */
data class PlayerStats(
    /** Identificador del jugador al que pertenecen estas estadísticas. */
    val playerId: String,
    /** Identificador del evento al que corresponden estas estadísticas. */
    val eventId: String,
    /** Total de puntos anotados por el jugador en el evento. */
    val points: Int,
    /** Total de asistencias realizadas por el jugador en el evento. */
    val assists: Int,
    /** Total de rebotes conseguidos por el jugador en el evento (suma de rebotes ofensivos y defensivos). */
    val totalRebounds: Int,
    /** Rebotes ofensivos capturados por el jugador en el evento. */
    val offensiveRebounds: Int = 0,
    /** Rebotes defensivos capturados por el jugador en el evento. */
    val defensiveRebounds: Int = 0,
    /** Total de robos de balón realizados por el jugador en el evento. */
    val steals: Int,
    /** Total de tapones realizados por el jugador en el evento. */
    val blocks: Int,
    /** Total de pérdidas de balón incurridas por el jugador en el evento. */
    val turnovers: Int = 0,
    /** Total de faltas personales cometidas por el jugador en el evento. */
    val fouls: Int = 0,
    /** Número de canastas de dos puntos anotadas. */
    val twoPointersMade: Int = 0,
    /** Número de intentos de canastas de dos puntos. */
    val twoPointersAttempted: Int = 0,
    /** Número de canastas de tres puntos anotadas. */
    val threePointersMade: Int = 0,
    /** Número de intentos de canastas de tres puntos. */
    val threePointersAttempted: Int = 0,
    /** Número de tiros libres anotados. */
    val freeThrowsMade: Int = 0,
    /** Número de intentos de tiros libres. */
    val freeThrowsAttempted: Int = 0,
    /** Minutos jugados por el jugador en el evento. */
    val minutesPlayed: Int = 0,
    /** El valor Plus/Minus del jugador en el evento (diferencia de puntos del equipo mientras el jugador estaba en cancha). */
    val plusMinus: Int = 0
)