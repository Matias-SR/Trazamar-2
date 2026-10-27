package cl.aldemar.trazamar.ui.screens.nuevamuestra

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.aldemar.trazamar.data.remote.dto.*
import cl.aldemar.trazamar.data.repository.AuthRepository
import cl.aldemar.trazamar.data.repository.CatalogoRepository
import cl.aldemar.trazamar.data.repository.MuestraRepository
import cl.aldemar.trazamar.data.repository.ResultadoApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import cl.aldemar.trazamar.data.remote.NetworkTimeUtils
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class PasoRegistro { FORMULARIO, FOTOGRAFIA, CONTEO, RESUMEN }

private fun obtenerFechaHoraActual(): Pair<String, String> {
    val currentDateTime = LocalDateTime.now()
    val fecha = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val hora = currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    return fecha to hora
}

data class NuevaMuestraUiState(
    val paso: PasoRegistro = PasoRegistro.FORMULARIO,
    val cargandoCatalogos: Boolean = true,
    val cargandoFechaRed: Boolean = true,
    val centros: List<CentroDto> = emptyList(),

    val centroId: Int? = null,
    val trenId: Int? = null,
    val lineaId: Int? = null,
    val fecha: String = obtenerFechaHoraActual().first,
    val hora: String = obtenerFechaHoraActual().second,
    val tramoMetros: String = "",

    val fotoPath: String? = null,

    val conteoIndividuos: String = "",
    val calibrePromedio: String = "",
    val calibreMin: String = "",
    val calibreMax: String = "",
    val pesoMuestra: String = "",
    val observaciones: String = "",

    val muestraId: Int? = null,
    val guardando: Boolean = false,
    val error: String? = null,
    val enviadaConExito: Boolean = false,
) {
    val centroSeleccionado get() = centros.find { it.id == centroId }
    val trenSeleccionado get() = centroSeleccionado?.trenes?.find { it.id == trenId }
    val lineaSeleccionada get() = trenSeleccionado?.lineas?.find { it.id == lineaId }
}

class NuevaMuestraViewModel(application: Application) : AndroidViewModel(application) {
    private val catalogoRepo = CatalogoRepository(application)
    private val muestraRepo = MuestraRepository(application)
    private val authRepo = AuthRepository(application)

    private val _uiState = MutableStateFlow(NuevaMuestraUiState())
    val uiState: StateFlow<NuevaMuestraUiState> = _uiState

    private var operadorId: Int? = null

    init {
        sincronizarFechaHoraRed()
        cargarSesionYCatalogos()
    }

