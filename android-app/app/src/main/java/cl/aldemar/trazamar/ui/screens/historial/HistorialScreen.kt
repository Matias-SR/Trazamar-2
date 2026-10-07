package cl.aldemar.trazamar.ui.screens.historial

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import cl.aldemar.trazamar.data.remote.dto.MuestraDto
import cl.aldemar.trazamar.BuildConfig
import cl.aldemar.trazamar.ui.components.EstadoBadge
import cl.aldemar.trazamar.ui.components.TrazaMarTopBar
import cl.aldemar.trazamar.ui.theme.*

@Composable
fun HistorialScreen(
    onVolver: () -> Unit,
    viewModel: HistorialViewModel = viewModel(),
) {
    val estado by viewModel.uiState.collectAsState()
    var mostrarFiltros by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TrazaMarTopBar(
                titulo = if (estado.rol == "operador") "Mis muestras" else "Historial de muestras",
                alVolver = onVolver,
            )
        },
        containerColor = Tide,
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarFiltros = true }, containerColor = KelpSecondary) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtros", tint = Color.White)
            }
        },
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            when {
                estado.cargando -> Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) { CircularProgressIndicator() }
                estado.error != null && estado.muestras.isEmpty() -> Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text(estado.error!!, color = InkMuted, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
                estado.muestras.isEmpty() -> Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("No hay muestras registradas con estos filtros.", color = InkMuted)
                }
                else -> LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(estado.muestras) { muestra ->
                        TarjetaMuestra(muestra, onClick = { viewModel.verDetalle(muestra) })
                    }
                    item { Spacer(Modifier.height(60.dp)) }
                }
            }
        }
    }

    if (mostrarFiltros) {
        HojaFiltros(estado, onAplicar = { viewModel.actualizarFiltros(it); mostrarFiltros = false }, onCerrar = { mostrarFiltros = false })
    }

    estado.detalle?.let { muestra ->
        DialogoDetalle(
            muestra = muestra,
            esSupervisor = estado.rol == "supervisor" || estado.rol == "admin",
            onCerrar = viewModel::cerrarDetalle,
            onRevisar = { nuevoEstado, comentario -> viewModel.revisar(muestra.id, nuevoEstado, comentario) },
        )
    }
}

