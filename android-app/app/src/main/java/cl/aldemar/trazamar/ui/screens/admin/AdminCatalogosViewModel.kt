package cl.aldemar.trazamar.ui.screens.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.aldemar.trazamar.data.remote.dto.CentroDto
import cl.aldemar.trazamar.data.repository.CatalogoRepository
import cl.aldemar.trazamar.data.repository.ResultadoApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminCatalogosUiState(
    val cargando: Boolean = true,
    val centros: List<CentroDto> = emptyList(),
    val error: String? = null,
    val mensaje: String? = null,
)

class AdminCatalogosViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = CatalogoRepository(application)

    private val _uiState = MutableStateFlow(AdminCatalogosUiState())
    val uiState: StateFlow<AdminCatalogosUiState> = _uiState

    init { cargar() }

    fun cargar() {
        _uiState.update { it.copy(cargando = true) }
        viewModelScope.launch {
            when (val r = repo.obtenerCentros()) {
                is ResultadoApi.Exito -> _uiState.update { it.copy(cargando = false, centros = r.datos) }
                is ResultadoApi.Error -> _uiState.update { it.copy(cargando = false, error = r.mensaje) }
            }
        }
    }

    fun crearCentro(nombre: String, codigo: String) {
        viewModelScope.launch {
            val r = repo.crearCentro(nombre, codigo)
            if (r.isSuccessful) { _uiState.update { it.copy(mensaje = "Centro creado.") }; cargar() }
            else _uiState.update { it.copy(error = "No se pudo crear el centro.") }
        }
    }

    fun crearTren(nombre: String, centroId: Int) {
        viewModelScope.launch {
            val r = repo.crearTren(nombre, centroId)
            if (r.isSuccessful) { _uiState.update { it.copy(mensaje = "Tren creado.") }; cargar() }
            else _uiState.update { it.copy(error = "No se pudo crear el tren.") }
        }
    }

    fun crearLinea(nombre: String, trenId: Int) {
        viewModelScope.launch {
            val r = repo.crearLinea(nombre, trenId)
            if (r.isSuccessful) { _uiState.update { it.copy(mensaje = "Línea creada.") }; cargar() }
            else _uiState.update { it.copy(error = "No se pudo crear la línea.") }
        }
    }

    fun consumirMensaje() = _uiState.update { it.copy(mensaje = null, error = null) }
}
