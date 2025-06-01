/**
 * AppDatabase es el mapa de tu base de datos.
 * Le dice a Android qué tipos de cosas vas a guardar (Eventos y Fichas de Rendimiento) y cómo Room
 * (la herramienta que organiza la base de datos) debe tratarlos.
 * LLamamos al Converts para que pase la fecha y horas al formato poara lo que lo entienda la bdd.
 * Los Dao son los encargados de guardar y buscar cosas dentro de la bdd.
 */

package com.example.baskstatsapp.data
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.baskstatsapp.dao.EventDao
import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.PerformanceSheet
import com.example.baskstatsapp.converters.Converters
import com.example.baskstatsapp.dao.PerformanceSheetDao
import com.example.baskstatsapp.dao.PlayerDao
import com.example.baskstatsapp.model.Player
/**
 * BAskStatsDatabase donde recoge todos los datos de la app.
 * Pasa a Room las entidades y vesión y como convertir los tipos de datos raros como las
 * fechas.
 */
@Database(entities = [Event::class, PerformanceSheet::class, Player::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class) // Registra tus convertidores de tipo aquí
abstract class BaskStatsDatabase : RoomDatabase() {
    //Se comunica con el Room
    abstract fun eventDao(): EventDao
    //Y donde se crean las fichas de rendimiento.
    abstract fun performanceSheetDao(): PerformanceSheetDao
    abstract fun playerDao(): PlayerDao
}