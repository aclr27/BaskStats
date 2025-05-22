package com.example.baskstatsapp

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope() // Para abrir y cerrar el drawer

    // Eliminar el TextButton "Cerrar Sesión" de la TopAppBar, se moverá al Drawer
    // Se mantiene el icono del menú.
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = LightGrayBackground // Color de fondo del drawer
            ) {
                // Encabezado del Drawer (similar al logo de LoginScreen, o un texto simple)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Puedes usar tu logo aquí si quieres, o solo texto como en el ejemplo
                    // val image = painterResource(R.drawable.logo)
                    // Image(painter = image, contentDescription = "BaskStats Logo", modifier = Modifier.size(80.dp))
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
                Divider() // Separador visual

                // Elementos del menú
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = navController.currentDestination?.route == "home_screen", // Marca si está seleccionado
                    onClick = {
                        navController.navigate("home_screen") {
                            popUpTo(navController.graph.startDestinationId) // Vuelve al inicio de la pila, para evitar múltiples instancias de Home
                            launchSingleTop = true // Evita recrear la misma pantalla si ya está en la cima
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
                    selected = false, // TODO: Cambiar cuando tengamos EventosScreen
                    onClick = {
                        // TODO: navController.navigate("events_screen")
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
                    selected = false, // TODO: Cambiar cuando tengamos PerformanceSheetsScreen
                    onClick = {
                        // TODO: navController.navigate("performance_sheets_screen")
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
                    selected = false, // TODO: Cambiar cuando tengamos TeamsScreen
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

                Spacer(modifier = Modifier.weight(1f)) // Empuja el resto hacia abajo

                // Elemento "Configuración" (opcionalmente)
                NavigationDrawerItem(
                    label = { Text("Configuración") },
                    selected = false, // TODO: Cambiar cuando tengamos SettingsScreen
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

                // Elemento "Cerrar Sesión"
                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        // Lógica de cerrar sesión:
                        // 1. Limpiar cualquier estado de usuario (cuando tengamos persistencia)
                        // 2. Navegar a la pantalla de login y limpiar la pila de navegación
                        navController.navigate("login_screen") {
                            popUpTo("home_screen") { inclusive = true } // Elimina home_screen de la pila
                        }
                        scope.launch { drawerState.close() } // Cerrar el drawer después de navegar
                    },
                    icon = { Icon(Icons.Filled.Logout, contentDescription = "Cerrar Sesión") },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = LightGrayBackground,
                        unselectedTextColor = DarkText,
                        unselectedIconColor = DarkText
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(16.dp)) // Espacio al final del drawer
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
                            IconButton(onClick = { scope.launch { drawerState.open() } }) { // ¡Abrir el Drawer!
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Menú",
                                    tint = DarkText
                                )
                            }
                        },
                        // Se ha eliminado el botón "Cerrar Sesión" de aquí.
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
                                // Tarjetas de eventos recientes
                                EventCard(
                                    date = "10/11/2023, 18:00",
                                    description = "Puntos Totales: 90",
                                    type = "Partido"
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                EventCard(
                                    date = "08/11/2023, 19:00",
                                    description = "Puntos Totales: 75",
                                    type = "Entrenamiento"
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                EventCard(
                                    date = "05/11/2023, 18:00",
                                    description = "Puntos Totales: 107",
                                    type = "Partido"
                                )
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
                                // Tarjetas de fichas de rendimiento
                                PerformanceCard(
                                    date = "Fecha: 02/11/2023",
                                    description = "Asistencias: 8",
                                    points = "Puntos: 25"
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                PerformanceCard(
                                    date = "Fecha: 29/10/2023",
                                    description = "Asistencias: 7",
                                    points = "Puntos: 21"
                                )
                            }
                        }
                    }
                }
            )
        }
    )
}

@Composable
fun EventCard(date: String, description: String, type: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkText.copy(alpha = 0.7f)
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = DarkText
                )
            }
            Text(
                text = type,
                color = PrimaryOrange,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .background(PrimaryOrange.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun PerformanceCard(date: String, description: String, points: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkText.copy(alpha = 0.7f)
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = DarkText
                )
            }
            Text(
                text = points,
                color = PrimaryOrange,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .background(PrimaryOrange.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
