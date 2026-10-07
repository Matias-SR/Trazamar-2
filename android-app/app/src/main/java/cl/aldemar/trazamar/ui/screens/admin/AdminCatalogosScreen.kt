package cl.aldemar.trazamar.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.aldemar.trazamar.data.remote.dto.CentroDto
import cl.aldemar.trazamar.data.remote.dto.TrenDto
import cl.aldemar.trazamar.ui.components.TrazaMarTopBar
import cl.aldemar.trazamar.ui.theme.*

@Composable
fun AdminCatalogosScreen(onVolver: () -> Unit, viewModel: AdminCatalogosViewModel = viewModel()) {
    val estado by viewModel.uiState.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    var dialogoActivo by remember { mutableStateOf<DialogoCatalogo?>(null) }

    LaunchedEffect(estado.mensaje, estado.error) {
        (estado.mensaje ?: estado.error)?.let { snackbar.showSnackbar(it); viewModel.consumirMensaje() }
    }

    Scaffold(
        topBar = { TrazaMarTopBar("Centros, trenes y líneas", "Estructura de trazabilidad", onVolver) },
        containerColor = Tide,
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = {
            FloatingActionButton(onClick = { dialogoActivo = DialogoCatalogo.NuevoCentro }, containerColor = KelpSecondary) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo centro", tint = Color.White)
            }
        },
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            if (estado.cargando) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(estado.centros) { centro ->
                        TarjetaCentro(
                            centro = centro,
                            onAgregarTren = { dialogoActivo = DialogoCatalogo.NuevoTren(centro) },
                            onAgregarLinea = { tren -> dialogoActivo = DialogoCatalogo.NuevaLinea(tren) },
                        )
                    }
                    item { Spacer(Modifier.height(60.dp)) }
                }
            }
        }
    }

    when (val d = dialogoActivo) {
        is DialogoCatalogo.NuevoCentro -> DialogoTexto("Nuevo centro", listOf("Nombre", "Código"), onCerrar = { dialogoActivo = null }) { valores ->
            viewModel.crearCentro(valores[0], valores[1]); dialogoActivo = null
        }
        is DialogoCatalogo.NuevoTren -> DialogoTexto("Nuevo tren en ${d.centro.nombre}", listOf("Nombre del tren"), onCerrar = { dialogoActivo = null }) { valores ->
            viewModel.crearTren(valores[0], d.centro.id); dialogoActivo = null
        }
        is DialogoCatalogo.NuevaLinea -> DialogoTexto("Nueva línea en ${d.tren.nombre}", listOf("Nombre de la línea"), onCerrar = { dialogoActivo = null }) { valores ->
            viewModel.crearLinea(valores[0], d.tren.id); dialogoActivo = null
        }
        null -> {}
    }
}

private sealed class DialogoCatalogo {
    object NuevoCentro : DialogoCatalogo()
    data class NuevoTren(val centro: CentroDto) : DialogoCatalogo()
    data class NuevaLinea(val tren: TrenDto) : DialogoCatalogo()
}

@Composable
private fun TarjetaCentro(centro: CentroDto, onAgregarTren: () -> Unit, onAgregarLinea: (TrenDto) -> Unit) {
    var expandido by remember { mutableStateOf(false) }
    Surface(shape = RoundedCornerShape(14.dp), color = Color.White, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    Text(centro.nombre, fontWeight = FontWeight.Bold, color = Ink)
                    Text("Código: ${centro.codigo} · ${centro.trenes?.size ?: 0} trenes", color = InkMuted, style = MaterialTheme.typography.bodyMedium)
                }
                IconButton(onClick = { expandido = !expandido }) {
                    Icon(if (expandido) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null)
                }
            }

            if (expandido) {
                Spacer(Modifier.height(8.dp))
                centro.trenes?.forEach { tren ->
                    Surface(color = Tide, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(tren.nombre, fontWeight = FontWeight.SemiBold, color = Ink, modifier = Modifier.weight(1f))
                                TextButton(onClick = { onAgregarLinea(tren) }) { Text("+ Línea") }
                            }
                            tren.lineas?.forEach { linea ->
                                Text("• ${linea.nombre}", color = InkMuted, modifier = Modifier.padding(start = 8.dp, top = 2.dp))
                            }
                        }
                    }
                }
                Spacer(Modifier.height(6.dp))
                TextButton(onClick = onAgregarTren) { Text("+ Agregar tren") }
            }
        }
    }
}

@Composable
private fun DialogoTexto(titulo: String, etiquetas: List<String>, onCerrar: () -> Unit, onConfirmar: (List<String>) -> Unit) {
    val valores = remember { mutableStateListOf(*Array(etiquetas.size) { "" }) }
    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text(titulo) },
        text = {
            Column {
                etiquetas.forEachIndexed { i, etiqueta ->
                    OutlinedTextField(
                        value = valores[i],
                        onValueChange = { valores[i] = it },
                        label = { Text(etiqueta) },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirmar(valores.toList()) },
                enabled = valores.all { it.isNotBlank() },
                colors = ButtonDefaults.buttonColors(containerColor = BayPrimary),
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onCerrar) { Text("Cancelar") } },
    )
}