    fun sincronizarFechaHoraRed() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargandoFechaRed = true) }
            val (fechaRed, horaRed) = NetworkTimeUtils.obtenerFechaHoraRed()
            _uiState.update {
                it.copy(
                    fecha = fechaRed,
                    hora = horaRed,
                    cargandoFechaRed = false,
                )
            }
        }
    }

    private fun cargarSesionYCatalogos() {
        viewModelScope.launch {
            operadorId = authRepo.sesion.first().id

            when (val resultado = catalogoRepo.obtenerCentros()) {
                is ResultadoApi.Exito -> _uiState.update { it.copy(centros = resultado.datos, cargandoCatalogos = false) }
                is ResultadoApi.Error -> _uiState.update { it.copy(error = resultado.mensaje, cargandoCatalogos = false) }
            }
        }
    }

    fun seleccionarCentro(id: Int) = _uiState.update { it.copy(centroId = id, trenId = null, lineaId = null) }
    fun seleccionarTren(id: Int) = _uiState.update { it.copy(trenId = id, lineaId = null) }
    fun seleccionarLinea(id: Int) = _uiState.update { it.copy(lineaId = id) }
    fun onFechaChange(v: String) = _uiState.update { it.copy(fecha = v) }
    fun onHoraChange(v: String) = _uiState.update { it.copy(hora = v) }
    fun onTramoChange(v: String) = _uiState.update { it.copy(tramoMetros = v) }

    fun onConteoChange(v: String) = _uiState.update { it.copy(conteoIndividuos = v.filter { c -> c.isDigit() }) }
    fun onCalibreProm(v: String) = _uiState.update { it.copy(calibrePromedio = v) }
    fun onCalibreMin(v: String) = _uiState.update { it.copy(calibreMin = v) }
    fun onCalibreMax(v: String) = _uiState.update { it.copy(calibreMax = v) }
    fun onPeso(v: String) = _uiState.update { it.copy(pesoMuestra = v) }
    fun onObservaciones(v: String) = _uiState.update { it.copy(observaciones = v) }

    fun validarFormularioYAvanzar(): Boolean {
        val e = _uiState.value
        if (e.centroId == null || e.trenId == null || e.lineaId == null || e.fecha.isBlank()) {
            _uiState.update { it.copy(error = "Completa centro, tren, línea y fecha para continuar.") }
            return false
        }
        _uiState.update { it.copy(paso = PasoRegistro.FOTOGRAFIA, error = null) }
        return true
    }

    /** Crea el registro en borrador en el backend apenas se completa el formulario base. */
    fun crearBorrador() {
        val e = _uiState.value
        val opId = operadorId ?: return
        _uiState.update { it.copy(guardando = true, error = null) }
        viewModelScope.launch {
            val request = CrearMuestraRequest(
                centroId = e.centroId!!, trenId = e.trenId!!, lineaId = e.lineaId!!,
                fecha = e.fecha, hora = e.hora, tramoMetros = e.tramoMetros.toDoubleOrNull(),
                operadorId = opId,
            )
            when (val r = muestraRepo.crearMuestra(request)) {
                is ResultadoApi.Exito -> _uiState.update { it.copy(guardando = false, muestraId = r.datos.id, paso = PasoRegistro.FOTOGRAFIA) }
                is ResultadoApi.Error -> _uiState.update { it.copy(guardando = false, error = r.mensaje) }
            }
        }
    }

    fun fotoCapturada(path: String) {
        _uiState.update { it.copy(fotoPath = path) }
        val id = _uiState.value.muestraId ?: return
        viewModelScope.launch {
            when (val r = muestraRepo.subirFoto(id, File(path))) {
                is ResultadoApi.Exito -> _uiState.update { it.copy(error = null) }
                is ResultadoApi.Error -> _uiState.update { it.copy(error = r.mensaje) }
            }
        }
    }

    fun avanzarAConteo() = _uiState.update {
        if (it.fotoPath == null) it.copy(error = "Adjunta una fotografía de la muestra antes de continuar.")
        else it.copy(paso = PasoRegistro.CONTEO, error = null)
    }

    fun avanzarAResumen() {
        val e = _uiState.value
        if (e.conteoIndividuos.isBlank()) {
            _uiState.update { it.copy(error = "Ingresa el conteo de individuos.") }
            return
        }
        _uiState.update { it.copy(paso = PasoRegistro.RESUMEN, error = null) }
    }

    fun volverPaso() {
        val anterior = when (_uiState.value.paso) {
            PasoRegistro.FOTOGRAFIA -> PasoRegistro.FORMULARIO
            PasoRegistro.CONTEO -> PasoRegistro.FOTOGRAFIA
            PasoRegistro.RESUMEN -> PasoRegistro.CONTEO
            PasoRegistro.FORMULARIO -> PasoRegistro.FORMULARIO
        }
        _uiState.update { it.copy(paso = anterior, error = null) }
    }

    fun confirmarYEnviar() {
        val e = _uiState.value
        val id = e.muestraId ?: return
        _uiState.update { it.copy(guardando = true, error = null) }
        viewModelScope.launch {
            val actualizacion = ActualizarMuestraRequest(
                conteoIndividuos = e.conteoIndividuos.toIntOrNull(),
                observaciones = e.observaciones.ifBlank { null },
                calibrePromedio = e.calibrePromedio.toDoubleOrNull(),
                calibreMin = e.calibreMin.toDoubleOrNull(),
                calibreMax = e.calibreMax.toDoubleOrNull(),
                pesoMuestra = e.pesoMuestra.toDoubleOrNull(),
                tramoMetros = e.tramoMetros.toDoubleOrNull(),
            )
            when (val r1 = muestraRepo.actualizarMuestra(id, actualizacion)) {
                is ResultadoApi.Error -> { _uiState.update { it.copy(guardando = false, error = r1.mensaje) }; return@launch }
                is ResultadoApi.Exito -> { /* continúa */ }
            }
            when (val r2 = muestraRepo.enviarARevision(id)) {
                is ResultadoApi.Exito -> _uiState.update { it.copy(guardando = false, enviadaConExito = true) }
                is ResultadoApi.Error -> _uiState.update { it.copy(guardando = false, error = r2.mensaje) }
            }
        }
    }
}
