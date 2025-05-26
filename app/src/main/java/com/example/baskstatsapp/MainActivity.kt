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
import androidx.navigation.NavType // <-- Asegúrate de que esto está importado
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument // <-- Asegúrate de que esto está importado
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaskStatsAppTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "login_screen"
                    ){
                        composable("login_screen"){
                            LoginScreen(navController = navController)
                        }
                        composable("registration_screen"){
                            RegistrationScreen(navController = navController)
                        }
                        composable("home_screen"){
                            HomeScreen(navController = navController)
                        }
                        composable("events_screen") {
                            EventsScreen(navController = navController)
                        }
                        // *** ESTO ES CLAVE: Definir el argumento como LongType ***
                        composable(
                            route = "event_detail_screen/{eventId}",
                            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val eventId = backStackEntry.arguments?.getLong("eventId") // Obtener como Long
                            EventDetailScreen(navController = navController, eventId = eventId)
                        }
                        composable("performance_sheets_screen") {
                            PerformanceSheetsScreen(navController = navController)
                        }
                        // *** ESTO ES CLAVE: Definir el argumento como LongType ***
                        composable(
                            route = "performance_sheet_detail_screen/{sheetId}",
                            arguments = listOf(navArgument("sheetId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val sheetId = backStackEntry.arguments?.getLong("sheetId") // Obtener como Long
                            PerformanceSheetDetailScreen(navController = navController, sheetId = sheetId)
                        }

                        composable("add_event_screen") {
                            AddEventScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

