// app/src/main/java/com/example/baskstatsapp/screens/goals/GoalsScreen.kt
package com.example.baskstatsapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu // Asegúrate de tener esta importación si usas el icono de menú
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton // Asegúrate de tener esta importación
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.baskstatsapp.data.model.Goal
import com.example.baskstatsapp.data.model.GoalStatus
import com.example.baskstatsapp.formatDate // Importa tu función de formato de fecha
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import com.example.baskstatsapp.viewmodel.GoalViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(navController: NavController, goalViewModel: GoalViewModel) {
    val goals by goalViewModel.goals.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Establecer Objetivos",
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                ) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = DarkText
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = LightGrayBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_goal_screen") },
                containerColor = PrimaryOrange,
                contentColor = Color.White) {
                Icon(Icons.Filled.Add, "Añadir nuevo objetivo")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (goals.isEmpty()) {
                Text(
                    text = "No tienes objetivos registrados. Pulsa '+' para añadir uno.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(goals) { goal ->
                        GoalCard(goal = goal) {
                            navController.navigate("edit_goal_screen/${goal.goalId}")
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GoalCard(goal: Goal, onClick: (Goal) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(goal) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDate(goal.creationDate), // Usa la función formatDate
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = goal.status.toLocalizedText(), // Usa la función de extensión
                    style = MaterialTheme.typography.labelMedium,
                    color = getStatusColor(goal.status)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = goal.description, // Esta es la descripción generada que se mostrará
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            // Opcional: Mostrar notas adicionales si existen y son relevantes
            if (goal.notes != null && goal.notes.isNotBlank()) {
                Text(
                    text = "Notas: ${goal.notes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

// Estas funciones de extensión y utilidad deben estar definidas en un lugar accesible
// (por ejemplo, en el mismo archivo o en un archivo de utilidades importado).
@Composable
fun GoalStatus.toLocalizedText(): String {
    return when (this) {
        GoalStatus.IN_PROGRESS -> "En Curso"
        GoalStatus.COMPLETED -> "Completado"
        GoalStatus.FAILED -> "Fallido"
        GoalStatus.CANCELLED -> "Cancelado"
    }
}

@Composable
fun getStatusColor(status: GoalStatus): Color {
    return when (status) {
        GoalStatus.IN_PROGRESS -> Color(0xFFFFA726) // Naranja
        GoalStatus.COMPLETED -> Color(0xFF66BB6A) // Verde
        GoalStatus.FAILED -> MaterialTheme.colorScheme.error // Rojo por defecto del tema
        GoalStatus.CANCELLED -> MaterialTheme.colorScheme.onSurfaceVariant // Gris
    }
}


fun formatDate(timestamp: Long): String {
     val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
     return sdf.format(Date(timestamp))
}