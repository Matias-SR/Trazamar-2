@file:OptIn(ExperimentalMaterial3Api::class)

package cl.aldemar.trazamar.ui.screens.nuevamuestra

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import cl.aldemar.trazamar.ui.components.TrazaMarTopBar
import cl.aldemar.trazamar.ui.theme.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.Image

@Composable
fun NuevaMuestraScreen(
    onFinalizado: () -> Unit,
    onVolverHome: () -> Unit,
    viewModel: NuevaMuestraViewModel = viewModel(),
) {
    val estado by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(estado.enviadaConExito) {
        if (estado.enviadaConExito) onFinalizado()
    }

    Scaffold(
        topBar = {
            TrazaMarTopBar(
                titulo = "Nueva muestra",
                subtitulo = tituloPaso(estado.paso),
                alVolver = { if (estado.paso == PasoRegistro.FORMULARIO) onVolverHome() else viewModel.volverPaso() },
            )
        },
        containerColor = Tide,
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            IndicadorPasos(estado.paso)

            if (estado.error != null) {
                Surface(color = ErrorSoft, modifier = Modifier.fillMaxWidth().padding(16.dp, 4.dp)) {
                    Text(estado.error!!, color = ErrorRed, modifier = Modifier.padding(10.dp))
                }
            }

            Box(Modifier.weight(1f)) {
                when (estado.paso) {
                    PasoRegistro.FORMULARIO -> PasoFormulario(estado, viewModel)
                    PasoRegistro.FOTOGRAFIA -> PasoFotografia(estado, viewModel, context)
                    PasoRegistro.CONTEO -> PasoConteo(estado, viewModel)
                    PasoRegistro.RESUMEN -> PasoResumen(estado, viewModel)
                }
            }
        }
    }
}

private fun tituloPaso(paso: PasoRegistro) = when (paso) {
    PasoRegistro.FORMULARIO -> "Paso 1 de 4 · Datos de trazabilidad"
    PasoRegistro.FOTOGRAFIA -> "Paso 2 de 4 · Evidencia fotográfica"
    PasoRegistro.CONTEO -> "Paso 3 de 4 · Conteo y mediciones"
    PasoRegistro.RESUMEN -> "Paso 4 de 4 · Resumen y envío"
}

