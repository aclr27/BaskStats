package com.example.baskstatsapp // Asegúrate de que este sea el nombre de tu paquete principal

import android.app.Application
import androidx.room.Room
import com.example.baskstatsapp.data.AppDatabase
import com.example.baskstatsapp.data.AppRepository

class BaskStatsApplication : Application() {
    // Instancia de la base de datos
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "baskstats_db" // Nombre de tu base de datos
        ).build()
    }

    // Instancia del repositorio
    val repository: AppRepository by lazy {
        AppRepository(database.eventDao(), database.performanceSheetDao())
    }
}