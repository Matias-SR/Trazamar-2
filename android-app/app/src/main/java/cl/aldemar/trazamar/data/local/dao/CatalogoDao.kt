package cl.aldemar.trazamar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.aldemar.trazamar.data.local.entities.CentroEntity
import cl.aldemar.trazamar.data.local.entities.LineaEntity
import cl.aldemar.trazamar.data.local.entities.TrenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogoDao {
    @Query("SELECT * FROM centros_cache ORDER BY nombre")
    fun observarCentros(): Flow<List<CentroEntity>>

    @Query("SELECT * FROM trenes_cache WHERE centroId = :centroId ORDER BY nombre")
    fun observarTrenes(centroId: Int): Flow<List<TrenEntity>>

    @Query("SELECT * FROM lineas_cache WHERE trenId = :trenId ORDER BY nombre")
    fun observarLineas(trenId: Int): Flow<List<LineaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCentros(centros: List<CentroEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTrenes(trenes: List<TrenEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarLineas(lineas: List<LineaEntity>)
}
