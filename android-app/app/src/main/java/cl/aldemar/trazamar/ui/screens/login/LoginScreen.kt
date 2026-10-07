package cl.aldemar.trazamar.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.aldemar.trazamar.ui.theme.*
import cl.aldemar.trazamar.R

@Composable
fun LoginScreen(
    onLoginExitoso: (rol: String) -> Unit,
    viewModel: LoginViewModel = viewModel(),
) {
    val estado by viewModel.uiState.collectAsState()
    var mostrarPassword by remember { mutableStateOf(false) }

    LaunchedEffect(estado.rolIngresado) {
        estado.rolIngresado?.let {
            onLoginExitoso(it)
            viewModel.consumirNavegacion()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BayDeep, BayPrimary)))
            .padding(horizontal = 28.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de TrazaMar"
            )
        }

            Spacer(Modifier.height(4.dp))
            Text(
                "Registro y trazabilidad de muestreo · ALDEMAR SpA",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 13.sp,
            )

            Spacer(Modifier.height(36.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 8.dp,
            ) {
                Column(Modifier.padding(24.dp)) {
                    Text("Iniciar sesión", style = MaterialTheme.typography.titleLarge, color = Ink)
                    Text(
                        "Operador, Supervisor, Analista y Administrador",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkMuted,
                    )

                    Spacer(Modifier.height(20.dp))

                    OutlinedTextField(
                        value = estado.username,
                        onValueChange = viewModel::onUsernameChange,
                        label = { Text("Usuario") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = estado.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = if (mostrarPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { mostrarPassword = !mostrarPassword }) {
                                Icon(
                                    if (mostrarPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    if (estado.error != null) {
                        Spacer(Modifier.height(10.dp))
                        Surface(color = ErrorSoft, shape = RoundedCornerShape(10.dp)) {
                            Text(
                                estado.error!!,
                                color = ErrorRed,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(10.dp),
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = viewModel::ingresar,
                        enabled = !estado.cargando,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BayPrimary),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        if (estado.cargando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Ingresar", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

    }
}