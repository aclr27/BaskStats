// app/src/main/java/com/example/baskstatsapp/screens/goals/AddGoalScreen.kt
package com.example.baskstatsapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.baskstatsapp.MainActivity
import com.example.baskstatsapp.data.model.Goal
import com.example.baskstatsapp.data.model.GoalFrequency
import com.example.baskstatsapp.data.model.GoalType
import com.example.baskstatsapp.viewmodel.GoalViewModel
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalScreen(navController: NavController, goalViewModel: GoalViewModel) {
    // description ya no es editable directamente, se genera
    var typeExpanded by remember { mutableStateOf(false) }
    var selectedGoalType by remember { mutableStateOf(GoalType.POINTS) }
    var quantity by remember { mutableStateOf("") } // sigue siendo String para la entrada de texto
    var frequencyExpanded by remember { mutableStateOf(false) }
    var selectedGoalFrequency by remember { mutableStateOf(GoalFrequency.PER_GAME) }
    var notes by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Objetivo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Ya no hay un campo de texto para la descripción, se genera
            // Text(text = "Descripción Del Objetivo", style = MaterialTheme.typography.titleMedium)
            // Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = !typeExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedGoalType.toLocalizedText(),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo De Objetivo") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded)
                    }
                )
                ExposedDropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false }
                ) {
                    GoalType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.toLocalizedText()) },
                            onClick = {
                                selectedGoalType = type
                                typeExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = quantity,
                onValueChange = { newValue ->
                    // Permitir números enteros o decimales
                    if (newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                        quantity = newValue
                    }
                },
                label = {
                    Text(
                        when (selectedGoalType) {
                            GoalType.FIELD_GOAL_PERCENTAGE, GoalType.THREE_POINT_PERCENTAGE, GoalType.FREE_THROW_PERCENTAGE -> "Porcentaje (%)"
                            GoalType.TURNOVERS -> "Cantidad (intentar reducir)"
                            GoalType.SPEED_SPRINT, GoalType.VERTICAL_JUMP, GoalType.ENDURANCE -> "Medida"
                            else -> "Cantidad"
                        }
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = frequencyExpanded,
                onExpandedChange = { frequencyExpanded = !frequencyExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedGoalFrequency.toLocalizedText(),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Frecuencia") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = frequencyExpanded)
                    }
                )
                ExposedDropdownMenu(
                    expanded = frequencyExpanded,
                    onDismissRequest = { frequencyExpanded = false }
                ) {
                    GoalFrequency.entries.forEach { frequency ->
                        DropdownMenuItem(
                            text = { Text(frequency.toLocalizedText()) },
                            onClick = {
                                selectedGoalFrequency = frequency
                                frequencyExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notas Adicionales") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val currentPlayersId = MainActivity.currentLoggedInPlayerId
                    val targetDouble = quantity.replace(",", ".").toDoubleOrNull() // Manejar coma como separador decimal
                    if (currentPlayersId != null && targetDouble != null) {
                        val newGoal = Goal(
                            playerId = currentPlayersId,
                            description = generateGoalDescription(selectedGoalType, targetDouble, selectedGoalFrequency),
                            type = selectedGoalType,
                            targetQuantity = targetDouble,
                            frequency = selectedGoalFrequency,
                            notes = notes.ifBlank { null }
                        )
                        goalViewModel.addGoal(newGoal)
                        navController.popBackStack()
                    } else {
                        // TODO: Mostrar un Toast o SnackBar al usuario indicando que falta información o que la cantidad es inválida
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Color verde
            ) {
                Text("Guardar")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFFCCCCCC)) // Color gris
            ) {
                Text("Cancelar", color = Color.Black)
            }
        }
    }
}

// Funciones de extensión para localizar los textos de los enums
@Composable
fun GoalType.toLocalizedText(): String {
    return when (this) {
        GoalType.POINTS -> "Puntos"
        GoalType.REBOUNDS -> "Rebotes"
        GoalType.ASSISTS -> "Asistencias"
        GoalType.STEALS -> "Robos"
        GoalType.BLOCKS -> "Tapones"
        GoalType.TURNOVERS -> "Pérdidas de Balón"
        GoalType.FIELD_GOAL_PERCENTAGE -> "Porcentaje Tiros Campo"
        GoalType.THREE_POINT_PERCENTAGE -> "Porcentaje Tiros de 3"
        GoalType.FREE_THROW_PERCENTAGE -> "Porcentaje Tiros Libres"
        GoalType.AVERAGE_POINTS -> "Promedio de Puntos"
        GoalType.AVERAGE_REBOUNDS -> "Promedio de Rebotes"
        GoalType.AVERAGE_ASSISTS -> "Promedio de Asistencias"
        GoalType.WINS -> "Victorias"
        GoalType.CONSECUTIVE_WINS -> "Victorias Consecutivas"
        GoalType.MINUTES_PLAYED -> "Minutos Jugados"
        GoalType.SPEED_SPRINT -> "Velocidad Sprint"
        GoalType.VERTICAL_JUMP -> "Salto Vertical"
        GoalType.ENDURANCE -> "Resistencia"
    }
}

@Composable
fun GoalFrequency.toLocalizedText(): String {
    return when (this) {
        GoalFrequency.PER_GAME -> "Por Partido"
        GoalFrequency.PER_MONTH -> "Por Mes"
        GoalFrequency.PER_SEASON -> "Por Temporada"
        GoalFrequency.OVERALL -> "General"
        GoalFrequency.LAST_X_GAMES -> "En los últimos X partidos"
        GoalFrequency.NEXT_X_GAMES -> "En los próximos X partidos"
        GoalFrequency.SPECIFIC_GAME -> "En un Partido Específico"
        GoalFrequency.PERSONAL_BEST -> "Mejorar Récord Personal"
        GoalFrequency.MAINTAIN_PERCENTAGE -> "Mantener Porcentaje"
    }
}

// Función para generar la descripción del objetivo automáticamente
fun generateGoalDescription(type: GoalType, quantity: Double, frequency: GoalFrequency): String {
    val df = DecimalFormat("#.##") // Formateador para dos decimales
    val qStr = df.format(quantity)

    return when (type) {
        GoalType.POINTS -> "Anotar $qStr puntos"
        GoalType.REBOUNDS -> "Capturar $qStr rebotes"
        GoalType.ASSISTS -> "Dar $qStr asistencias"
        GoalType.STEALS -> "Realizar $qStr robos"
        GoalType.BLOCKS -> "Realizar $qStr tapones"
        GoalType.TURNOVERS -> "Tener menos de $qStr pérdidas de balón"
        GoalType.FIELD_GOAL_PERCENTAGE -> "Alcanzar el $qStr% de acierto en tiros de campo"
        GoalType.THREE_POINT_PERCENTAGE -> "Alcanzar el $qStr% de acierto en tiros de 3"
        GoalType.FREE_THROW_PERCENTAGE -> "Alcanzar el $qStr% de acierto en tiros libres"
        GoalType.AVERAGE_POINTS -> "Promediar $qStr puntos"
        GoalType.AVERAGE_REBOUNDS -> "Promediar $qStr rebotes"
        GoalType.AVERAGE_ASSISTS -> "Promediar $qStr asistencias"
        GoalType.WINS -> "Conseguir $qStr victorias"
        GoalType.CONSECUTIVE_WINS -> "Conseguir $qStr victorias consecutivas"
        GoalType.MINUTES_PLAYED -> "Jugar $qStr minutos"
        GoalType.SPEED_SPRINT -> "Alcanzar $qStr segundos en sprint"
        GoalType.VERTICAL_JUMP -> "Alcanzar $qStr cm en salto vertical"
        GoalType.ENDURANCE -> "Mantener la resistencia durante $qStr minutos"
    } + when (frequency) {
        GoalFrequency.PER_GAME -> " por partido"
        GoalFrequency.PER_MONTH -> " este mes"
        GoalFrequency.PER_SEASON -> " esta temporada"
        GoalFrequency.OVERALL -> "" // La descripción ya lo implica
        GoalFrequency.LAST_X_GAMES -> " en los últimos $qStr partidos" // qStr aquí es la cantidad de partidos, no el objetivo
        GoalFrequency.NEXT_X_GAMES -> " en los próximos $qStr partidos" // qStr aquí es la cantidad de partidos, no el objetivo
        GoalFrequency.SPECIFIC_GAME -> " en un partido específico" // Necesitaría un selector de partido
        GoalFrequency.PERSONAL_BEST -> " como récord personal"
        GoalFrequency.MAINTAIN_PERCENTAGE -> "" // Implícito en el tipo de objetivo
    }
}