package cl.aldemar.trazamar.data.remote.dto

data class MuestraDto(
    val id: Int,
    val centroId: Int,
    val trenId: Int,
    val lineaId: Int,
    val fecha: String,
    val hora: String? = null,
    val tramoMetros: Double? = null,
    val operadorId: Int,
    val conteoIndividuos: Int? = null,
    val observaciones: String? = null,
    val calibrePromedio: Double? = null,
    val calibreMin: Double? = null,
    val calibreMax: Double? = null,
    val pesoMuestra: Double? = null,
    val fotoUrl: String? = null,
    val estado: String,
    val comentarioSupervisor: String? = null,
    val supervisorId: Int? = null,
    val fechaRevision: String? = null,
    val centro: CentroDto? = null,
    val tren: TrenDto? = null,
    val linea: LineaDto? = null,
    val operador: UsuarioDto? = null,
    val supervisor: UsuarioDto? = null,
)

data class CrearMuestraRequest(
    val centroId: Int,
    val trenId: Int,
    val lineaId: Int,
    val fecha: String,
    val hora: String?,
    val tramoMetros: Double?,
    val operadorId: Int,
    val conteoIndividuos: Int? = null,
    val observaciones: String? = null,
    val calibrePromedio: Double? = null,
    val calibreMin: Double? = null,
    val calibreMax: Double? = null,
    val pesoMuestra: Double? = null,
)

data class ActualizarMuestraRequest(
    val conteoIndividuos: Int? = null,
    val observaciones: String? = null,
    val calibrePromedio: Double? = null,
    val calibreMin: Double? = null,
    val calibreMax: Double? = null,
    val pesoMuestra: Double? = null,
    val tramoMetros: Double? = null,
)

data class RevisionRequest(
    val estado: String,
    val comentarioSupervisor: String?,
)

data class FotoResponse(val fotoUrl: String)

data class ErroresResponse(val errores: List<String>? = null, val error: String? = null)
