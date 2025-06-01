// app/src/main/java/com/example/baskstatsapp/model/PerformanceSheet.kt
package com.example.baskstatsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index // <-- Importar Index
import java.time.LocalDate

@Entity(tableName = "performance_sheets",
    foreignKeys = [
        ForeignKey(
            entity = Player::class,
            parentColumns = ["id"],
            childColumns = ["playerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Event::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [ // <-- Añadir la sección de índices
        Index(value = ["playerId"]), // Índice para playerId
        Index(value = ["eventId"]) // Índice para eventId
    ]
)
data class PerformanceSheet(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: LocalDate,
    val playerId: Long,
    val eventId: Long?,
    val points: Int,
    val assists: Int,
    val rebounds: Int, // Total de rebotes
    val offensiveRebounds: Int, // Rebotes ofensivos
    val defensiveRebounds: Int, // Rebotes defensivos
    val steals: Int,
    val blocks: Int,
    val turnovers: Int,
    val fouls: Int,
    val twoPointersMade: Int,
    val twoPointersAttempted: Int,
    val threePointersMade: Int,
    val threePointersAttempted: Int,
    val freeThrowsMade: Int,
    val freeThrowsAttempted: Int,
    val minutesPlayed: Int,
    val plusMinus: Int,
) {
    // Propiedad calculada para total de rebotes
    val totalRebounds: Int
        get() = offensiveRebounds + defensiveRebounds
}