package com.example.baskstatsapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
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
                    IconButton(onClick = { /* TODO: Abrir Drawer de navegación */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menú",
                            tint = DarkText
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = LightGrayBackground // Fondo de la TopAppBar igual al fondo de la pantalla
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Acción al pulsar el FAB */ },
                containerColor = PrimaryOrange, // Color naranja para el FAB
                contentColor = Color.White // Icono blanco
            ) {
                Icon(Icons.Filled.Add, "Añadir") // Icono de añadir
            }
        },
        // El contenido principal de la pantalla
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Aplica el padding de la TopAppBar y FAB
                    .background(LightGrayBackground) // Fondo general de la pantalla
                    .padding(horizontal = 16.dp), // Padding horizontal para el contenido
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp)) // Espacio debajo de la TopAppBar

                    // Botones de alternancia "Partidos/Entrenamientos" y "Fichas"
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                            .background(
                                color = Color(0xFFE0E0E0), // Fondo gris claro para el contenedor de botones
                                shape = RoundedCornerShape(24.dp) // Redondeo para el contenedor
                            )
                            .padding(4.dp), // Padding interno para los botones
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { /* TODO: Filtrar por Partidos/Entrenamientos */ },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryOrange, // Naranja para el seleccionado
                                contentColor = Color.White // Texto blanco
                            ),
                            shape = RoundedCornerShape(20.dp) // Redondeo de los botones
                        ) {
                            Text("Partidos/Entrenamientos", fontSize = 14.sp)
                        }
                        Button(
                            onClick = { /* TODO: Filtrar por Fichas */ },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent, // Transparente para el no seleccionado
                                contentColor = DarkText // Texto oscuro
                            ),
                            elevation = ButtonDefaults.buttonElevation(0.dp), // Sin sombra para no seleccionado
                            shape = RoundedCornerShape(20.dp) // Redondeo de los botones
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
                            .padding(bottom = 24.dp) // Espacio entre secciones
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
                            .padding(bottom = 24.dp) // Espacio para el FAB
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

@Composable
fun EventCard(date: String, description: String, type: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp), // Redondeo de la tarjeta
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Fondo blanco para las tarjetas
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Espacio entre fecha/desc y tipo
        ) {
            Column {
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkText.copy(alpha = 0.7f) // Texto de fecha/hora un poco más tenue
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = DarkText
                )
            }
            Text(
                text = type,
                color = PrimaryOrange, // Texto del tipo (Partido/Entrenamiento) naranja
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .background(PrimaryOrange.copy(alpha = 0.1f), RoundedCornerShape(8.dp)) // Fondo naranja tenue
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun PerformanceCard(date: String, description: String, points: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp), // Redondeo de la tarjeta
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Fondo blanco para las tarjetas
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
                    color = DarkText.copy(alpha = 0.7f) // Texto de fecha un poco más tenue
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = DarkText
                )
            }
            Text(
                text = points,
                color = PrimaryOrange, // Texto de puntos naranja
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .background(PrimaryOrange.copy(alpha = 0.1f), RoundedCornerShape(8.dp)) // Fondo naranja tenue
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewHomeScreen() {
    BaskStatsAppTheme {
        HomeScreen()
    }
}