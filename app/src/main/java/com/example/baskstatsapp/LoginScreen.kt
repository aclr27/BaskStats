package com.example.baskstatsapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.PrimaryOrange

import androidx.compose.material3.Icon // Importación correcta del Icon de Material3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var usernameOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val montserratFontFamily = FontFamily(
        Font(R.font.montserrat_variablefont_wght, FontWeight.Normal),
        Font(R.font.montserrat_italic_variablefont_wght, FontWeight.Normal),
    )

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8F8F8)) {
        // Usamos un Box para contener todo y alinear verticalmente el contenido principal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center // Esto centrará todo el contenido del Box
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(), // El Column interno puede ocupar todo el ancho
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top // Usamos Top aquí porque el Box ya centrará el Column
            ) {
                // TITULO CON ICONO (Baloncesto)
                Row(
                    modifier = Modifier.padding(bottom = 32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.SportsBasketball,
                        contentDescription = "Balón de baloncesto",
                        tint = PrimaryOrange,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "BaskStats",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                            color = DarkText,
                            fontFamily = montserratFontFamily
                        )
                    )
                }

                // Campo de Usuario o Correo
                TextField(
                    value = usernameOrEmail,
                    onValueChange = { usernameOrEmail = it },
                    label = { Text("Nombre de usuario o correo electrónico") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = PrimaryOrange,
                        focusedLabelColor = DarkText,
                        unfocusedLabelColor = DarkText
                    ),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Campo de contraseña con "Mostrar"
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Text(
                            text = if (passwordVisibility) "Ocultar" else "Mostrar",
                            color = PrimaryOrange,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable { passwordVisibility = !passwordVisibility }
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = PrimaryOrange,
                        focusedLabelColor = DarkText,
                        unfocusedLabelColor = DarkText
                    ),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )

                Button(
                    onClick = {
                        val validUsername = "user@example.com"
                        val validPassword = "123"

                        if (usernameOrEmail == validUsername && password == validPassword) {
                            Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                            navController.navigate("home_screen") {
                                popUpTo("loginScreen") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryOrange,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(text = "Iniciar Sesión", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = { /* TODO: Navegar a pantalla de recuperación de contraseña */ }) {
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        color = DarkText,
                        fontSize = 14.sp
                    )
                }

                // Spacer para empujar el botón "Registrarse" al final, fuera del Box principal centrado.
                // Este spacer debe estar fuera del Column centrado verticalmente si lo queremos abajo del todo.
            }

            // **BOTÓN "REGISTRARSE"**
            // Lo colocamos directamente dentro del Box para que podamos usar Alignment.BottomCenter
            // y que no sea afectado por el centrado vertical del Column de arriba.
            Button(
                onClick = { /* TODO: Navegar a pantalla de registro */ },
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Alinea este botón específicamente abajo y al centro
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(bottom = 32.dp), // Espacio al final de la pantalla
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = PrimaryOrange
                ),
                shape = RoundedCornerShape(28.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "Registrarse",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(rememberNavController())
}