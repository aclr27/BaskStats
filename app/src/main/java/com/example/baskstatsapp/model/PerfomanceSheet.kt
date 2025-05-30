package com.example.baskstatsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "performance_sheets")
data class PerformanceSheet(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val date: LocalDate = LocalDate.now(),
    val playerId: Long,
    val eventId: Long? = null,
    val points: Int = 0,
    val assists: Int = 0,
    val rebounds: Int = 0,
    val steals: Int = 0,
    val blocks: Int = 0,
    val turnovers: Int = 0,
    val freeThrowsMade: Int = 0,
    val freeThrowsAttempted: Int = 0,
    val twoPointersMade: Int = 0,
    val twoPointersAttempted: Int = 0,
    val threePointersMade: Int = 0,
    val threePointersAttempted: Int = 0,
    val fouls: Int = 0,
    val minutesPlayed: Int = 0,
    val plusMinus: Int = 0,
    val offensiveRebounds: Int = 0,
    val defensiveRebounds: Int = 0
)