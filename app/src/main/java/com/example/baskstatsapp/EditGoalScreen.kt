// app/src/main/java/com/example/baskstatsapp/screens/goals/EditGoalScreen.kt
package com.example.baskstatsapp

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.baskstatsapp.data.model.Goal
import com.example.baskstatsapp.data.model.GoalFrequency
import com.example.baskstatsapp.data.model.GoalStatus
import com.example.baskstatsapp.data.model.GoalType
import com.example.baskstatsapp.viewmodel.GoalViewModel
import java.text.DecimalFormat

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGoalScreen(navController: NavController, goalViewModel: GoalViewModel, goalId: Long) {
    var goal by remember { mutableStateOf<Goal?>(null) }

    // Ya no es editable directamente, se genera
    // var description by remember { mutableStateOf("") }
    var typeExpanded by remember { mutableStateOf(false) }
    var selectedGoalType by remember { mutableStateOf(GoalType.POINTS) }
    var quantity by remember { mutableStateOf("") } // Sigue siendo String para la entrada de texto
    var frequencyExpanded by remember { mutableStateOf(false) }
    var selectedGoalFrequency by remember { mutableStateOf(GoalFrequency.PER_GAME) }
    var notes by remember { mutableStateOf("") }
    var statusExpanded by remember { mutableStateOf(false) }
    var selectedGoalStatus by remember { mutableStateOf(GoalStatus.IN_PROGRESS) }

    // Formateador para la cantidad
    val df = remember { DecimalFormat("#.##") }


    // Cargar el objetivo al iniciar la pantalla
    LaunchedEffect(goalId) {
        val loadedGoal = goalViewModel.getGoalById(goalId)
        loadedGoal?.let {
            goal = it
            // description = it.description // No lo necesitamos si se genera
            selectedGoalType = it.type
            quantity = df.format(it.targetQuantity) // Formatear Double a String
            selectedGoalFrequency = it.frequency
            notes = it.notes ?: ""
            selectedGoalStatus = it.status
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Objetivo") },
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
            // Ya no hay un campo de texto para la descripción
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

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = !statusExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedGoalStatus.toLocalizedText(),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded)
                    }
                )
                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    GoalStatus.entries.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status.toLocalizedText()) },
                            onClick = {
                                selectedGoalStatus = status
                                statusExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    goal?.let { existingGoal ->
                        val targetDouble = quantity.replace(",", ".").toDoubleOrNull()
                        if (targetDouble != null) {
                            val updatedGoal = existingGoal.copy(
                                description = generateGoalDescription(selectedGoalType, targetDouble, selectedGoalFrequency),
                                type = selectedGoalType,
                                targetQuantity = targetDouble,
                                frequency = selectedGoalFrequency,
                                notes = notes.ifBlank { null },
                                status = selectedGoalStatus,
                                completionDate = if (selectedGoalStatus == GoalStatus.COMPLETED) System.currentTimeMillis() else null
                            )
                            goalViewModel.updateGoal(updatedGoal)
                            navController.popBackStack()
                        } else {
                            // TODO: Mostrar un Toast o SnackBar indicando que la cantidad es inválida
                        }
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

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    goal?.let {
                        goalViewModel.deleteGoal(it)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)) // Color rojo
            ) {
                Text("Eliminar")
            }
        }
    }
}