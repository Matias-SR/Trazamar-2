package cl.aldemar.trazamar.data.repository

import android.content.Context
import cl.aldemar.trazamar.data.remote.ApiService
import cl.aldemar.trazamar.data.remote.RetrofitClient
import cl.aldemar.trazamar.data.remote.dto.LoginRequest
import cl.aldemar.trazamar.data.session.SessionManager

sealed class ResultadoApi<out T> {
    data class Exito<T>(val datos: T) : ResultadoApi<T>()
    data class Error(val mensaje: String) : ResultadoApi<Nothing>()
}

class AuthRepository(private val context: Context) {
    private val api: ApiService = RetrofitClient.getInstance(context)
    private val session = SessionManager(context)

    suspend fun login(username: String, password: String): ResultadoApi<String> {
        return try {
            val respuesta = api.login(LoginRequest(username, password))
            if (respuesta.isSuccessful && respuesta.body() != null) {
                val body = respuesta.body()!!
                session.guardarSesion(
                    token = body.token,
                    id = body.usuario.id,
                    username = body.usuario.username,
                    nombreCompleto = body.usuario.nombreCompleto,
                    rol = body.usuario.rol,
                )
                ResultadoApi.Exito(body.usuario.rol)
            } else {
                ResultadoApi.Error("Usuario o contraseña incorrectos.")
            }
        } catch (e: Exception) {
            ResultadoApi.Error("No se pudo conectar con el servidor. Verifica tu conexión: ${e.message}")
        }
    }

    suspend fun cerrarSesion() = session.cerrarSesion()

    val sesion get() = session.sesion
}
