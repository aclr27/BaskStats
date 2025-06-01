// app/src/main/java/com/example/baskstatsapp/model/PerformanceSheet.kt
package com.example.baskstatsapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "performance_sheets",
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
    ]
)
data class PerformanceSheet(
    @PrimaryKey(autoGenerate = true) val sheetId: Long = 0, // Asegura que se usa sheetId
    val playerId: Long,
    val eventId: Long,
    val eventDate: Long, // <--- Importante que sea este nombre para las consultas
    val points: Int,
    val assists: Int,
    val offensiveRebounds: Int,
    val defensiveRebounds: Int,
    val steals: Int,
    val turnovers: Int,
    val blocks: Int,
    val fouls: Int,
    val freeThrowsMade: Int,
    val freeThrowsAttempted: Int,
    val twoPointersMade: Int,
    val twoPointersAttempted: Int,
    val threePointersMade: Int,
    val threePointersAttempted: Int,
    val minutesPlayed: Int = 0, // Asegúrate de que estos existan si los usas en el DAO
    val plusMinus: Int = 0      // Asegúrate de que estos existan si los usas en el DAO
)