// C:/Users/ancal/OneDrive/Escritorio/BaskStats/app/src/main/java/com/example/baskstatsapp/EventsScreen.kt
package com.example.baskstatsapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Importación necesaria para items(list)
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember // Necesario para remember para asegurar que la creación del flow sea estable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.baskstatsapp.dao.EventDao
import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.EventType
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import com.example.baskstatsapp.viewmodel.EventViewModel
import kotlinx.coroutines.flow.flowOf // Necesario para flowOf
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    navController: NavController,
    eventViewModel: EventViewModel
) {
    // CORRECCIÓN: Directamente observa playerEvents del ViewModel
    // Este Flow ya se encarga de filtrar por el jugador logueado (establecido en MainActivity)
    val events by eventViewModel.playerEvents.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Mis Eventos",
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
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
                onClick = { navController.navigate("add_event_screen") },
                containerColor = PrimaryOrange,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "Añadir Evento")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightGrayBackground)
                    .padding(horizontal = 16.dp) // Relleno ajustado para una mejor consistencia
            ) {
                // CORRECCIÓN: events.isEmpty() ahora se resolverá correctamente
                if (events.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (MainActivity.currentLoggedInPlayerId == null) "Por favor, inicia sesión para ver tus eventos." else "No hay eventos registrados. Pulsa '+' para añadir uno.",
                            color = DarkText.copy(alpha = 0.7f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp), // Relleno vertical añadido
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // CORRECCIÓN: items(list) en lugar de items(list.size)
                        items(events) { eventItem -> // 'eventItem' aquí es directamente un objeto Event
                            EventListItem(
                                event = eventItem, // Pasa el objeto Evento
                                onClick = { clickedEvent -> // Usa un nombre explícito para el parámetro de la lambda
                                    // CORRECCIÓN: Usa eventId del modelo Event
                                    navController.navigate("event_detail_screen/${clickedEvent.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventListItem(event: Event, onClick: (Event) -> Unit) { // La firma de onClick es correcta aquí
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(event) }, // Pasa el objeto evento real al hacer clic
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = event.type.name.replace("_", " ").lowercase().capitalize(), // Formatea el tipo de evento
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                event.opponent?.let {
                    Text(
                        text = "vs. $it",
                        fontSize = 16.sp,
                        color = DarkText.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Text(
                    text = event.dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)),
                    fontSize = 14.sp,
                    color = DarkText.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}