package com.example.baskstatsapp.model

import java.time.LocalDateTime

/**
 * Representa un evento de baloncesto, que puede ser un partido o un entrenamiento.
 * Contiene información general del evento y una lista de las estadísticas individuales de los jugadores.
 */
data class Event(
    /** Identificador único del evento. */
    val id: String,
    /** Tipo de evento: [EventType.MATCH] para partido, [EventType.TRAINING] para entrenamiento. */
    val type: EventType,
    /** Fecha y hora exacta en la que ocurrió el evento. */
    val dateTime: LocalDateTime,
    /** Nombre del equipo oponente, solo aplicable si el evento es un partido. Nulo para entrenamientos. */
    val opponent: String? = null,
    /** Puntuación obtenida por nuestro equipo en el partido. Nulo para entrenamientos. */
    val teamScore: Int? = null,
    /** Puntuación obtenida por el equipo oponente en el partido. Nulo para entrenamientos. */
    val opponentScore: Int? = null,
    /** Notas o comentarios adicionales sobre el evento. */
    val notes: String? = null,
    /** Lista de las estadísticas individuales registradas para los jugadores participantes en este evento. */
    val playerStatsList: List<PlayerStats> = emptyList()
)

/**
 * Define los tipos posibles de eventos de baloncesto.
 */
enum class EventType {
    /** Indica que el evento es un partido oficial o amistoso. */
    MATCH,
    /** Indica que el evento es una sesión de entrenamiento. */
    TRAINING
}