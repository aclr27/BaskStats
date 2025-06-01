package com.example.baskstatsapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.EventType
import com.example.baskstatsapp.model.PerformanceSheet
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import java.time.format.DateTimeFormatter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import java.time.Instant // Añadir esta importación
import java.time.ZoneId // Añadir esta importación

/**
 * Fichas de rendimiento que dan más detalles del jugador
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PerformanceItemCard(performanceSheet: PerformanceSheet,
                        playerName: String,
                        modifier: Modifier = Modifier) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Corrección: Acceder a eventDate y convertirlo a LocalDate
    val performanceLocalDate = Instant.ofEpochMilli(performanceSheet.eventDate)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Corrección: Usar performanceLocalDate formateado
            Text(
                text = "Ficha de ${playerName} - ${performanceLocalDate.format(dateFormatter)}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = DarkText,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Divider(color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem("Pts", performanceSheet.points)
                StatItem("Asis", performanceSheet.assists)
                // Corrección: Sumar offensiveRebounds y defensiveRebounds para mostrar los rebotes totales
                StatItem("Reb", performanceSheet.offensiveRebounds + performanceSheet.defensiveRebounds)
                StatItem("Rob", performanceSheet.steals)
                StatItem("Bloq", performanceSheet.blocks)
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.widthIn(min = 70.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = DarkText.copy(alpha = 0.6f)
        )
        Text(
            text = value.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryOrange
        )
    }
}



@Composable
fun StatRow(label: String, value: String, percentage: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = DarkText)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = PrimaryOrange)
            percentage?.let {
                Text(text = " ($it)", style = MaterialTheme.typography.bodySmall, color = DarkText.copy(alpha = 0.7f), modifier = Modifier.padding(start = 4.dp))
            }
        }
    }
}

@Composable
fun EventItemCard(
    event: Event,
    playerStats: PerformanceSheet?,
    playerName: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Fecha y hora del evento
            Text(
                text = "${event.dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Tipo de evento y Rival (si es partido)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = if (event.type == EventType.MATCH) "Partido" else "Entrenamiento",
                    fontSize = 14.sp,
                    color = PrimaryOrange,
                    fontWeight = FontWeight.Bold
                )
                if (event.type == EventType.MATCH && event.opponent != null) {
                    Text(
                        text = "vs ${event.opponent}",
                        fontSize = 14.sp,
                        color = DarkText
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Puntuación del partido (si es partido)
            if (event.type == EventType.MATCH && event.teamScore != null && event.opponentScore != null) {
                Text(
                    text = "Resultado: ${event.teamScore} - ${event.opponentScore}",
                    fontSize = 14.sp,
                    color = DarkText
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Estadísticas clave del jugador
            Text(
                text = "$playerName - Puntos: ${playerStats?.points}",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
            Text(
                text = "Asistencias: ${playerStats?.assists}",
                fontSize = 13.sp,
                color = DarkText
            )
            // Corrección: Sumar offensiveRebounds y defensiveRebounds para mostrar los rebotes totales
            Text(
                text = "Rebotes: ${playerStats?.let { it.offensiveRebounds + it.defensiveRebounds } ?: 0}",
                fontSize = 13.sp,
                color = DarkText
            )
        }
    }
}