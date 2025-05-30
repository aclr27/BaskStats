package com.example.baskstatsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val username: String,
    val email: String,
    val passwordHash: String,
    val number: Int? = null,
    val position: String? = null,
    val teamId: String? = null,
    val photoUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)