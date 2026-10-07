package cl.aldemar.trazamar.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import cl.aldemar.trazamar.data.local.dao.CatalogoDao;
import cl.aldemar.trazamar.data.local.dao.CatalogoDao_Impl;
import cl.aldemar.trazamar.data.local.dao.MuestraDao;
import cl.aldemar.trazamar.data.local.dao.MuestraDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile MuestraDao _muestraDao;

  private volatile CatalogoDao _catalogoDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `muestras_cache` (`id` INTEGER NOT NULL, `centroId` INTEGER NOT NULL, `centroNombre` TEXT, `trenId` INTEGER NOT NULL, `trenNombre` TEXT, `lineaId` INTEGER NOT NULL, `lineaNombre` TEXT, `fecha` TEXT NOT NULL, `hora` TEXT, `tramoMetros` REAL, `operadorId` INTEGER NOT NULL, `operadorNombre` TEXT, `conteoIndividuos` INTEGER, `observaciones` TEXT, `calibrePromedio` REAL, `calibreMin` REAL, `calibreMax` REAL, `pesoMuestra` REAL, `fotoUrl` TEXT, `estado` TEXT NOT NULL, `comentarioSupervisor` TEXT, `supervisorNombre` TEXT, `sincronizada` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `centros_cache` (`id` INTEGER NOT NULL, `nombre` TEXT NOT NULL, `codigo` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `trenes_cache` (`id` INTEGER NOT NULL, `nombre` TEXT NOT NULL, `centroId` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `lineas_cache` (`id` INTEGER NOT NULL, `nombre` TEXT NOT NULL, `trenId` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '8f4dcdb2c9294803a59d31529e5303b7')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `muestras_cache`");
        db.execSQL("DROP TABLE IF EXISTS `centros_cache`");
        db.execSQL("DROP TABLE IF EXISTS `trenes_cache`");
        db.execSQL("DROP TABLE IF EXISTS `lineas_cache`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsMuestrasCache = new HashMap<String, TableInfo.Column>(23);
        _columnsMuestrasCache.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("centroId", new TableInfo.Column("centroId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("centroNombre", new TableInfo.Column("centroNombre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("trenId", new TableInfo.Column("trenId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("trenNombre", new TableInfo.Column("trenNombre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("lineaId", new TableInfo.Column("lineaId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("lineaNombre", new TableInfo.Column("lineaNombre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("fecha", new TableInfo.Column("fecha", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("hora", new TableInfo.Column("hora", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("tramoMetros", new TableInfo.Column("tramoMetros", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("operadorId", new TableInfo.Column("operadorId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("operadorNombre", new TableInfo.Column("operadorNombre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("conteoIndividuos", new TableInfo.Column("conteoIndividuos", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("observaciones", new TableInfo.Column("observaciones", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("calibrePromedio", new TableInfo.Column("calibrePromedio", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("calibreMin", new TableInfo.Column("calibreMin", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("calibreMax", new TableInfo.Column("calibreMax", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("pesoMuestra", new TableInfo.Column("pesoMuestra", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("fotoUrl", new TableInfo.Column("fotoUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("estado", new TableInfo.Column("estado", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("comentarioSupervisor", new TableInfo.Column("comentarioSupervisor", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("supervisorNombre", new TableInfo.Column("supervisorNombre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuestrasCache.put("sincronizada", new TableInfo.Column("sincronizada", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMuestrasCache = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMuestrasCache = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMuestrasCache = new TableInfo("muestras_cache", _columnsMuestrasCache, _foreignKeysMuestrasCache, _indicesMuestrasCache);
        final TableInfo _existingMuestrasCache = TableInfo.read(db, "muestras_cache");
        if (!_infoMuestrasCache.equals(_existingMuestrasCache)) {
          return new RoomOpenHelper.ValidationResult(false, "muestras_cache(cl.aldemar.trazamar.data.local.entities.MuestraEntity).\n"
                  + " Expected:\n" + _infoMuestrasCache + "\n"
                  + " Found:\n" + _existingMuestrasCache);
        }
        final HashMap<String, TableInfo.Column> _columnsCentrosCache = new HashMap<String, TableInfo.Column>(3);
        _columnsCentrosCache.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCentrosCache.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCentrosCache.put("codigo", new TableInfo.Column("codigo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCentrosCache = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCentrosCache = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCentrosCache = new TableInfo("centros_cache", _columnsCentrosCache, _foreignKeysCentrosCache, _indicesCentrosCache);
        final TableInfo _existingCentrosCache = TableInfo.read(db, "centros_cache");
        if (!_infoCentrosCache.equals(_existingCentrosCache)) {
          return new RoomOpenHelper.ValidationResult(false, "centros_cache(cl.aldemar.trazamar.data.local.entities.CentroEntity).\n"
                  + " Expected:\n" + _infoCentrosCache + "\n"
                  + " Found:\n" + _existingCentrosCache);
        }
        final HashMap<String, TableInfo.Column> _columnsTrenesCache = new HashMap<String, TableInfo.Column>(3);
        _columnsTrenesCache.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrenesCache.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrenesCache.put("centroId", new TableInfo.Column("centroId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTrenesCache = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTrenesCache = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrenesCache = new TableInfo("trenes_cache", _columnsTrenesCache, _foreignKeysTrenesCache, _indicesTrenesCache);
        final TableInfo _existingTrenesCache = TableInfo.read(db, "trenes_cache");
        if (!_infoTrenesCache.equals(_existingTrenesCache)) {
          return new RoomOpenHelper.ValidationResult(false, "trenes_cache(cl.aldemar.trazamar.data.local.entities.TrenEntity).\n"
                  + " Expected:\n" + _infoTrenesCache + "\n"
                  + " Found:\n" + _existingTrenesCache);
        }
        final HashMap<String, TableInfo.Column> _columnsLineasCache = new HashMap<String, TableInfo.Column>(3);
        _columnsLineasCache.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLineasCache.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLineasCache.put("trenId", new TableInfo.Column("trenId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLineasCache = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLineasCache = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLineasCache = new TableInfo("lineas_cache", _columnsLineasCache, _foreignKeysLineasCache, _indicesLineasCache);
        final TableInfo _existingLineasCache = TableInfo.read(db, "lineas_cache");
        if (!_infoLineasCache.equals(_existingLineasCache)) {
          return new RoomOpenHelper.ValidationResult(false, "lineas_cache(cl.aldemar.trazamar.data.local.entities.LineaEntity).\n"
                  + " Expected:\n" + _infoLineasCache + "\n"
                  + " Found:\n" + _existingLineasCache);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "8f4dcdb2c9294803a59d31529e5303b7", "060c59cf1602df2313e71eb9fb8b76d9");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "muestras_cache","centros_cache","trenes_cache","lineas_cache");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `muestras_cache`");
      _db.execSQL("DELETE FROM `centros_cache`");
      _db.execSQL("DELETE FROM `trenes_cache`");
      _db.execSQL("DELETE FROM `lineas_cache`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(MuestraDao.class, MuestraDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CatalogoDao.class, CatalogoDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public MuestraDao muestraDao() {
    if (_muestraDao != null) {
      return _muestraDao;
    } else {
      synchronized(this) {
        if(_muestraDao == null) {
          _muestraDao = new MuestraDao_Impl(this);
        }
        return _muestraDao;
      }
    }
  }

  @Override
  public CatalogoDao catalogoDao() {
    if (_catalogoDao != null) {
      return _catalogoDao;
    } else {
      synchronized(this) {
        if(_catalogoDao == null) {
          _catalogoDao = new CatalogoDao_Impl(this);
        }
        return _catalogoDao;
      }
    }
  }
}
