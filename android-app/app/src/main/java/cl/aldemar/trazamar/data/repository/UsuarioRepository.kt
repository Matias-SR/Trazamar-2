package cl.aldemar.trazamar.data.repository

import android.content.Context
import cl.aldemar.trazamar.data.remote.ApiService
import cl.aldemar.trazamar.data.remote.RetrofitClient
import cl.aldemar.trazamar.data.remote.dto.ActualizarUsuarioRequest
import cl.aldemar.trazamar.data.remote.dto.CrearUsuarioRequest
import cl.aldemar.trazamar.data.remote.dto.UsuarioDto

class UsuarioRepository(private val context: Context) {
    private val api: ApiService = RetrofitClient.getInstance(context)

    suspend fun listar(): ResultadoApi<List<UsuarioDto>> = try {
        val r = api.listarUsuarios()
        if (r.isSuccessful && r.body() != null) ResultadoApi.Exito(r.body()!!)
        else ResultadoApi.Error("No se pudo obtener la lista de usuarios.")
    } catch (e: Exception) {
        ResultadoApi.Error("Error de conexión: ${e.message}")
    }

    suspend fun crear(username: String, password: String, nombreCompleto: String, rol: String): ResultadoApi<UsuarioDto> = try {
        val r = api.crearUsuario(CrearUsuarioRequest(username, password, nombreCompleto, rol))
        if (r.isSuccessful && r.body() != null) ResultadoApi.Exito(r.body()!!)
        else ResultadoApi.Error(r.errorBody()?.string() ?: "No se pudo crear el usuario.")
    } catch (e: Exception) {
        ResultadoApi.Error("Error de conexión: ${e.message}")
    }

    suspend fun actualizar(id: Int, request: ActualizarUsuarioRequest): ResultadoApi<UsuarioDto> = try {
        val r = api.actualizarUsuario(id, request)
        if (r.isSuccessful && r.body() != null) ResultadoApi.Exito(r.body()!!)
        else ResultadoApi.Error("No se pudo actualizar el usuario.")
    } catch (e: Exception) {
        ResultadoApi.Error("Error de conexión: ${e.message}")
    }

    suspend fun desactivar(id: Int): ResultadoApi<Unit> = try {
        val r = api.desactivarUsuario(id)
        if (r.isSuccessful) ResultadoApi.Exito(Unit)
        else ResultadoApi.Error("No se pudo desactivar el usuario.")
    } catch (e: Exception) {
        ResultadoApi.Error("Error de conexión: ${e.message}")
    }
}
