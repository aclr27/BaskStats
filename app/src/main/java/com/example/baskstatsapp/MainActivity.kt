package com.example.baskstatsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel // Importa esto
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.baskstatsapp.data.BaskStatsDatabase
import com.example.baskstatsapp.dao.EventDao
import com.example.baskstatsapp.dao.PerformanceSheetDao
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.viewmodel.EventViewModel // Importa el EventViewModel
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel // Importa el PerformanceSheetViewModel

class MainActivity : ComponentActivity() {

    // Inicializar la base de datos y los DAOs en un lazy (se inicializa la primera vez que se accede)
    // Usamos `application` para el context, que es más seguro que `this` de la Activity
    private val database: BaskStatsDatabase by lazy {
        Room.databaseBuilder(
            application, // Usa application context
            BaskStatsDatabase::class.java,
            "baskstats_db"
        )
            .fallbackToDestructiveMigration() // Importante para desarrollo: si cambias la base de datos, la reconstruye. En producción, harías migraciones.
            .build()
    }

    // Los DAOs se obtienen de la instancia de la base de datos
    private val eventDao: EventDao by lazy { database.eventDao() }
    private val performanceSheetDao: PerformanceSheetDao by lazy { database.performanceSheetDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BaskStatsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "login_screen"
                    ) {
                        composable("login_screen") {
                            LoginScreen(navController = navController)
                        }
                        composable("registration_screen") {
                            RegistrationScreen(navController = navController)
                        }
                        composable("home_screen") {
                            // Obtener instancias de los ViewModels
                            val eventViewModel: EventViewModel = viewModel(factory = EventViewModelFactory(eventDao))
                            val performanceSheetViewModel: PerformanceSheetViewModel = viewModel(factory = PerformanceSheetViewModelFactory(performanceSheetDao))
                            HomeScreen(
                                navController = navController,
                                eventViewModel = eventViewModel,
                                performanceSheetViewModel = performanceSheetViewModel
                            )
                        }
                        composable("events_screen") {
                            val eventViewModel: EventViewModel = viewModel(factory = EventViewModelFactory(eventDao))
                            EventsScreen(navController = navController, eventViewModel = eventViewModel)
                        }
                        composable(
                            route = "event_detail_screen/{eventId}",
                            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val eventId = backStackEntry.arguments?.getLong("eventId")
                            val eventViewModel: EventViewModel = viewModel(factory = EventViewModelFactory(eventDao))
                            // performanceSheetViewModel también podría ser útil aquí si se muestran stats del evento
                            val performanceSheetViewModel: PerformanceSheetViewModel = viewModel(factory = PerformanceSheetViewModelFactory(performanceSheetDao))
                            EventDetailScreen(
                                navController = navController,
                                eventId = eventId,
                                eventViewModel = eventViewModel,
                                performanceSheetViewModel = performanceSheetViewModel
                            )
                        }
                        composable("performance_sheets_screen") {
                            val performanceSheetViewModel: PerformanceSheetViewModel = viewModel(factory = PerformanceSheetViewModelFactory(performanceSheetDao))
                            PerformanceSheetsScreen(navController = navController, performanceSheetViewModel = performanceSheetViewModel)
                        }
                        composable(
                            route = "performance_sheet_detail_screen/{sheetId}",
                            arguments = listOf(navArgument("sheetId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val sheetId = backStackEntry.arguments?.getLong("sheetId")
                            val performanceSheetViewModel: PerformanceSheetViewModel = viewModel(factory = PerformanceSheetViewModelFactory(performanceSheetDao))
                            PerformanceSheetDetailScreen(
                                navController = navController,
                                sheetId = sheetId,
                                performanceSheetViewModel = performanceSheetViewModel
                            )
                        }

                        composable("add_event_screen") {
                            val eventViewModel: EventViewModel = viewModel(factory = EventViewModelFactory(eventDao))
                            val performanceSheetViewModel: PerformanceSheetViewModel = viewModel(factory = PerformanceSheetViewModelFactory(performanceSheetDao)) // <-- Asegúrate de que lo creas aquí
                        }
                        composable("add_performance_sheet_screen/{eventId}") { backStackEntry ->
                            val eventId = backStackEntry.arguments?.getLong("eventId")
                            val performanceSheetViewModel: PerformanceSheetViewModel = viewModel(factory = PerformanceSheetViewModelFactory(performanceSheetDao))
                            val eventViewModel: EventViewModel = viewModel(factory = EventViewModelFactory(eventDao))
                            AddPerformanceSheetScreen(
                                navController = navController,
                                eventId = eventId,
                                performanceSheetViewModel = performanceSheetViewModel,
                                eventViewModel = eventViewModel
                            )
                        }
                        composable(
                            route = "edit_performance_sheet_screen/{sheetId}",
                            arguments = listOf(navArgument("sheetId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val sheetId = backStackEntry.arguments?.getLong("sheetId")
                            val performanceSheetViewModel: PerformanceSheetViewModel = viewModel(factory = PerformanceSheetViewModelFactory(performanceSheetDao))
                            val eventViewModel: EventViewModel = viewModel(factory = EventViewModelFactory(eventDao))
                            EditPerformanceSheetScreen(
                                navController = navController,
                                sheetId = sheetId,
                                performanceSheetViewModel = performanceSheetViewModel,
                                eventViewModel = eventViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

// Factoría para crear EventViewModel con el DAO
class EventViewModelFactory(private val eventDao: EventDao) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(eventDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Factoría para crear PerformanceSheetViewModel con el DAO
class PerformanceSheetViewModelFactory(private val performanceSheetDao: PerformanceSheetDao) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerformanceSheetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerformanceSheetViewModel(performanceSheetDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BaskStatsAppTheme {
        Greeting("Android")
    }
}