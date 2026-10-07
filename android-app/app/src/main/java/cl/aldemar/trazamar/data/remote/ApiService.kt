package cl.aldemar.trazamar.data.remote

import cl.aldemar.trazamar.data.remote.dto.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ---------- Autenticación ----------
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // ---------- Usuarios (admin) ----------
    @GET("usuarios")
    suspend fun listarUsuarios(): Response<List<UsuarioDto>>

    @POST("usuarios")
    suspend fun crearUsuario(@Body request: CrearUsuarioRequest): Response<UsuarioDto>

    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(@Path("id") id: Int, @Body request: ActualizarUsuarioRequest): Response<UsuarioDto>

    @DELETE("usuarios/{id}")
    suspend fun desactivarUsuario(@Path("id") id: Int): Response<ResponseBody>

    // ---------- Catálogos ----------
    @GET("catalogos/centros")
    suspend fun listarCentros(): Response<List<CentroDto>>

    @GET("catalogos/trenes")
    suspend fun listarTrenes(@Query("centroId") centroId: Int? = null): Response<List<TrenDto>>

    @GET("catalogos/lineas")
    suspend fun listarLineas(@Query("trenId") trenId: Int? = null): Response<List<LineaDto>>

    @POST("catalogos/centros")
    suspend fun crearCentro(@Body request: CrearCentroRequest): Response<CentroDto>

    @POST("catalogos/trenes")
    suspend fun crearTren(@Body request: CrearTrenRequest): Response<TrenDto>

    @POST("catalogos/lineas")
    suspend fun crearLinea(@Body request: CrearLineaRequest): Response<LineaDto>

    // ---------- Muestras ----------
    @GET("muestras")
    suspend fun listarMuestras(
        @Query("centroId") centroId: Int? = null,
        @Query("trenId") trenId: Int? = null,
        @Query("lineaId") lineaId: Int? = null,
        @Query("estado") estado: String? = null,
        @Query("operadorId") operadorId: Int? = null,
        @Query("fechaDesde") fechaDesde: String? = null,
        @Query("fechaHasta") fechaHasta: String? = null,
        @Query("q") q: String? = null,
    ): Response<List<MuestraDto>>

    @GET("muestras/{id}")
    suspend fun obtenerMuestra(@Path("id") id: Int): Response<MuestraDto>

    @POST("muestras")
    suspend fun crearMuestra(@Body request: CrearMuestraRequest): Response<MuestraDto>

    @PUT("muestras/{id}")
    suspend fun actualizarMuestra(@Path("id") id: Int, @Body request: ActualizarMuestraRequest): Response<MuestraDto>

    @POST("muestras/{id}/enviar")
    suspend fun enviarMuestraARevision(@Path("id") id: Int): Response<MuestraDto>

    @Multipart
    @POST("muestras/{id}/foto")
    suspend fun subirFoto(@Path("id") id: Int, @Part foto: MultipartBody.Part): Response<FotoResponse>

    @PATCH("muestras/{id}/revision")
    suspend fun revisarMuestra(@Path("id") id: Int, @Body request: RevisionRequest): Response<MuestraDto>

    @DELETE("muestras/{id}")
    suspend fun eliminarMuestra(@Path("id") id: Int): Response<ResponseBody>

    // ---------- Reportes ----------
    @GET("reportes/resumen")
    suspend fun resumenReportes(
        @Query("centroId") centroId: Int? = null,
        @Query("trenId") trenId: Int? = null,
        @Query("lineaId") lineaId: Int? = null,
        @Query("estado") estado: String? = null,
        @Query("fechaDesde") fechaDesde: String? = null,
        @Query("fechaHasta") fechaHasta: String? = null,
    ): Response<ResumenReporteDto>
}
