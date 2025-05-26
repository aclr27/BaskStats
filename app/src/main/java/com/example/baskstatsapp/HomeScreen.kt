package com.example.baskstatsapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import kotlinx.coroutines.launch

import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.EventType
import com.example.baskstatsapp.model.Player
import com.example.baskstatsapp.model.PlayerStats
import com.example.baskstatsapp.model.PerformanceSheet // Corregido a PerformanceSheet
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Datos de prueba (Mock Data) con los nuevos modelos
    val currentPlayer = remember { Player(id = "player1", name = "Tu Jugador", number = 23, position = "Escolta") }

    // Datos de ejemplo para eventos y sus estadísticas individuales
    val sampleEventsAndStats = remember {
        listOf(
            Pair(
                Event(
                    id = "e1",
                    type = EventType.MATCH,
                    dateTime = LocalDateTime.of(2023, 11, 10, 18, 0),
                    opponent = "Rival B",
                    teamScore = 90,
                    opponentScore = 85,
                    notes = "Partido de liga"
                ),
                PlayerStats(
                    playerId = "player1",
                    eventId = "e1",
                    points = 25,
                    assists = 8,
                    totalRebounds = 5,
                    offensiveRebounds = 1,
                    defensiveRebounds = 4,
                    steals = 2,
                    blocks = 1,
                    turnovers = 3,
                    fouls = 2,
                    twoPointersMade = 7,
                    twoPointersAttempted = 12,
                    threePointersMade = 3,
                    threePointersAttempted = 6,
                    freeThrowsMade = 2,
                    freeThrowsAttempted = 2,
                    minutesPlayed = 30,
                    plusMinus = 15
                )
            ),
            Pair(
                Event(
                    id = "e2",
                    type = EventType.TRAINING,
                    dateTime = LocalDateTime.of(2023, 11, 8, 19, 0),
                    notes = "Entrenamiento de tiro"
                ),
                PlayerStats(
                    playerId = "player1",
                    eventId = "e2",
                    points = 15,
                    assists = 3,
                    totalRebounds = 2,
                    offensiveRebounds = 0,
                    defensiveRebounds = 2,
                    steals = 0,
                    blocks = 0,
                    turnovers = 1,
                    fouls = 0,
                    twoPointersMade = 5,
                    twoPointersAttempted = 10,
                    threePointersMade = 1,
                    threePointersAttempted = 5,
                    freeThrowsMade = 2,
                    freeThrowsAttempted = 2,
                    minutesPlayed = 60,
                    plusMinus = 0 // No aplica para entrenamiento
                )
            ),
            Pair(
                Event(
                    id = "e3",
                    type = EventType.MATCH,
                    dateTime = LocalDateTime.of(2023, 11, 5, 18, 0),
                    opponent = "Rival A",
                    teamScore = 107,
                    opponentScore = 98,
                    notes = "Partido amistoso"
                ),
                PlayerStats(
                    playerId = "player1",
                    eventId = "e3",
                    points = 30,
                    assists = 7,
                    totalRebounds = 10,
                    offensiveRebounds = 3,
                    defensiveRebounds = 7,
                    steals = 3,
                    blocks = 2,
                    turnovers = 4,
                    fouls = 3,
                    twoPointersMade = 9,
                    twoPointersAttempted = 15,
                    threePointersMade = 4,
                    threePointersAttempted = 8,
                    freeThrowsMade = 0,
                    freeThrowsAttempted = 0,
                    minutesPlayed = 35,
                    plusMinus = 20
                )
            )
        )
    }

    // Datos de ejemplo para fichas de rendimiento
    val samplePerformanceSheets = remember {
        listOf(
            PerformanceSheet(
                id = "ps1",
                date = LocalDate.of(2023, 11, 2),
                playerId = "player1",
                eventId = null, // Podría ser nulo si es una ficha general
                points = 25,
                assists = 8,
                rebounds = 5,
                steals = 2,
                blocks = 1,
                turnovers = 3,
                freeThrowsMade = 2,
                freeThrowsAttempted = 2
            ),
            PerformanceSheet(
                id = "ps2",
                date = LocalDate.of(2023, 10, 29),
                playerId = "player1",
                eventId = null,
                points = 21,
                assists = 7,
                rebounds = 3,
                steals = 1,
                blocks = 0,
                turnovers = 2,
                freeThrowsMade = 1,
                freeThrowsAttempted = 2
            )
        )
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = LightGrayBackground
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Puedes usar tu logo aquí si quieres, o solo texto como en el ejemplo
                    // Image(painter = painterResource(R.drawable.logo), contentDescription = "BaskStats Logo", modifier = Modifier.size(80.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "BaskStats",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Menú Principal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkText.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Divider()

                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = navController.currentDestination?.route == "home_screen",
                    onClick = {
                        navController.navigate("home_screen") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = PrimaryOrange,
                        selectedTextColor = Color.White,
                        selectedIconColor = Color.White,
                        unselectedContainerColor = LightGrayBackground,
                        unselectedTextColor = DarkText,
                        unselectedIconColor = DarkText
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Eventos") },
                    selected = navController.currentDestination?.route == "events_screen", // Podrías marcarla como seleccionada si ya estás ahí
                    onClick = {
                        navController.navigate("events_screen") {
                            // Limpiar la pila de navegación para no acumular Home Screens
                            popUpTo("home_screen") { saveState = true } // Guarda el estado de Home
                            launchSingleTop = true // Evita múltiples copias de EventsScreen
                            restoreState = true // Restaura el estado si ya existía
                        }
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Filled.DateRange, contentDescription = "Eventos") },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = LightGrayBackground,
                        unselectedTextColor = DarkText,
                        unselectedIconColor = DarkText
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Fichas de Rendimiento") },
                    selected = navController.currentDestination?.route == "performance_sheets_screen",
                    onClick = {
                        navController.navigate("performance_sheets_screen") {
                            popUpTo("home_screen") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Filled.Star, contentDescription = "Fichas de Rendimiento") },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = LightGrayBackground,
                        unselectedTextColor = DarkText,
                        unselectedIconColor = DarkText
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Equipos") },
                    selected = false,
                    onClick = {
                        // TODO: navController.navigate("teams_screen")
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Filled.Group, contentDescription = "Equipos") },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = LightGrayBackground,
                        unselectedTextColor = DarkText,
                        unselectedIconColor = DarkText
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                NavigationDrawerItem(
                    label = { Text("Configuración") },
                    selected = false,
                    onClick = {
                        // TODO: navController.navigate("settings_screen")
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Configuración") },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = LightGrayBackground,
                        unselectedTextColor = DarkText,
                        unselectedIconColor = DarkText
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        navController.navigate("login_screen") {
                            popUpTo("home_screen") { inclusive = true }
                        }
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Filled.Logout, contentDescription = "Cerrar Sesión") },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = LightGrayBackground,
                        unselectedTextColor = DarkText,
                        unselectedIconColor = DarkText
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        content = {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = "Inicio",
                                color = DarkText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Menú",
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
                        onClick = { /* TODO: Acción al pulsar el FAB */ },
                        containerColor = PrimaryOrange,
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Filled.Add, "Añadir")
                    }
                },
                content = { paddingValues ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .background(LightGrayBackground)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                            // Botones de alternancia "Partidos/Entrenamientos" y "Fichas"
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                                    .background(
                                        color = Color(0xFFE0E0E0),
                                        shape = RoundedCornerShape(24.dp)
                                    )
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { /* TODO: Filtrar por Partidos/Entrenamientos */ },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PrimaryOrange,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text("Partidos/Entrenamientos", fontSize = 14.sp)
                                }
                                Button(
                                    onClick = { /* TODO: Filtrar por Fichas */ },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = DarkText
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(0.dp),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text("Fichas", fontSize = 14.sp)
                                }
                            }
                        }

                        // Sección "Eventos Recientes"
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                            ) {
                                Text(
                                    text = "Eventos Recientes",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DarkText
                                    ),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                // Renderiza las tarjetas de eventos usando los nuevos datos
                                sampleEventsAndStats.forEach { (event, playerStats) ->
                                    EventItemCard(event = event, playerStats = playerStats, playerName = currentPlayer.name)
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }

                        // Sección "Fichas de Rendimiento"
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                            ) {
                                Text(
                                    text = "Fichas de Rendimiento",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DarkText
                                    ),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                samplePerformanceSheets.forEach { sheet ->
                                    PerformanceItemCard(performanceSheet = sheet, playerName = currentPlayer.name)
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                    }
                }
            )
        }
    )
}


