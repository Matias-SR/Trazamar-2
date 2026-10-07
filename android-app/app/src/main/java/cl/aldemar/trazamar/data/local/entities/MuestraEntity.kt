package cl.aldemar.trazamar.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Caché local de muestras. Permite al Operador y Supervisor seguir viendo
 * el historial reciente aunque pierdan conectividad en terreno (RF de
 * trabajo offline mencionado en el caso). El id negativo indica un registro
 * creado sin conexión, pendiente de sincronizar con el backend (idServidor = null).
 */
@Entity(tableName = "muestras_cache")
data class MuestraEntity(
    @PrimaryKey val id: Int,
    val centroId: Int,
    val centroNombre: String?,
    val trenId: Int,
    val trenNombre: String?,
    val lineaId: Int,
    val lineaNombre: String?,
    val fecha: String,
    val hora: String?,
    val tramoMetros: Double?,
    val operadorId: Int,
    val operadorNombre: String?,
    val conteoIndividuos: Int?,
    val observaciones: String?,
    val calibrePromedio: Double?,
    val calibreMin: Double?,
    val calibreMax: Double?,
    val pesoMuestra: Double?,
    val fotoUrl: String?,
    val estado: String,
    val comentarioSupervisor: String?,
    val supervisorNombre: String?,
    val sincronizada: Boolean = true,
)
