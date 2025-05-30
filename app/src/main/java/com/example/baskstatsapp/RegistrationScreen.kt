package com.example.baskstatsapp

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.rememberCoroutineScope // <-- Importar para coroutines
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.baskstatsapp.ui.theme.DarkText
import com.example.baskstatsapp.ui.theme.PrimaryOrange
import com.example.baskstatsapp.ui.theme.BaskStatsAppTheme

import com.example.baskstatsapp.viewmodel.PlayerViewModel
import com.example.baskstatsapp.model.Player
import kotlinx.coroutines.launch // <-- Importar para scope.launch
import org.mindrot.jbcrypt.BCrypt // <-- Importar BCrypt para hashing de contraseñas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navController: NavController,
    playerViewModel: PlayerViewModel,
    onRegistrationSuccess: (Long) -> Unit // Callback para cuando el registro sea exitoso
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var playerName by remember { mutableStateOf("") }
    var playerNumber by remember { mutableStateOf("") }
    var playerPosition by remember { mutableStateOf("") }

    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope() // <-- Scope para lanzar coroutines

    val montserratFontFamily = FontFamily(
        Font(R.font.montserrat_variablefont_wght, FontWeight.Normal),
        Font(R.font.montserrat_italic_variablefont_wght, FontWeight.Normal),
    )

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF0F0F0)) {
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
                Text(
                    text = "BaskStats",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = DarkText,
                        fontFamily = montserratFontFamily
                    ),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Campo de Nombre del Jugador
                OutlinedTextField(
                    value = playerName,
                    onValueChange = { playerName = it },
                    label = { Text("Nombre del jugador") },
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

                // Campo de Correo Electrónico
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // Teclado de email
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

                // Campo de Número de Camiseta (Opcional)
                OutlinedTextField(
                    value = playerNumber,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            playerNumber = newValue
                        }
                    },
                    label = { Text("Número de camiseta (Opcional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

                // Campo de Posición (Opcional)
                OutlinedTextField(
                    value = playerPosition,
                    onValueChange = { playerPosition = it },
                    label = { Text("Posición (Opcional, ej. Base)") },
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
                                .clickable {
                                    confirmPasswordVisibility = !confirmPasswordVisibility
                                }
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
                        .padding(bottom = 24.dp)
                )

                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank() || playerName.isBlank()) {
                            Toast.makeText(
                                context,
                                "Correo, nombre y contraseña son obligatorios",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        if (password != confirmPassword) {
                            Toast.makeText(
                                context,
                                "Las contraseñas no coinciden",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        if (password.length < 6) { // Requiere mínimo 6 caracteres
                            Toast.makeText(
                                context,
                                "La contraseña debe tener al menos 6 caracteres",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        scope.launch { // Usar rememberCoroutineScope para lanzar la coroutine
                            try {
                                // Hashear la contraseña antes de guardarla
                                val passwordHashed = BCrypt.hashpw(password, BCrypt.gensalt())
                                // Para DEVELOPMENT_ONLY sin jbcrypt: val passwordHashed = password

                                val newPlayer = Player(
                                    name = playerName,
                                    email = email,
                                    passwordHash = passwordHashed, // Usar el hash
                                    number = playerNumber.toIntOrNull(),
                                    position = playerPosition.ifBlank { null },
                                    teamId = null,
                                    photoUrl = null
                                )
                                // Intentar insertar el jugador
                                val playerId = playerViewModel.registerPlayer(newPlayer)
                                if (playerId > 0) { // Si el ID es mayor que 0, la inserción fue exitosa
                                    Toast.makeText(
                                        context,
                                        "Registro exitoso para ${email}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onRegistrationSuccess(playerId) // Llamar al callback de éxito
                                } else {
                                    // Esto podría ocurrir si el email ya existe y PlayerDao usa OnConflictStrategy.ABORT
                                    Toast.makeText(
                                        context,
                                        "Fallo en el registro. El correo electrónico ya está en uso.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } catch (e: Exception) {
                                // Capturar cualquier otra excepción durante la inserción
                                Toast.makeText(
                                    context,
                                    "Error al registrar: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                                e.printStackTrace() // Imprime el stack trace para depuración
                            }
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

                TextButton(onClick = { navController.popBackStack() }) {
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