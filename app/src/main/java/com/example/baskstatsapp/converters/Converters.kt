package com.example.baskstatsapp.converters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset // Para convertir a y desde Long

/**
 * [Converters] es como un "diccionario de traducción" para tu base de datos.
 * La base de datos (SQLite) no entiende directamente "fechas" o "fechas con hora" de la misma forma que Kotlin (LocalDateTime, LocalDate).
 * Así que necesitamos unas reglas para traducir:
 * - De 'LocalDateTime' (o LocalDate) a un número (Long) que la base de datos sí entienda.
 * - Y de ese número (Long) de vuelta a 'LocalDateTime' (o LocalDate) para tu app.
 *
 * Cada función con `@TypeConverter` es una regla de traducción.
 */
class Converters {

    /**
     * Regla para traducir un NÚMERO GRANDE (Long, como un sello de tiempo) a una FECHA Y HORA (LocalDateTime).
     * @param value Este es el número que Room saca de la base de datos.
     * @return Te devuelve la fecha y hora que tu app entiende.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        // Si el número es nulo, la fecha y hora también es nula.
        // Si hay un número, lo convierte. 'ZoneOffset.UTC' es para que la hora no se líe con los cambios de horario de verano/invierno o países.
        return value?.let { LocalDateTime.ofEpochSecond(it / 1000, (it % 1000 * 1_000_000).toInt(), ZoneOffset.UTC) }
    }

    /**
     * Regla para traducir una FECHA Y HORA (LocalDateTime) a un NÚMERO GRANDE (Long) para guardar en la base de datos.
     * @param date Esta es la fecha y hora que tu app quiere guardar.
     * @return Te devuelve el número que la base de datos guardará.
     */
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        // Si la fecha es nula, el número también es nulo.
        // Si no, la convierte a un número de milisegundos desde "el inicio de los tiempos" (1 de enero de 1970).
        return date?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }

    /**
     * Regla para traducir un NÚMERO (Long, que representa días) a una FECHA (LocalDate).
     * @param value Este es el número de días desde "el inicio de los tiempos" que saca de la base de datos.
     * @return Te devuelve la fecha que tu app entiende.
     */
    @TypeConverter
    fun fromLocalDate(value: Long?): LocalDate? {
        // Si el número es nulo, la fecha es nula. Si no, lo convierte a fecha.
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    /**
     * Regla para traducir una FECHA (LocalDate) a un NÚMERO (Long) para guardar en la base de datos.
     * @param date Esta es la fecha que tu app quiere guardar.
     * @return Te devuelve el número de días desde "el inicio de los tiempos" que la base de datos guardará.
     */
    @TypeConverter
    fun localDateToLong(date: LocalDate?): Long? {
        // Si la fecha es nula, el número es nulo. Si no, la convierte a número.
        return date?.toEpochDay()
    }
}