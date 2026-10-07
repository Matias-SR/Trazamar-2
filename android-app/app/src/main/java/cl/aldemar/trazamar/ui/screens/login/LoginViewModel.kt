package cl.aldemar.trazamar.ui.screens.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.aldemar.trazamar.data.repository.AuthRepository
import cl.aldemar.trazamar.data.repository.ResultadoApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val cargando: Boolean = false,
    val error: String? = null,
    val rolIngresado: String? = null,
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = AuthRepository(application)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onUsernameChange(valor: String) = _uiState.update { it.copy(username = valor, error = null) }
    fun onPasswordChange(valor: String) = _uiState.update { it.copy(password = valor, error = null) }

    fun ingresar() {
        val estado = _uiState.value
        if (estado.username.isBlank() || estado.password.isBlank()) {
            _uiState.update { it.copy(error = "Ingresa tu usuario y contraseña.") }
            return
        }
        _uiState.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            when (val resultado = repo.login(estado.username.trim(), estado.password)) {
                is ResultadoApi.Exito -> _uiState.update { it.copy(cargando = false, rolIngresado = resultado.datos) }
                is ResultadoApi.Error -> _uiState.update { it.copy(cargando = false, error = resultado.mensaje) }
            }
        }
    }

    fun consumirNavegacion() = _uiState.update { it.copy(rolIngresado = null) }
}
