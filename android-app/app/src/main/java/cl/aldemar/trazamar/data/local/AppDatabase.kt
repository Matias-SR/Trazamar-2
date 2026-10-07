package cl.aldemar.trazamar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.aldemar.trazamar.data.local.dao.CatalogoDao
import cl.aldemar.trazamar.data.local.dao.MuestraDao
import cl.aldemar.trazamar.data.local.entities.CentroEntity
import cl.aldemar.trazamar.data.local.entities.LineaEntity
import cl.aldemar.trazamar.data.local.entities.MuestraEntity
import cl.aldemar.trazamar.data.local.entities.TrenEntity

@Database(
    entities = [MuestraEntity::class, CentroEntity::class, TrenEntity::class, LineaEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun muestraDao(): MuestraDao
    abstract fun catalogoDao(): CatalogoDao

    companion object {
        @Volatile private var instancia: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "trazamar.db"
                ).fallbackToDestructiveMigration().build().also { instancia = it }
            }
        }
    }
}
