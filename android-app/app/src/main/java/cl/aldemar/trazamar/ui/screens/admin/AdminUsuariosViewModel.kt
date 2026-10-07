package cl.aldemar.trazamar.ui.screens.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.aldemar.trazamar.data.remote.dto.UsuarioDto
import cl.aldemar.trazamar.data.repository.ResultadoApi
import cl.aldemar.trazamar.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminUsuariosUiState(
    val cargando: Boolean = true,
    val usuarios: List<UsuarioDto> = emptyList(),
    val error: String? = null,
    val mensaje: String? = null,
)

class AdminUsuariosViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UsuarioRepository(application)

    private val _uiState = MutableStateFlow(AdminUsuariosUiState())
    val uiState: StateFlow<AdminUsuariosUiState> = _uiState

    init { cargar() }

    fun cargar() {
        _uiState.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            when (val r = repo.listar()) {
                is ResultadoApi.Exito -> _uiState.update { it.copy(cargando = false, usuarios = r.datos) }
                is ResultadoApi.Error -> _uiState.update { it.copy(cargando = false, error = r.mensaje) }
            }
        }
    }

    fun crearUsuario(username: String, password: String, nombreCompleto: String, rol: String) {
        viewModelScope.launch {
            when (val r = repo.crear(username, password, nombreCompleto, rol)) {
                is ResultadoApi.Exito -> { _uiState.update { it.copy(mensaje = "Cuenta creada correctamente.") }; cargar() }
                is ResultadoApi.Error -> _uiState.update { it.copy(error = r.mensaje) }
            }
        }
    }

    fun cambiarEstado(usuario: UsuarioDto) {
        viewModelScope.launch {
            if (usuario.activo) repo.desactivar(usuario.id) else repo.actualizar(usuario.id, cl.aldemar.trazamar.data.remote.dto.ActualizarUsuarioRequest(activo = true))
            cargar()
        }
    }

    fun consumirMensaje() = _uiState.update { it.copy(mensaje = null, error = null) }
}
