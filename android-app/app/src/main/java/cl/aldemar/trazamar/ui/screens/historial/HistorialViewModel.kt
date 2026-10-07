package cl.aldemar.trazamar.ui.screens.historial

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.aldemar.trazamar.data.remote.dto.CentroDto
import cl.aldemar.trazamar.data.remote.dto.MuestraDto
import cl.aldemar.trazamar.data.repository.AuthRepository
import cl.aldemar.trazamar.data.repository.CatalogoRepository
import cl.aldemar.trazamar.data.repository.MuestraRepository
import cl.aldemar.trazamar.data.repository.ResultadoApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FiltrosHistorial(
    val centroId: Int? = null,
    val trenId: Int? = null,
    val lineaId: Int? = null,
    val estado: String? = null,
)

data class HistorialUiState(
    val rol: String? = null,
    val cargando: Boolean = true,
    val muestras: List<MuestraDto> = emptyList(),
    val centros: List<CentroDto> = emptyList(),
    val filtros: FiltrosHistorial = FiltrosHistorial(),
    val error: String? = null,
    val detalle: MuestraDto? = null,
)

class HistorialViewModel(application: Application) : AndroidViewModel(application) {
    private val muestraRepo = MuestraRepository(application)
    private val catalogoRepo = CatalogoRepository(application)
    private val authRepo = AuthRepository(application)

    private val _uiState = MutableStateFlow(HistorialUiState())
    val uiState: StateFlow<HistorialUiState> = _uiState

    private var operadorIdPropio: Int? = null

    init {
        viewModelScope.launch {
            val sesion = authRepo.sesion.first()
            operadorIdPropio = sesion.id
            _uiState.update { it.copy(rol = sesion.rol) }

            when (val c = catalogoRepo.obtenerCentros()) {
                is ResultadoApi.Exito -> _uiState.update { it.copy(centros = c.datos) }
                is ResultadoApi.Error -> { /* silencioso: los filtros de catálogo son opcionales */ }
            }
            cargar()
        }
    }

    fun actualizarFiltros(filtros: FiltrosHistorial) {
        _uiState.update { it.copy(filtros = filtros) }
        cargar()
    }

    fun cargar() {
        _uiState.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            val f = _uiState.value.filtros
            // El operador solo debe ver sus propias muestras en su historial personal.
            val operadorFiltro = if (_uiState.value.rol == "operador") operadorIdPropio else null

            when (val r = muestraRepo.sincronizarHistorial(
                centroId = f.centroId, trenId = f.trenId, lineaId = f.lineaId,
                estado = f.estado, operadorId = operadorFiltro,
            )) {
                is ResultadoApi.Exito -> _uiState.update { it.copy(cargando = false, muestras = r.datos) }
                is ResultadoApi.Error -> _uiState.update { it.copy(cargando = false, error = r.mensaje) }
            }
        }
    }

    fun verDetalle(muestra: MuestraDto) = _uiState.update { it.copy(detalle = muestra) }
    fun cerrarDetalle() = _uiState.update { it.copy(detalle = null) }

    fun revisar(id: Int, nuevoEstado: String, comentario: String?) {
        viewModelScope.launch {
            when (val r = muestraRepo.revisar(id, nuevoEstado, comentario)) {
                is ResultadoApi.Exito -> {
                    _uiState.update { it.copy(detalle = r.datos) }
                    cargar()
                }
                is ResultadoApi.Error -> _uiState.update { it.copy(error = r.mensaje) }
            }
        }
    }
}
