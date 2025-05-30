package com.example.baskstatsapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize // <-- ¡Asegúrate de esta importación!
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.baskstatsapp.data.BaskStatsDatabase
import com.example.baskstatsapp.dao.EventDao
import com.example.baskstatsapp.dao.PerformanceSheetDao
import com.example.baskstatsapp.dao.PlayerDao
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme
import com.example.baskstatsapp.viewmodel.EventViewModel
import com.example.baskstatsapp.viewmodel.PerformanceSheetViewModel
import com.example.baskstatsapp.viewmodel.PlayerViewModel

class MainActivity : ComponentActivity() {

    companion object {
        var currentLoggedInPlayerId: Long? = null
    }

    private val database: BaskStatsDatabase by lazy {
        Room.databaseBuilder(
            application,
            BaskStatsDatabase::class.java,
            "baskstats_db"
        ).fallbackToDestructiveMigration().build()
    }

    private val eventDao: EventDao by lazy { database.eventDao() }
    private val performanceSheetDao: PerformanceSheetDao by lazy { database.performanceSheetDao() }
    private val playerDao: PlayerDao by lazy { database.playerDao() }

    @RequiresApi(Build.VERSION_CODES.O) // Mantén esta anotación aquí
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPrefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        currentLoggedInPlayerId = sharedPrefs.getLong("logged_in_player_id", -1L).takeIf { it != -1L }

        setContent {
            BaskStatsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), // <-- 'fillMaxSize' debería funcionar con la importación
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModelFactory(playerDao))
                    val eventViewModel: EventViewModel = viewModel(factory = EventViewModelFactory(eventDao))
                    val performanceSheetViewModel: PerformanceSheetViewModel = viewModel(factory = PerformanceSheetViewModelFactory(performanceSheetDao))

                    NavHost(
                        navController = navController,
                        startDestination = if (currentLoggedInPlayerId != null) "home_screen" else "login_screen"
                    ) {
                        composable("login_screen") {
                            LoginScreen(
                                navController = navController,
                                playerViewModel = playerViewModel,
                                onLoginSuccess = { playerId ->
                                    sharedPrefs.edit().putLong("logged_in_player_id", playerId).apply()
                                    currentLoggedInPlayerId = playerId
                                    navController.navigate("home_screen") {
                                        popUpTo("login_screen") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("registration_screen") {
                            RegistrationScreen(
                                navController = navController,
                                playerViewModel = playerViewModel,
                                onRegistrationSuccess = { playerId ->
                                    sharedPrefs.edit().putLong("logged_in_player_id", playerId).apply()
                                    currentLoggedInPlayerId = playerId
                                    navController.navigate("home_screen") {
                                        popUpTo("registration_screen") { inclusive = true }
                                        popUpTo("login_screen") { inclusive = true }
                                    }
                                }
                            )
                        }
                        // --- ¡QUITADA LA ANOTACIÓN @RequiresApi DE ESTOS BLOQUES 'composable'! ---
                        composable("home_screen") {
                            HomeScreen(
                                navController = navController,
                                eventViewModel = eventViewModel,
                                performanceSheetViewModel = performanceSheetViewModel,
                            )
                        }
                        composable("events_screen") {
                            EventsScreen(
                                navController = navController,
                                eventViewModel = eventViewModel
                            )
                        }
                        composable(
                            route = "event_detail_screen/{eventId}",
                            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val eventId = backStackEntry.arguments?.getLong("eventId") ?: -1L
                            EventDetailScreen(
                                navController = navController,
                                eventId = eventId,
                                eventViewModel = eventViewModel,
                                performanceSheetViewModel = performanceSheetViewModel
                            )
                        }
                        composable("performance_sheets_screen") {
                            PerformanceSheetsScreen(
                                navController = navController,
                                performanceSheetViewModel = performanceSheetViewModel
                            )
                        }
                        composable(
                            route = "performance_sheet_detail_screen/{sheetId}",
                            arguments = listOf(navArgument("sheetId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val sheetId = backStackEntry.arguments?.getLong("sheetId")
                            PerformanceSheetDetailScreen(
                                navController = navController,
                                sheetId = sheetId,
                                performanceSheetViewModel = performanceSheetViewModel
                            )
                        }

                        composable("add_event_screen") {
                            AddEventScreen(
                                navController = navController,
                                eventViewModel = eventViewModel,
                                performanceSheetViewModel = performanceSheetViewModel
                            )
                        }
                        composable(
                            route = "add_performance_sheet_screen/{eventId}",
                            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val eventId = backStackEntry.arguments?.getLong("eventId")
                            AddPerformanceSheetScreen(
                                navController = navController,
                                performanceSheetViewModel = performanceSheetViewModel,
                                eventViewModel = eventViewModel,
                                eventId = eventId
                            )
                        }
                        composable(
                            route = "edit_performance_sheet_screen/{sheetId}",
                            arguments = listOf(navArgument("sheetId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val sheetId = backStackEntry.arguments?.getLong("sheetId")
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

class EventViewModelFactory(private val eventDao: EventDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(eventDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class PerformanceSheetViewModelFactory(private val performanceSheetDao: PerformanceSheetDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerformanceSheetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerformanceSheetViewModel(performanceSheetDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class PlayerViewModelFactory(private val playerDao: PlayerDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayerViewModel(playerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}