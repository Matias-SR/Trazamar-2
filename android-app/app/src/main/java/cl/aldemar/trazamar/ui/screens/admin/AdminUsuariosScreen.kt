@file:OptIn(ExperimentalMaterial3Api::class)

package cl.aldemar.trazamar.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.aldemar.trazamar.data.remote.dto.UsuarioDto
import cl.aldemar.trazamar.ui.components.TrazaMarTopBar
import cl.aldemar.trazamar.ui.theme.*

private val ROLES = listOf("operador" to "Operador de muestreo", "supervisor" to "Supervisor técnico", "analista" to "Analista de reportes", "admin" to "Administrador")

@Composable
fun AdminUsuariosScreen(onVolver: () -> Unit, viewModel: AdminUsuariosViewModel = viewModel()) {
    val estado by viewModel.uiState.collectAsState()
    var mostrarFormulario by remember { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(estado.mensaje, estado.error) {
        (estado.mensaje ?: estado.error)?.let { snackbar.showSnackbar(it); viewModel.consumirMensaje() }
    }

    Scaffold(
        topBar = { TrazaMarTopBar("Cuentas de usuario", "Creación y roles del sistema", onVolver) },
        containerColor = Tide,
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarFormulario = true }, containerColor = KelpSecondary) {
                Icon(Icons.Default.Add, contentDescription = "Nueva cuenta", tint = Color.White)
            }
        },
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when {
                estado.cargando -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                else -> LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(estado.usuarios) { usuario -> TarjetaUsuario(usuario, onCambiarEstado = { viewModel.cambiarEstado(usuario) }) }
                    item { Spacer(Modifier.height(60.dp)) }
                }
            }
        }
    }

    if (mostrarFormulario) {
        DialogoNuevaCuenta(
            onCerrar = { mostrarFormulario = false },
            onCrear = { u, p, n, r -> viewModel.crearUsuario(u, p, n, r); mostrarFormulario = false },
        )
    }
}

@Composable
private fun TarjetaUsuario(usuario: UsuarioDto, onCambiarEstado: () -> Unit) {
    Surface(shape = RoundedCornerShape(14.dp), color = Color.White, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(usuario.nombreCompleto, fontWeight = FontWeight.Bold, color = Ink)
                Text("@${usuario.username} · ${ROLES.find { it.first == usuario.rol }?.second ?: usuario.rol}", color = InkMuted, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onCambiarEstado) {
                if (usuario.activo) Icon(Icons.Default.CheckCircle, contentDescription = "Activo", tint = KelpSecondary)
                else Icon(Icons.Default.Block, contentDescription = "Inactivo", tint = ErrorRed)
            }
        }
    }
}

@Composable
private fun DialogoNuevaCuenta(onCerrar: () -> Unit, onCrear: (String, String, String, String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf(ROLES.first().first) }
    var expandido by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Nueva cuenta") },
        text = {
            Column {
                OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre completo") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(username, { username = it }, label = { Text("Usuario") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(password, { password = it }, label = { Text("Contraseña temporal") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(10.dp))

                ExposedDropdownMenuBox(expanded = expandido, onExpandedChange = { expandido = it }) {
                    OutlinedTextField(
                        value = ROLES.find { it.first == rol }?.second ?: "",
                        onValueChange = {}, readOnly = true, label = { Text("Rol") },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                    )
                    ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                        ROLES.forEach { (valor, etiqueta) ->
                            DropdownMenuItem(text = { Text(etiqueta) }, onClick = { rol = valor; expandido = false })
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onCrear(username.trim(), password, nombre.trim(), rol) },
                enabled = username.isNotBlank() && password.length >= 6 && nombre.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = BayPrimary),
            ) { Text("Crear cuenta") }
        },
        dismissButton = { TextButton(onClick = onCerrar) { Text("Cancelar") } },
    )
}
