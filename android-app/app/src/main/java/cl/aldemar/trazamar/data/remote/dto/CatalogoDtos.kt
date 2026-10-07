package cl.aldemar.trazamar.data.remote.dto

data class LineaDto(
    val id: Int,
    val nombre: String,
    val trenId: Int,
    val activo: Boolean = true,
)

data class TrenDto(
    val id: Int,
    val nombre: String,
    val centroId: Int,
    val activo: Boolean = true,
    val lineas: List<LineaDto>? = null,
)

data class CentroDto(
    val id: Int,
    val nombre: String,
    val codigo: String,
    val activo: Boolean = true,
    val trenes: List<TrenDto>? = null,
)

data class CrearCentroRequest(val nombre: String, val codigo: String)
data class CrearTrenRequest(val nombre: String, val centroId: Int)
data class CrearLineaRequest(val nombre: String, val trenId: Int)
