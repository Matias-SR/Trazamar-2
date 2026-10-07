package cl.aldemar.trazamar.data.repository

import android.content.Context
import cl.aldemar.trazamar.data.local.AppDatabase
import cl.aldemar.trazamar.data.local.entities.MuestraEntity
import cl.aldemar.trazamar.data.remote.ApiService
import cl.aldemar.trazamar.data.remote.RetrofitClient
import cl.aldemar.trazamar.data.remote.dto.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MuestraRepository(private val context: Context) {
    private val api: ApiService = RetrofitClient.getInstance(context)
    private val dao = AppDatabase.getInstance(context).muestraDao()

    fun observarCache(): Flow<List<MuestraEntity>> = dao.observarTodas()

    /** Descarga el historial filtrado desde el backend y refresca la caché local. */
    suspend fun sincronizarHistorial(
        centroId: Int? = null, trenId: Int? = null, lineaId: Int? = null,
        estado: String? = null, operadorId: Int? = null,
        fechaDesde: String? = null, fechaHasta: String? = null,
    ): ResultadoApi<List<MuestraDto>> {
        return try {
            val respuesta = api.listarMuestras(centroId, trenId, lineaId, estado, operadorId, fechaDesde, fechaHasta)
            if (respuesta.isSuccessful && respuesta.body() != null) {
                val muestras = respuesta.body()!!
                dao.insertarTodas(muestras.map { it.aEntidad() })
                ResultadoApi.Exito(muestras)
            } else {
                ResultadoApi.Error("No se pudo obtener el historial de muestras.")
            }
        } catch (e: Exception) {
            ResultadoApi.Error("Sin conexión. Mostrando el último historial guardado en este dispositivo.")
        }
    }

    suspend fun obtenerMuestra(id: Int): ResultadoApi<MuestraDto> {
        return try {
            val respuesta = api.obtenerMuestra(id)
            if (respuesta.isSuccessful && respuesta.body() != null) ResultadoApi.Exito(respuesta.body()!!)
            else ResultadoApi.Error("Muestra no encontrada.")
        } catch (e: Exception) {
            ResultadoApi.Error("No se pudo cargar la muestra: ${e.message}")
        }
    }

    suspend fun crearMuestra(request: CrearMuestraRequest): ResultadoApi<MuestraDto> {
        return try {
            val respuesta = api.crearMuestra(request)
            if (respuesta.isSuccessful && respuesta.body() != null) {
                dao.insertar(respuesta.body()!!.aEntidad())
                ResultadoApi.Exito(respuesta.body()!!)
            } else {
                ResultadoApi.Error(extraerError(respuesta.errorBody()?.string()))
            }
        } catch (e: Exception) {
            ResultadoApi.Error("No se pudo crear la muestra (sin conexión): ${e.message}")
        }
    }

    suspend fun actualizarMuestra(id: Int, request: ActualizarMuestraRequest): ResultadoApi<MuestraDto> {
        return try {
            val respuesta = api.actualizarMuestra(id, request)
            if (respuesta.isSuccessful && respuesta.body() != null) {
                dao.insertar(respuesta.body()!!.aEntidad())
                ResultadoApi.Exito(respuesta.body()!!)
            } else {
                ResultadoApi.Error(extraerError(respuesta.errorBody()?.string()))
            }
        } catch (e: Exception) {
            ResultadoApi.Error("No se pudo guardar el registro: ${e.message}")
        }
    }

    suspend fun enviarARevision(id: Int): ResultadoApi<MuestraDto> {
        return try {
            val respuesta = api.enviarMuestraARevision(id)
            if (respuesta.isSuccessful && respuesta.body() != null) {
                dao.insertar(respuesta.body()!!.aEntidad())
                ResultadoApi.Exito(respuesta.body()!!)
            } else {
                ResultadoApi.Error(extraerError(respuesta.errorBody()?.string()))
            }
        } catch (e: Exception) {
            ResultadoApi.Error("No se pudo enviar a revisión: ${e.message}")
        }
    }

    suspend fun subirFoto(id: Int, archivo: File): ResultadoApi<String> {
        return try {
            val cuerpo = archivo.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val parte = MultipartBody.Part.createFormData("foto", archivo.name, cuerpo)
            val respuesta = api.subirFoto(id, parte)
            if (respuesta.isSuccessful && respuesta.body() != null) {
                ResultadoApi.Exito(respuesta.body()!!.fotoUrl)
            } else {
                ResultadoApi.Error("No se pudo subir la fotografía.")
            }
        } catch (e: Exception) {
            ResultadoApi.Error("No se pudo subir la fotografía: ${e.message}")
        }
    }

    suspend fun revisar(id: Int, estado: String, comentario: String?): ResultadoApi<MuestraDto> {
        return try {
            val respuesta = api.revisarMuestra(id, RevisionRequest(estado, comentario))
            if (respuesta.isSuccessful && respuesta.body() != null) {
                dao.insertar(respuesta.body()!!.aEntidad())
                ResultadoApi.Exito(respuesta.body()!!)
            } else {
                ResultadoApi.Error(extraerError(respuesta.errorBody()?.string()))
            }
        } catch (e: Exception) {
            ResultadoApi.Error("No se pudo registrar la revisión: ${e.message}")
        }
    }

    private fun extraerError(cuerpo: String?): String {
        if (cuerpo.isNullOrBlank()) return "Ocurrió un error inesperado."
        return try {
            val gson = com.google.gson.Gson()
            val err = gson.fromJson(cuerpo, ErroresResponse::class.java)
            err.errores?.joinToString(" · ") ?: err.error ?: "Ocurrió un error inesperado."
        } catch (e: Exception) {
            "Ocurrió un error inesperado."
        }
    }
}

private fun MuestraDto.aEntidad() = MuestraEntity(
    id = id,
    centroId = centroId,
    centroNombre = centro?.nombre,
    trenId = trenId,
    trenNombre = tren?.nombre,
    lineaId = lineaId,
    lineaNombre = linea?.nombre,
    fecha = fecha,
    hora = hora,
    tramoMetros = tramoMetros,
    operadorId = operadorId,
    operadorNombre = operador?.nombreCompleto,
    conteoIndividuos = conteoIndividuos,
    observaciones = observaciones,
    calibrePromedio = calibrePromedio,
    calibreMin = calibreMin,
    calibreMax = calibreMax,
    pesoMuestra = pesoMuestra,
    fotoUrl = fotoUrl,
    estado = estado,
    comentarioSupervisor = comentarioSupervisor,
    supervisorNombre = supervisor?.nombreCompleto,
    sincronizada = true,
)
