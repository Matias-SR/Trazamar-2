package cl.aldemar.trazamar.data.remote.dto

data class LoginRequest(val username: String, val password: String)

data class LoginResponse(val token: String, val usuario: UsuarioDto)

data class UsuarioDto(
    val id: Int,
    val username: String,
    val nombreCompleto: String,
    val rol: String,
    val activo: Boolean = true,
    val createdAt: String? = null,
)

data class CrearUsuarioRequest(
    val username: String,
    val password: String,
    val nombreCompleto: String,
    val rol: String,
)

data class ActualizarUsuarioRequest(
    val nombreCompleto: String? = null,
    val rol: String? = null,
    val password: String? = null,
    val activo: Boolean? = null,
)