// Muestra las Estas de un jugador.
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventItemCard(event: Event, playerStats: PlayerStats, playerName: String, modifier:Modifier = Modifier) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = event.dateTime.format(formatter),
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkText.copy(alpha = 0.7f)
                    )
                    Text(
                        text = if (event.type == EventType.MATCH && event.opponent != null) "vs ${event.opponent}" else event.notes ?: "Entrenamiento",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = DarkText
                    )
                }
                Text(
                    text = if (event.type == EventType.MATCH) "Partido" else "Entrenamiento",
                    color = PrimaryOrange,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .background(PrimaryOrange.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(8.dp))
            // Estadísticas individuales destacadas para el jugador
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem("Pts", playerStats.points) // Abreviado para que quepa mejor
                StatItem("Asis", playerStats.assists) // Abreviado
                StatItem("Reb", playerStats.totalRebounds) // Usar totalRebounds
                StatItem("Rob", playerStats.steals) // Abreviado
                StatItem("Bloq", playerStats.blocks) // Abreviado
            }
        }
    }
}

/**
 * Fichas de rendimiento que dan más detalles del jugador
 */

// NUEVA COMPOSABLE para Fichas de Rendimiento (más detalles del jugador)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PerformanceItemCard(performanceSheet: PerformanceSheet, playerName: String, modifier: Modifier = Modifier) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
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
            Text(
                text = "Ficha de ${playerName} - ${performanceSheet.date.format(dateFormatter)}",
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
                StatItem("Reb", performanceSheet.rebounds)
                StatItem("Rob", performanceSheet.steals)
                StatItem("Bloq", performanceSheet.blocks)
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = DarkText.copy(alpha = 0.7f))
        Text(text = value.toString(), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = PrimaryOrange)
    }
}
