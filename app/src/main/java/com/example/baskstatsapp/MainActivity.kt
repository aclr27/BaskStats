// app/src/main/java/com/example/baskstatsapp/MainActivity.kt
package com.example.baskstatsapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPrefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        currentLoggedInPlayerId = sharedPrefs.getLong("logged_in_player_id", -1L).takeIf { it != -1L }

        setContent {
            BaskStatsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
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
                        composable("home_screen") {
                            HomeScreen(
                                navController = navController,
                                eventViewModel = eventViewModel,
                                performanceSheetViewModel = performanceSheetViewModel,
                                onLogout = {
                                    sharedPrefs.edit().remove("logged_in_player_id").apply()
                                    currentLoggedInPlayerId = null
                                    navController.navigate("login_screen") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
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
                            arguments = listOf(navArgument("eventId") { type = NavType.LongType; defaultValue = -1L })
                        ) { backStackEntry ->
                            val eventId = backStackEntry.arguments?.getLong("eventId") ?: -1L
                            EventDetailScreen(
                                navController = navController,
                                eventId = eventId,
                                eventViewModel = eventViewModel,
                                performanceSheetViewModel = performanceSheetViewModel
                            )
                        }
                        composable(
                            route = "edit_event_screen/{eventId}",
                            arguments = listOf(navArgument("eventId") { type = NavType.LongType; defaultValue = -1L })
                        ) { backStackEntry ->
                            val eventId = backStackEntry.arguments?.getLong("eventId") ?: -1L
                            EditEventScreen(
                                navController = navController,
                                eventId = eventId,
                                eventViewModel = eventViewModel
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
                            arguments = listOf(navArgument("sheetId") { type = NavType.LongType; defaultValue = -1L })
                        ) { backStackEntry ->
                            val sheetId = backStackEntry.arguments?.getLong("sheetId") ?: -1L
                            PerformanceSheetDetailScreen(
                                navController = navController,
                                sheetId = sheetId,
                                performanceSheetViewModel = performanceSheetViewModel,
                                eventViewModel = eventViewModel,
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
                            route = "add_performance_sheet_screen?eventId={eventId}",
                            arguments = listOf(navArgument("eventId") {
                                type = NavType.LongType
                                defaultValue = -1L
                                // ELIMINADO: nullable = true // Esto causó el error para LongType
                            })
                        ) { backStackEntry ->
                            // El `takeIf` maneja la opcionalidad correctamente aquí.
                            val eventId = backStackEntry.arguments?.getLong("eventId")?.takeIf { it != -1L }
                            AddPerformanceSheetScreen(
                                navController = navController,
                                performanceSheetViewModel = performanceSheetViewModel,
                                eventViewModel = eventViewModel,
                                playerViewModel = playerViewModel,
                                associatedEventId = eventId
                            )
                        }
                        composable(
                            route = "edit_performance_sheet_screen/{sheetId}",
                            arguments = listOf(navArgument("sheetId") { type = NavType.LongType; defaultValue = -1L })
                        ) { backStackEntry ->
                            val sheetId = backStackEntry.arguments?.getLong("sheetId") ?: -1L
                            EditPerformanceSheetScreen(
                                navController = navController,
                                sheetId = sheetId,
                                performanceSheetViewModel = performanceSheetViewModel,
                                eventViewModel = eventViewModel,
                                playerViewModel = playerViewModel
                            )
                        }
                        composable(
                            route = "player_stats_screen/{playerId}",
                            arguments = listOf(navArgument("playerId") { type = NavType.LongType; defaultValue = -1L })
                        ) { backStackEntry ->
                            val playerId = backStackEntry.arguments?.getLong("playerId") ?: -1L
                            PlayerStatsScreen(
                                navController = navController,
                                playerId = playerId,
                                performanceSheetViewModel = performanceSheetViewModel,
                                playerViewModel = playerViewModel
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