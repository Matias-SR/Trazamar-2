package cl.aldemar.trazamar.data.remote.dto

data class EstadoCantidadDto(val estado: String, val total: Int)
data class CentroCantidadDto(val centro: String?, val total: Int)
data class SeriePuntoDto(val fecha: String, val totalIndividuos: Int, val totalMuestras: Int)

data class ResumenReporteDto(
    val total: Int,
    val promedioConteo: String?,
    val porEstado: List<EstadoCantidadDto>,
    val porCentro: List<CentroCantidadDto>,
    val serieTemporal: List<SeriePuntoDto>,
)
