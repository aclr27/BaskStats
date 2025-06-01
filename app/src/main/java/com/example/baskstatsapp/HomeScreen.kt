// app/src/main/java/com/example/baskstatsapp/HomeScreen.kt
package com.example.baskstatsapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Importar para usar items en LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import kotlinx.coroutines.launch

// Importaciones de los modelos y ViewModels
import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.PerformanceSheet
import com.example.baskstatsapp.viewmodel.EventViewModel
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel

// Importaciones para observar Flow
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

// Asegúrate de que estas importaciones son correctas para tus composables de tarjetas
// Si EventItemCard y PerformanceItemCard están en el mismo paquete, no necesitarán importación explícita
// Si están en un subpaquete, por ejemplo, 'composables', las importaciones serían así:

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    eventViewModel: EventViewModel,
    performanceSheetViewModel: PerformanceSheetViewModel,
    onLogout: () -> Unit // Callback para cerrar sesión
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Obtener el ID del jugador logueado desde MainActivity (temporal para este ejemplo)
    // Una implementación más robusta usaría un ViewModel compartido o DataStore
    val currentLoggedInPlayerId = MainActivity.currentLoggedInPlayerId

    // Observar los eventos y fichas de rendimiento del jugador logueado
    // Nota: Necesitarás implementar estos métodos en tus ViewModels para filtrar por playerId
    val playerEvents by eventViewModel.getEventsForPlayer(currentLoggedInPlayerId ?: -1L)
        .collectAsState(initial = emptyList())

    val playerPerformanceSheets by performanceSheetViewModel.getPerformanceSheetsForPlayer(currentLoggedInPlayerId ?: -1L)
        .collectAsState(initial = emptyList())

    // O si quieres TODOS los eventos/fichas (sin filtrar por jugador logueado):
    // val allEvents by eventViewModel.allEvents.collectAsState(initial = emptyList())
    // val allPerformanceSheets by performanceSheetViewModel.allPerformanceSheets.collectAsState(initial = emptyList())


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
                    selected = navController.currentDestination?.route == "events_screen",
                    onClick = {
                        navController.navigate("events_screen") {
                            popUpTo("home_screen") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
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
                        scope.launch { drawerState.close() } // Cierra el Drawer antes de cerrar sesión
                        onLogout() // Llama al callback para limpiar la sesión y navegar al login
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

                        // Sección "Eventos Recientes" (usando datos reales del ViewModel)
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
                                if (playerEvents.isEmpty()) {
                                    Text("No hay eventos registrados para este jugador.", color = DarkText.copy(alpha = 0.6f))
                                } else {
                                    playerEvents.forEach { event ->
                                        // Para el playerStats en EventItemCard, si realmente necesitas la PerformanceSheet
                                        // asociada a ese evento Y ese jugador, tendrás que obtenerla.
                                        // Por ahora, para evitar errores, si no hay PerformanceSheet asociada directamente,
                                        // podrías pasar 'null' o una PerformanceSheet vacía por defecto.
                                        // La solución ideal sería que EventItemCard solo requiera el Event y un PlayerId,
                                        // y que la propia tarjeta obtenga los datos de rendimiento si los necesita,
                                        // o que tengas un objeto de dominio combinado (e.g., EventWithPerformance)
                                        // en tu ViewModel/DAO.

                                        // Buscamos la PerformanceSheet para el evento y el jugador actual
                                        val associatedPerformanceSheet = playerPerformanceSheets.find {
                                            it.eventId == event.id && it.playerId == currentLoggedInPlayerId
                                        }
                                        EventItemCard(
                                            event = event,
                                            playerStats = associatedPerformanceSheet,
                                            playerName = "Tu Jugador" // Aquí podrías cargar el nombre real del jugador desde PlayerViewModel
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                            }
                        }

                        // Sección "Fichas de Rendimiento" (usando datos reales del ViewModel)
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
                                if (playerPerformanceSheets.isEmpty()) {
                                    Text("No hay fichas de rendimiento para este jugador.", color = DarkText.copy(alpha = 0.6f))
                                } else {
                                    playerPerformanceSheets.forEach { sheet ->
                                        PerformanceItemCard(
                                            performanceSheet = sheet,
                                            playerName = "Tu Jugador"
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    )
}