@Composable
private fun TarjetaMuestra(muestra: MuestraDto, onClick: () -> Unit) {
    Surface(shape = RoundedCornerShape(14.dp), color = Color.White, tonalElevation = 1.dp, onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(14.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("${muestra.fecha}${muestra.hora?.let { " · $it" } ?: ""}", fontWeight = FontWeight.Bold, color = Ink)
                Text("${muestra.centro?.nombre ?: ""} · ${muestra.tren?.nombre ?: ""} / ${muestra.linea?.nombre ?: ""}", color = InkMuted, style = MaterialTheme.typography.bodyMedium)
                Text("Operador: ${muestra.operador?.nombreCompleto ?: "—"} · Conteo: ${muestra.conteoIndividuos ?: "—"}", color = InkMuted, style = MaterialTheme.typography.bodyMedium)
            }
            EstadoBadge(muestra.estado)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HojaFiltros(estado: HistorialUiState, onAplicar: (FiltrosHistorial) -> Unit, onCerrar: () -> Unit) {
    var centroId by remember { mutableStateOf(estado.filtros.centroId) }
    var trenId by remember { mutableStateOf(estado.filtros.trenId) }
    var lineaId by remember { mutableStateOf(estado.filtros.lineaId) }
    var estadoSel by remember { mutableStateOf(estado.filtros.estado) }

    val centro = estado.centros.find { it.id == centroId }
    val tren = centro?.trenes?.find { it.id == trenId }

    ModalBottomSheet(onDismissRequest = onCerrar) {
        Column(Modifier.padding(20.dp)) {
            Text("Filtrar historial", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            ChipsSeleccionables("Centro", estado.centros.map { it.id to it.nombre }, centroId) { centroId = it; trenId = null; lineaId = null }
            Spacer(Modifier.height(12.dp))
            ChipsSeleccionables("Tren", centro?.trenes?.map { it.id to it.nombre } ?: emptyList(), trenId) { trenId = it; lineaId = null }
            Spacer(Modifier.height(12.dp))
            ChipsSeleccionables("Línea", tren?.lineas?.map { it.id to it.nombre } ?: emptyList(), lineaId) { lineaId = it }
            Spacer(Modifier.height(12.dp))
            ChipsEstado(estadoSel) { estadoSel = it }

            Spacer(Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = { onAplicar(FiltrosHistorial()) }, modifier = Modifier.weight(1f)) { Text("Limpiar") }
                Button(
                    onClick = { onAplicar(FiltrosHistorial(centroId, trenId, lineaId, estadoSel)) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = BayPrimary),
                ) { Text("Aplicar") }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ChipsSeleccionables(etiqueta: String, opciones: List<Pair<Int, String>>, seleccionId: Int?, onSeleccionar: (Int?) -> Unit) {
    Text(etiqueta, style = MaterialTheme.typography.labelLarge, color = InkMuted)
    Spacer(Modifier.height(6.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        opciones.forEach { (id, nombre) ->
            FilterChip(
                selected = seleccionId == id,
                onClick = { onSeleccionar(if (seleccionId == id) null else id) },
                label = { Text(nombre) },
            )
        }
    }
}

private val OPCIONES_ESTADO = listOf(
    "pendiente" to "Pendiente",
    "observada" to "Observada",
    "corregida" to "Corregida",
    "validada" to "Validada",
)

@Composable
private fun ChipsEstado(seleccion: String?, onSeleccionar: (String?) -> Unit) {
    Text("Estado", style = MaterialTheme.typography.labelLarge, color = InkMuted)
    Spacer(Modifier.height(6.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OPCIONES_ESTADO.forEach { (valor, etiqueta) ->
            FilterChip(
                selected = seleccion == valor,
                onClick = { onSeleccionar(if (seleccion == valor) null else valor) },
                label = { Text(etiqueta) },
            )
        }
    }
}

@Composable
private fun DialogoDetalle(
    muestra: MuestraDto,
    esSupervisor: Boolean,
    onCerrar: () -> Unit,
    onRevisar: (String, String?) -> Unit,
) {
    var comentario by remember { mutableStateOf(muestra.comentarioSupervisor ?: "") }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Muestra #${muestra.id}") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                if (!muestra.fotoUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = "${BuildConfig.BASE_URL.removeSuffix("api/").removeSuffix("/")}${muestra.fotoUrl}",
                        contentDescription = "Evidencia",
                        modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(10.dp)),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    )
                    Spacer(Modifier.height(12.dp))
                }
                FilaDetalle("Centro", muestra.centro?.nombre)
                FilaDetalle("Tren / Línea", "${muestra.tren?.nombre ?: ""} / ${muestra.linea?.nombre ?: ""}")
                FilaDetalle("Fecha", "${muestra.fecha} ${muestra.hora ?: ""}")
                FilaDetalle("Operador", muestra.operador?.nombreCompleto)
                FilaDetalle("Conteo", muestra.conteoIndividuos?.toString())
                FilaDetalle("Calibre (mín/prom/máx)", "${muestra.calibreMin ?: "—"} / ${muestra.calibrePromedio ?: "—"} / ${muestra.calibreMax ?: "—"}")
                FilaDetalle("Observaciones", muestra.observaciones)
                Spacer(Modifier.height(8.dp))
                EstadoBadge(muestra.estado)

                if (esSupervisor && muestra.estado in listOf("pendiente", "corregida")) {
                    Spacer(Modifier.height(14.dp))
                    OutlinedTextField(
                        value = comentario,
                        onValueChange = { comentario = it },
                        label = { Text("Comentario de revisión") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                    )
                }
            }
        },
        confirmButton = {
            if (esSupervisor && muestra.estado in listOf("pendiente", "corregida")) {
                Row {
                    TextButton(onClick = { onRevisar("observada", comentario) }) { Text("Observar", color = Amber) }
                    Button(onClick = { onRevisar("validada", comentario) }, colors = ButtonDefaults.buttonColors(containerColor = KelpSecondary)) { Text("Validar") }
                }
            } else {
                TextButton(onClick = onCerrar) { Text("Cerrar") }
            }
        },
        dismissButton = { if (esSupervisor && muestra.estado in listOf("pendiente", "corregida")) TextButton(onClick = onCerrar) { Text("Cancelar") } },
    )
}

@Composable
private fun FilaDetalle(etiqueta: String, valor: String?) {
    Column(Modifier.padding(vertical = 4.dp)) {
        Text(etiqueta, color = InkMuted, style = MaterialTheme.typography.labelMedium)
        Text(valor?.ifBlank { "—" } ?: "—", color = Ink)
    }
}
