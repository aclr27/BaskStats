package com.example.baskstatsapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen() {
    // Declaramos las variables para los campos de texto.
    var usernameOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //LOGO
        val image = painterResource(R.drawable.logo)
        Image(
            painter = image,
            contentDescription = "BaskStats Logo",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        //TITULO BASKSTATS
        Text(
            text = "BASKSTATS", // En la imagen el texto está en mayúsculas
            style = MaterialTheme.typography.headlineLarge.copy( // Usamos un estilo de título grande
                fontWeight = FontWeight.Bold, // Hacemos el texto negrita
                fontSize = 32.sp, // Ajustamos el tamaño de la fuente
                color = MaterialTheme.colorScheme.onBackground // Color oscuro para el texto del título
            ),
            modifier = Modifier.padding(bottom = 32.dp) // Espacio debajo del título
        )

        // Campo de Usuario o Correo
        OutlinedTextField(
            value = usernameOrEmail, // Usar la variable de estado
            onValueChange = { usernameOrEmail = it }, // Actualizar la variable de estado
            label = { Text("Usuario o Correo") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        // Campo de contraseña tapando los digitos.
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )
        Button(
            onClick = {
                // TODO: Lógica de autenticación real aquí
                val validUsername = "user@example.com"
                val validPassword = "1234"

                if(usernameOrEmail == validUsername && password == validPassword){
                    Toast.makeText(context,"Inicio exitoso", Toast.LENGTH_SHORT).show()
                    println("Navegando a la pantalla principal ...")

                    //navegar a la pantalla principal
                }else{
                    Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    println("Inicio de sesión fallido.")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Mayor altura para el botón
        ) {
            Text(text = "Iniciar Sesión", fontSize = 18.sp) // Texto más grande en el botón
        }

        Spacer(modifier = Modifier.height(16.dp)) // Espacio antes del "¿Olvidaste?"

        // Enlace "¿Olvidaste tu contraseña?"
        TextButton(onClick = { /* TODO: Navegar a pantalla de recuperación de contraseña */ }) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Spacer que empuja el botón "Registrarse" hacia abajo
        Spacer(modifier = Modifier.weight(1f))

        // Botón Registrarse (al final de la pantalla)
        OutlinedButton( // Usamos OutlinedButton para que tenga un borde
            onClick = { /* TODO: Navegar a pantalla de registro */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Mayor altura para el botón
        ) {
            Text(text = "Registrarse", fontSize = 18.sp) // Texto más grande en el botón
        }

    }
}