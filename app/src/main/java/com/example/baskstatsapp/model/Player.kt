package com.example.baskstatsapp.model

/**
 * Representa a un jugador de baloncesto.
 */
data class Player(
    /** Identificador único del jugador. */
    val id: String,
    /** Nombre completo del jugador. */
    val name: String,
    /** Número de camiseta del jugador. Nulo si no tiene uno asignado o es desconocido. */
    val number: Int? = null,
    /** Posición de juego del jugador (ej. "Base", "Escolta", "Alero", "Ala-Pívot", "Pívot"). */
    val position: String? = null,
    /** Identificador del equipo al que pertenece el jugador. Nulo si no está en un equipo o no se especifica. */
    val teamId: String? = null,
    /** URL de la foto de perfil del jugador. Nulo si no hay foto. */
    val photoUrl: String? = null
)