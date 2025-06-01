// app/src/main/java/com/example/baskstatsapp/screens/PlayerStatsScreen.kt
package com.example.baskstatsapp.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.LightGrayBackground
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import com.example.baskstatsapp.viewmodel.EventViewModel
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel
import com.example.baskstatsapp.model.Event
import com.example.baskstatsapp.model.PerformanceSheet

import com.aayushatharva.composecharts.line.LineChart
import com.aayushatharva.composecharts.line.LineChartData
import com.aayushatharva.composecharts.line.LineChartEntry
import com.aayushatharva.composecharts.line.LineChartPath
import com.aayushatharva.composecharts.line.LineChartPathEffect
import com.aayushatharva.composecharts.line.LineChartPoints
import com.aayushatharva.composecharts.line.LineChartType
import com.aayushatharva.composecharts.line.renderer.line.LineDrawer
import com.aayushatharva.composecharts.line.renderer.line.LineShader
import com.aayushatharva.composecharts.line.renderer.point.PointDrawer
import com.aayushatharva.composecharts.line.renderer.xaxis.XAxisDrawer
import com.aayushatharva.composecharts.line.renderer.yaxis.YAxisDrawer

// Importaciones para el gráfico de Compose Charts
import java.time.format.DateTimeFormatter // <--- AÑADIDO PARA LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerStatsScreen(
    navController: NavController,
    performanceSheetViewModel: PerformanceSheetViewModel,
    eventViewModel: EventViewModel
) {
    // Opciones de estadísticas disponibles para el filtro del gráfico
    val statOptions = remember {
        listOf(
            "Puntos",
            "Asistencias",
            "Rebotes", // Suma de defensivos y ofensivos
            "Robos",
            "Tapones",
            "Faltas"
            // Puedes añadir más si tienes más estadísticas en PerformanceSheet
        )
    }

    // Estado para la estadística seleccionada en el filtro del gráfico
    var selectedStatFilter by remember { mutableStateOf(statOptions.first()) }

    // Datos para el gráfico, observados del ViewModel
    val chartDataPoints by performanceSheetViewModel.getStatDataForChart(selectedStatFilter)
        .collectAsState(initial = emptyList())

    // Últimas fichas de rendimiento
    val lastPerformanceSheets by performanceSheetViewModel.getLastNPerformanceSheets(2).collectAsState(initial = emptyList())
    // Todas las fichas de rendimiento para la lógica "Ver Toda"
    val allPerformanceSheetsTotal by performanceSheetViewModel.allPerformanceSheets.collectAsState(initial = emptyList())

    // Estadísticas totales del jugador
    val totalStats by performanceSheetViewModel.totalPlayerStats.collectAsState()


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar( // Usar CenterAlignedTopAppBar para centrar el título
                title = {
                    Text(
                        text = "Estadísticas",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors( // Usar el tema de TopAppBarDefaults
                    containerColor = LightGrayBackground
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(LightGrayBackground)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Resumen General",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Cards de Resumen General
            item {
                StatsSummaryCard(
                    title = "Puntos Totales",
                    value = totalStats["totalPoints"].toString()
                )
            }
            item {
                StatsSummaryCard(
                    title = "Asistencias Totales",
                    value = totalStats["totalAssists"].toString()
                )
            }
            item {
                StatsSummaryCard(
                    title = "Rebotes Totales",
                    value = totalStats["totalRebounds"].toString()
                )
            }
            // Puedes añadir más tarjetas de resumen aquí para Robos, Tapones, Faltas etc.

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Últimas Fichas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            if (lastPerformanceSheets.isEmpty()) {
                item { // Usar item para que esté dentro del LazyColumn
                    Text(
                        text = "No hay fichas de rendimiento recientes.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = DarkText.copy(alpha = 0.7f)
                    )
                }
            } else {
                items(lastPerformanceSheets) { sheet -> // Usar items para iterar
                    LastPerformanceSheetCard(
                        performanceSheet = sheet,
                        eventViewModel = eventViewModel,
                        onLongClick = {
                            navController.navigate("performance_sheet_detail_screen/${sheet.sheetId}")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                // Botón "Ver Toda" para las últimas fichas si hay más de 2
                if (allPerformanceSheetsTotal.size > 2) {
                    item { // Usar item para que esté dentro del LazyColumn
                        Text(
                            text = "Ver Toda",
                            color = PrimaryOrange,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("performance_sheets_screen") }
                                .padding(top = 4.dp),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Evolución de Estadísticas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Dropdown para seleccionar la estadística del gráfico
            item {
                var expanded by remember { mutableStateOf(false) } // Estado para el dropdown

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedStatFilter,
                        onValueChange = { }, // No editable directamente
                        readOnly = true,
                        label = { Text("Seleccionar Estadística") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor() // Importante para el anclaje del menú
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        statOptions.forEach { stat ->
                            DropdownMenuItem(
                                text = { Text(stat) },
                                onClick = {
                                    selectedStatFilter = stat
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // --- INTEGRACIÓN DEL GRÁFICO ---
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp), // Altura fija para el gráfico
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    // Verifica si hay datos para mostrar el gráfico
                    if (chartDataPoints.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No hay datos para el gráfico de ${selectedStatFilter}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DarkText.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        // Convertir List<Pair<Float, Float>> a List<LineChartEntry>
                        val entries = chartDataPoints.map { (x, y) ->
                            LineChartEntry(x = x, y = y)
                        }

                        // Calcular el rango del eje Y para que sea dinámico
                        val minY = chartDataPoints.minOfOrNull { it.second } ?: 0f
                        val maxY = chartDataPoints.maxOfOrNull { it.second } ?: 10f
                        val yAxisRange = if (maxY == minY) (minY - 1f)..(maxY + 1f) else (minY * 0.9f)..(maxY * 1.1f)
                        val xAxisRange = 0f..(chartDataPoints.maxOfOrNull { it.first } ?: 1f) + 1f


                        // Crear los datos para el gráfico de línea
                        val lineChartData = LineChartData(
                            linePaths = listOf(
                                LineChartPath(
                                    entries = entries,
                                    pathEffect = LineChartPathEffect.Smooth, // Curva suave
                                    lineColor = PrimaryOrange,
                                    lineShader = LineShader.VerticalGradient(listOf(PrimaryOrange.copy(alpha = 0.5f), Color.Transparent)), // Sombreado bajo la línea
                                    points = LineChartPoints.Filled(color = PrimaryOrange) // Puntos rellenos
                                )
                            ),
                            // Dibujar ejes X e Y con etiquetas simples
                            xAxisDrawer = XAxisDrawer.Simple(),
                            yAxisDrawer = YAxisDrawer.Simple(),
                            // Ajustar límites de los ejes dinámicamente
                            xAxisValueRange = xAxisRange,
                            yAxisValueRange = yAxisRange
                        )

                        // Renderizar el gráfico de línea
                        LineChart(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp), // Padding dentro de la Card
                            lineChartData = lineChartData
                        )
                    }
                }
            }
            // --- FIN DE LA INTEGRACIÓN DEL GRÁFICO ---
        }
    }
}

@Composable
fun StatsSummaryCard(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = DarkText.copy(alpha = 0.8f)
            )
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryOrange,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LastPerformanceSheetCard(
    performanceSheet: PerformanceSheet,
    eventViewModel: EventViewModel,
    onLongClick: () -> Unit // Añadido para interactividad
) {
    // Obtenemos el evento completo para acceder a Event.eventName
    // Usar 'eventId' en lugar de 'id' si PerformanceSheet.eventId es el ID del evento
    val event by eventViewModel.getEventById(performanceSheet.eventId ?: -1L).collectAsState(initial = null) // Manejar eventId nulo

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLongClick() }, // Hacer la tarjeta clicable
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFECE4)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    // Formatear LocalDateTime desde el objeto Event
                    event?.let {
                        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy, HH:mm")
                        val formattedDateTime = it.dateTime.format(formatter)
                        Text(
                            text = formattedDateTime,
                            style = MaterialTheme.typography.labelMedium,
                            color = DarkText
                        )
                        Text(
                            text = "Evento: ${it.eventName}", // Usamos eventName del modelo Event
                            style = MaterialTheme.typography.labelSmall,
                            color = DarkText.copy(alpha = 0.7f)
                        )
                    } ?: Text(
                        text = if (performanceSheet.eventId == null) "Sin evento asociado" else "Cargando evento...",
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkText.copy(alpha = 0.7f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Puntos: ${performanceSheet.points}", style = MaterialTheme.typography.bodyMedium, color = DarkText)
            Text(text = "Rebotes: ${performanceSheet.defensiveRebounds + performanceSheet.offensiveRebounds}", style = MaterialTheme.typography.bodyMedium, color = DarkText)
            Text(text = "Asistencias: ${performanceSheet.assists}", style = MaterialTheme.typography.bodyMedium, color = DarkText)
        }
    }
}