@Composable
private fun IndicadorPasos(pasoActual: PasoRegistro) {
    val pasos = PasoRegistro.entries
    Row(Modifier.fillMaxWidth().padding(20.dp, 12.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        pasos.forEach { paso ->
            val activo = paso.ordinal <= pasoActual.ordinal
            Box(
                Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(if (activo) KelpSecondary else LineSubtle, RoundedCornerShape(2.dp))
            )
        }
    }
}

// ---------------------------------------------------------------------------
// PASO 1: Formulario de trazabilidad
// ---------------------------------------------------------------------------
@Composable
private fun PasoFormulario(estado: NuevaMuestraUiState, vm: NuevaMuestraViewModel) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        if (estado.cargandoCatalogos) {
            Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            return@Column
        }

        SelectorDesplegable(
            etiqueta = "Centro de cultivo",
            opciones = estado.centros.map { it.id to it.nombre },
            seleccionId = estado.centroId,
            onSeleccionar = vm::seleccionarCentro,
        )
        Spacer(Modifier.height(14.dp))
        SelectorDesplegable(
            etiqueta = "Tren",
            opciones = estado.centroSeleccionado?.trenes?.map { it.id to it.nombre } ?: emptyList(),
            seleccionId = estado.trenId,
            onSeleccionar = vm::seleccionarTren,
            habilitado = estado.centroId != null,
        )
        Spacer(Modifier.height(14.dp))
        SelectorDesplegable(
            etiqueta = "Línea",
            opciones = estado.trenSeleccionado?.lineas?.map { it.id to it.nombre } ?: emptyList(),
            seleccionId = estado.lineaId,
            onSeleccionar = vm::seleccionarLinea,
            habilitado = estado.trenId != null,
        )

        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(estado.fecha, vm::onFechaChange, label = { Text("Fecha (AAAA-MM-DD)") }, modifier = Modifier.weight(1f))
            OutlinedTextField(estado.hora, vm::onHoraChange, label = { Text("Hora") }, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            estado.tramoMetros, vm::onTramoChange,
            label = { Text("Tramo muestreado (metros)") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(28.dp))
        Button(
            onClick = { if (vm.validarFormularioYAvanzar()) vm.crearBorrador() },
            enabled = !estado.guardando,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BayPrimary),
        ) {
            if (estado.guardando) CircularProgressIndicator(Modifier.size(20.dp), color = androidx.compose.ui.graphics.Color.White, strokeWidth = 2.dp)
            else Text("Continuar a fotografía", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SelectorDesplegable(
    etiqueta: String,
    opciones: List<Pair<Int, String>>,
    seleccionId: Int?,
    onSeleccionar: (Int) -> Unit,
    habilitado: Boolean = true,
) {
    var expandido by remember { mutableStateOf(false) }
    val textoSeleccion = opciones.find { it.first == seleccionId }?.second ?: ""

    ExposedDropdownMenuBox(expanded = expandido && habilitado, onExpandedChange = { if (habilitado) expandido = it }) {
        OutlinedTextField(
            value = textoSeleccion,
            onValueChange = {},
            readOnly = true,
            enabled = habilitado,
            label = { Text(etiqueta) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
        )
        ExposedDropdownMenu(expanded = expandido && habilitado, onDismissRequest = { expandido = false }) {
            opciones.forEach { (id, nombre) ->
                DropdownMenuItem(text = { Text(nombre) }, onClick = { onSeleccionar(id); expandido = false })
            }
        }
    }
}

// ---------------------------------------------------------------------------
// PASO 2: Fotografía (cámara o galería)
// ---------------------------------------------------------------------------
@Composable
private fun PasoFotografia(estado: NuevaMuestraUiState, vm: NuevaMuestraViewModel, context: Context) {
    var uriTemporal by remember { mutableStateOf<Uri?>(null) }
    var archivoTemporal by remember { mutableStateOf<File?>(null) }

    val lanzadorCamara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { exito ->
        if (exito && archivoTemporal != null) vm.fotoCapturada(archivoTemporal!!.absolutePath)
    }
    val lanzadorGaleria = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val archivo = copiarUriAArchivo(context, uri)
            if (archivo != null) vm.fotoCapturada(archivo.absolutePath)
        }
    }

    Column(Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BaySoft,
            modifier = Modifier.fillMaxWidth().height(260.dp),
        ) {
            if (estado.fotoPath != null) {
                AsyncImage(
                    model = File(estado.fotoPath),
                    contentDescription = "Evidencia de la muestra",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = BayPrimary, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Aún no hay fotografía", color = InkMuted)
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = {
                    val archivo = crearArchivoTemporal(context)
                    archivoTemporal = archivo
                    uriTemporal = FileProvider.getUriForFile(context, "cl.aldemar.trazamar.fileprovider", archivo)
                    lanzadorCamara.launch(uriTemporal!!)
                },
                modifier = Modifier.weight(1f),
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Cámara")
            }
            OutlinedButton(onClick = { lanzadorGaleria.launch("image/*") }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Filled.Image, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Galería")
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = vm::avanzarAConteo,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BayPrimary),
        ) {
            Text("Continuar al conteo", fontWeight = FontWeight.Bold)
        }
    }
}

private fun crearArchivoTemporal(context: Context): File {
    val carpeta = File(context.cacheDir, "fotos").apply { mkdirs() }
    val nombre = "muestra_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date()) + ".jpg"
    return File(carpeta, nombre)
}

private fun copiarUriAArchivo(context: Context, uri: Uri): File? {
    return try {
        val destino = crearArchivoTemporal(context)
        context.contentResolver.openInputStream(uri)?.use { input ->
            destino.outputStream().use { output -> input.copyTo(output) }
        }
        destino
    } catch (e: Exception) {
        null
    }
}

// ---------------------------------------------------------------------------
// PASO 3: Conteo y mediciones
// ---------------------------------------------------------------------------
@Composable
private fun PasoConteo(estado: NuevaMuestraUiState, vm: NuevaMuestraViewModel) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        OutlinedTextField(
            estado.conteoIndividuos, vm::onConteoChange,
            label = { Text("Conteo de individuos *") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(16.dp))
        Text("Calibre (opcional, en mm)", style = MaterialTheme.typography.titleMedium, color = Ink)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(estado.calibreMin, vm::onCalibreMin, label = { Text("Mín") }, modifier = Modifier.weight(1f))
            OutlinedTextField(estado.calibrePromedio, vm::onCalibreProm, label = { Text("Prom") }, modifier = Modifier.weight(1f))
            OutlinedTextField(estado.calibreMax, vm::onCalibreMax, label = { Text("Máx") }, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            estado.pesoMuestra, vm::onPeso,
            label = { Text("Peso de la muestra (kg, opcional)") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            estado.observaciones, vm::onObservaciones,
            label = { Text("Observaciones") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = vm::avanzarAResumen,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BayPrimary),
        ) {
            Text("Ver resumen", fontWeight = FontWeight.Bold)
        }
    }
}

// ---------------------------------------------------------------------------
// PASO 4: Resumen y envío
// ---------------------------------------------------------------------------
@Composable
private fun PasoResumen(estado: NuevaMuestraUiState, vm: NuevaMuestraViewModel) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Surface(shape = RoundedCornerShape(16.dp), color = androidx.compose.ui.graphics.Color.White, tonalElevation = 1.dp) {
            Column(Modifier.padding(18.dp)) {
                FilaResumen("Centro", estado.centroSeleccionado?.nombre ?: "—")
                FilaResumen("Tren / Línea", "${estado.trenSeleccionado?.nombre ?: "—"} / ${estado.lineaSeleccionada?.nombre ?: "—"}")
                FilaResumen("Fecha / Hora", "${estado.fecha} ${estado.hora}")
                FilaResumen("Tramo", if (estado.tramoMetros.isBlank()) "—" else "${estado.tramoMetros} m")
                FilaResumen("Conteo", "${estado.conteoIndividuos} individuos")
                FilaResumen("Calibre (mín/prom/máx)", "${estado.calibreMin.ifBlank { "—" }} / ${estado.calibrePromedio.ifBlank { "—" }} / ${estado.calibreMax.ifBlank { "—" }}")
                FilaResumen("Peso muestra", if (estado.pesoMuestra.isBlank()) "—" else "${estado.pesoMuestra} kg")
                FilaResumen("Observaciones", estado.observaciones.ifBlank { "—" })
                FilaResumen("Fotografía", if (estado.fotoPath != null) "Adjunta ✓" else "Sin adjuntar")
            }
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = vm::confirmarYEnviar,
            enabled = !estado.guardando,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KelpSecondary),
        ) {
            if (estado.guardando) {
                CircularProgressIndicator(Modifier.size(20.dp), color = androidx.compose.ui.graphics.Color.White, strokeWidth = 2.dp)
            } else {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Enviar a revisión", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun FilaResumen(etiqueta: String, valor: String) {
    Column(Modifier.padding(vertical = 6.dp)) {
        Text(etiqueta, color = InkMuted, style = MaterialTheme.typography.labelMedium)
        Text(valor, color = Ink, style = MaterialTheme.typography.bodyLarge)
    }
    Divider(color = LineSubtle)
}
