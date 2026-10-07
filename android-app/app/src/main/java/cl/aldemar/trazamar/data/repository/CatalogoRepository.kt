package cl.aldemar.trazamar.data.repository

import android.content.Context
import cl.aldemar.trazamar.data.local.AppDatabase
import cl.aldemar.trazamar.data.remote.ApiService
import cl.aldemar.trazamar.data.remote.RetrofitClient
import cl.aldemar.trazamar.data.remote.dto.CentroDto

class CatalogoRepository(private val context: Context) {
    private val api: ApiService = RetrofitClient.getInstance(context)
    private val db = AppDatabase.getInstance(context)

    /** Trae los catálogos desde el backend (incluye trenes y líneas anidados). */
    suspend fun obtenerCentros(): ResultadoApi<List<CentroDto>> {
        return try {
            val respuesta = api.listarCentros()
            if (respuesta.isSuccessful && respuesta.body() != null) {
                ResultadoApi.Exito(respuesta.body()!!)
            } else {
                ResultadoApi.Error("No se pudieron cargar los centros de cultivo.")
            }
        } catch (e: Exception) {
            ResultadoApi.Error("Sin conexión. Mostrando datos guardados si existen.")
        }
    }

    suspend fun crearCentro(nombre: String, codigo: String) =
        api.crearCentro(cl.aldemar.trazamar.data.remote.dto.CrearCentroRequest(nombre, codigo))

    suspend fun crearTren(nombre: String, centroId: Int) =
        api.crearTren(cl.aldemar.trazamar.data.remote.dto.CrearTrenRequest(nombre, centroId))

    suspend fun crearLinea(nombre: String, trenId: Int) =
        api.crearLinea(cl.aldemar.trazamar.data.remote.dto.CrearLineaRequest(nombre, trenId))
}
