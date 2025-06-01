// app/src/main/java/com/example/baskstatsapp/model/PlayerStatsSummary.kt
package com.example.baskstatsapp.model

data class PlayerStatsSummary(
    val totalGamesPlayed: Int = 0,
    val totalPoints: Int = 0,
    val totalAssists: Int = 0,
    val totalRebounds: Int = 0,
    val totalSteals: Int = 0,
    val totalBlocks: Int = 0,
    val totalTurnovers: Int = 0,
    val avgPoints: Double = 0.0,
    val avgAssists: Double = 0.0,
    val avgRebounds: Double = 0.0,
    val avgSteals: Double = 0.0,
    val avgBlocks: Double = 0.0,
    val avgTurnovers: Double = 0.0,
    val totalTwoPointersMade: Int = 0,
    val totalTwoPointersAttempted: Int = 0,
    val totalThreePointersMade: Int = 0,
    val totalThreePointersAttempted: Int = 0,
    val totalFreeThrowsMade: Int = 0,
    val totalFreeThrowsAttempted: Int = 0,
    val totalFouls: Int = 0,
    val totalMinutesPlayed: Int = 0,
    val totalPlusMinus: Int = 0,
    val twoPointersPercentage: Double = 0.0,
    val threePointersPercentage: Double = 0.0,
    val freeThrowsPercentage: Double = 0.0
)