// app/src/main/java/com/example/baskstatsapp/data/model/Goal.kt
package com.example.baskstatsapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val goalId: Long = 0L,
    val playerId: Long, // Para asociar el objetivo con un jugador
    val description: String, // Ejemplo: "Anotar este mes 20 puntos"
    val type: GoalType, // Tipo de objetivo (Puntos, Rebotes, Partidos, etc.)
    val targetQuantity: Double, // Cantidad a alcanzar (ej: 20 puntos, 15 rebotes). Usamos Double para promedios.
    val frequency: GoalFrequency, // Frecuencia del objetivo (Mensual, Por Partido, etc.)
    val notes: String?, // Notas adicionales opcionales
    val creationDate: Long = System.currentTimeMillis(), // Fecha de creación (timestamp)
    var completionDate: Long? = null, // Fecha de completado (si aplica)
    var status: GoalStatus = GoalStatus.IN_PROGRESS, // Estado del objetivo
    var currentValue: Double = 0.0 // Valor actual para el seguimiento del objetivo (ej: 18 puntos anotados). Usamos Double.
)

enum class GoalType {
    // Estadísticas de partido
    POINTS,         // Puntos totales
    REBOUNDS,       // Rebotes totales
    ASSISTS,        // Asistencias totales
    STEALS,         // Robos totales
    BLOCKS,         // Tapones totales
    TURNOVERS,      // Pérdidas de balón (intentar reducir)
    FIELD_GOAL_PERCENTAGE, // % Tiros de campo
    THREE_POINT_PERCENTAGE, // % Tiros de 3
    FREE_THROW_PERCENTAGE,  // % Tiros libres
    // Estadísticas de rendimiento
    AVERAGE_POINTS, // Promedio de puntos por partido
    AVERAGE_REBOUNDS, // Promedio de rebotes por partido
    AVERAGE_ASSISTS,  // Promedio de asistencias por partido
    // Resultados de equipo/individual
    WINS,           // Número de victorias
    CONSECUTIVE_WINS, // Victorias consecutivas
    MINUTES_PLAYED,   // Minutos jugados
    // Fitness/Habilidad (requeriría más integración de datos)
    SPEED_SPRINT,     // Velocidad en sprint (ej: tiempo en X metros)
    VERTICAL_JUMP,    // Salto vertical (ej: altura en cm)
    ENDURANCE,        // Resistencia (ej: duración de ejercicio sin fatiga)
}

enum class GoalFrequency {
    PER_GAME,       // Por partido (ej: 10 puntos por partido)
    PER_MONTH,      // Por mes (ej: 100 puntos este mes)
    PER_SEASON,     // Por temporada (ej: 500 puntos esta temporada)
    OVERALL,        // En general (ej: alcanzar X valor absoluto, o mejorar Y%)
    LAST_X_GAMES,   // En los últimos X partidos (ej: promediar 20 puntos en los últimos 5 partidos)
    NEXT_X_GAMES,   // En los próximos X partidos (ej: ganar los próximos 3 partidos)
    SPECIFIC_GAME,  // En un partido específico (requeriría vincular a un EventId)
    // Para porcentajes o récords personales
    PERSONAL_BEST,  // Mejorar un récord personal (ej: Salto vertical de X cm)
    MAINTAIN_PERCENTAGE, // Mantener un porcentaje (ej: 80% tiros libres)
}

enum class GoalStatus {
    IN_PROGRESS, // En Curso
    COMPLETED, // Completado
    FAILED, // Fallido
    CANCELLED // Cancelado
}