package com.example.baskstatsapp

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme // Asegúrate de importar tu tema

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val montserratFontFamily = FontFamily(
        Font(R.font.montserrat_variablefont_wght, FontWeight.Normal),
        Font(R.font.montserrat_italic_variablefont_wght, FontWeight.Normal),
    )

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF0F0F0)) { // Fondo gris claro
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // TITULO "BaskStats"
                Text(
                    text = "BaskStats",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = DarkText,
                        fontFamily = montserratFontFamily
                    ),
                    modifier = Modifier.padding(bottom = 64.dp)
                )

                // Campo de Correo Electrónico
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = PrimaryOrange,
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        cursorColor = PrimaryOrange,
                        focusedLabelColor = DarkText,
                        unfocusedLabelColor = DarkText
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Campo de Contraseña
                OutlinedTextField(
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
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = PrimaryOrange,
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        cursorColor = PrimaryOrange,
                        focusedLabelColor = DarkText,
                        unfocusedLabelColor = DarkText
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Campo de Confirmar Contraseña
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar contraseña") },
                    visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Text(
                            text = if (confirmPasswordVisibility) "Ocultar" else "Mostrar",
                            color = PrimaryOrange,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable { confirmPasswordVisibility = !confirmPasswordVisibility }
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = PrimaryOrange,
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        cursorColor = PrimaryOrange,
                        focusedLabelColor = DarkText,
                        unfocusedLabelColor = DarkText
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp) // Espacio antes del botón
                )

                // Botón "Registrarse"
                Button(
                    onClick = {
                        if (password == confirmPassword) {
                            Toast.makeText(context, "Registro exitoso para ${email}", Toast.LENGTH_SHORT).show()
                            navController.navigate("home_screen") {
                                popUpTo("registration_screen") { inclusive = true }
                                popUpTo("login_screen") { inclusive = true } // Limpia la pila para que no se pueda volver
                            }
                        } else {
                            Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
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
                    Text(text = "Registrarse", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Enlace "¿Ya tienes cuenta?"
                TextButton(onClick = { navController.popBackStack() /* Vuelve a la pantalla de Login */ }) {
                    Text(
                        text = "¿Ya tienes una cuenta? Inicia Sesión",
                        color = DarkText,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewRegistrationScreen() {
    BaskStatsAppTheme { // Asegúrate de que el preview use tu tema
        RegistrationScreen(rememberNavController())
    }
}