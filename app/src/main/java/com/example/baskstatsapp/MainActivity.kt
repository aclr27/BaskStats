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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                    /*
                    * Creamos el Nav controler (para controlar la navegación entre
                    * Las escenas.
                    */
                    val navController = rememberNavController()

                    //Definimos la pantalla de inicio de nuestra navegación.
                    NavHost(
                        navController = navController,
                        startDestination = "login_screen"
                    ){
                        //Definimos la ruta para la pantala de Login
                        composable("login_screen"){
                            //Pasamos el navController a LoginScreen para que pueda navegar
                            LoginScreen(navController = navController)
                        }
                        //Definimos la ruta para la pantallad de registro.
                        composable("registration_screen"){
                            RegistrationScreen(navController = navController)
                        }
                        //Definimos la ruta para la ruta para el Home
                        composable("home_screen"){
                            HomeScreen(navController = navController)
                        }
                        //Definimos la ruta para la escena de eventos.
                        composable("events_screen") {
                            EventsScreen(navController = navController)
                        }
                        //{eventId} es un argumento que se le pasa a la pantalla.
                        composable("event_detail_screen/{eventId}") { backStackEntry ->
                            val eventId = backStackEntry.arguments?.getString("eventId")
                            EventDetailScreen(navController = navController, eventId = eventId)
                        }
                        //Ruta para la escena de performance Sheets
                        composable("performance_sheets_screen") {
                            PerformanceSheetsScreen(navController = navController)
                        }
                        //Ruta para la escena de detalles de rendimiento.
                        composable("performance_sheet_detail_screen/{sheetId}") { backStackEntry ->
                            val sheetId = backStackEntry.arguments?.getString("sheetId")
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BaskStatsAppTheme {
        Greeting("Android")
    }
}