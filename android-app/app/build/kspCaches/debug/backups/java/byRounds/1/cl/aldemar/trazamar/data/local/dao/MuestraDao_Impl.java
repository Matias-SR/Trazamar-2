package cl.aldemar.trazamar.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import cl.aldemar.trazamar.data.local.entities.MuestraEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MuestraDao_Impl implements MuestraDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MuestraEntity> __insertionAdapterOfMuestraEntity;

  private final SharedSQLiteStatement __preparedStmtOfLimpiar;

  public MuestraDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMuestraEntity = new EntityInsertionAdapter<MuestraEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `muestras_cache` (`id`,`centroId`,`centroNombre`,`trenId`,`trenNombre`,`lineaId`,`lineaNombre`,`fecha`,`hora`,`tramoMetros`,`operadorId`,`operadorNombre`,`conteoIndividuos`,`observaciones`,`calibrePromedio`,`calibreMin`,`calibreMax`,`pesoMuestra`,`fotoUrl`,`estado`,`comentarioSupervisor`,`supervisorNombre`,`sincronizada`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MuestraEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCentroId());
        if (entity.getCentroNombre() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCentroNombre());
        }
        statement.bindLong(4, entity.getTrenId());
        if (entity.getTrenNombre() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTrenNombre());
        }
        statement.bindLong(6, entity.getLineaId());
        if (entity.getLineaNombre() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLineaNombre());
        }
        statement.bindString(8, entity.getFecha());
        if (entity.getHora() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getHora());
        }
        if (entity.getTramoMetros() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getTramoMetros());
        }
        statement.bindLong(11, entity.getOperadorId());
        if (entity.getOperadorNombre() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getOperadorNombre());
        }
        if (entity.getConteoIndividuos() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getConteoIndividuos());
        }
        if (entity.getObservaciones() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getObservaciones());
        }
        if (entity.getCalibrePromedio() == null) {
          statement.bindNull(15);
        } else {
          statement.bindDouble(15, entity.getCalibrePromedio());
        }
        if (entity.getCalibreMin() == null) {
          statement.bindNull(16);
        } else {
          statement.bindDouble(16, entity.getCalibreMin());
        }
        if (entity.getCalibreMax() == null) {
          statement.bindNull(17);
        } else {
          statement.bindDouble(17, entity.getCalibreMax());
        }
        if (entity.getPesoMuestra() == null) {
          statement.bindNull(18);
        } else {
          statement.bindDouble(18, entity.getPesoMuestra());
        }
        if (entity.getFotoUrl() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getFotoUrl());
        }
        statement.bindString(20, entity.getEstado());
        if (entity.getComentarioSupervisor() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getComentarioSupervisor());
        }
        if (entity.getSupervisorNombre() == null) {
          statement.bindNull(22);
        } else {
          statement.bindString(22, entity.getSupervisorNombre());
        }
        final int _tmp = entity.getSincronizada() ? 1 : 0;
        statement.bindLong(23, _tmp);
      }
    };
    this.__preparedStmtOfLimpiar = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM muestras_cache";
        return _query;
      }
    };
  }

  @Override
  public Object insertarTodas(final List<MuestraEntity> muestras,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMuestraEntity.insert(muestras);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertar(final MuestraEntity muestra,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMuestraEntity.insert(muestra);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object limpiar(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfLimpiar.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfLimpiar.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MuestraEntity>> observarTodas() {
    final String _sql = "SELECT * FROM muestras_cache ORDER BY fecha DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"muestras_cache"}, new Callable<List<MuestraEntity>>() {
      @Override
      @NonNull
      public List<MuestraEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCentroId = CursorUtil.getColumnIndexOrThrow(_cursor, "centroId");
          final int _cursorIndexOfCentroNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "centroNombre");
          final int _cursorIndexOfTrenId = CursorUtil.getColumnIndexOrThrow(_cursor, "trenId");
          final int _cursorIndexOfTrenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "trenNombre");
          final int _cursorIndexOfLineaId = CursorUtil.getColumnIndexOrThrow(_cursor, "lineaId");
          final int _cursorIndexOfLineaNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "lineaNombre");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfHora = CursorUtil.getColumnIndexOrThrow(_cursor, "hora");
          final int _cursorIndexOfTramoMetros = CursorUtil.getColumnIndexOrThrow(_cursor, "tramoMetros");
          final int _cursorIndexOfOperadorId = CursorUtil.getColumnIndexOrThrow(_cursor, "operadorId");
          final int _cursorIndexOfOperadorNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "operadorNombre");
          final int _cursorIndexOfConteoIndividuos = CursorUtil.getColumnIndexOrThrow(_cursor, "conteoIndividuos");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfCalibrePromedio = CursorUtil.getColumnIndexOrThrow(_cursor, "calibrePromedio");
          final int _cursorIndexOfCalibreMin = CursorUtil.getColumnIndexOrThrow(_cursor, "calibreMin");
          final int _cursorIndexOfCalibreMax = CursorUtil.getColumnIndexOrThrow(_cursor, "calibreMax");
          final int _cursorIndexOfPesoMuestra = CursorUtil.getColumnIndexOrThrow(_cursor, "pesoMuestra");
          final int _cursorIndexOfFotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUrl");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfComentarioSupervisor = CursorUtil.getColumnIndexOrThrow(_cursor, "comentarioSupervisor");
          final int _cursorIndexOfSupervisorNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "supervisorNombre");
          final int _cursorIndexOfSincronizada = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizada");
          final List<MuestraEntity> _result = new ArrayList<MuestraEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MuestraEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpCentroId;
            _tmpCentroId = _cursor.getInt(_cursorIndexOfCentroId);
            final String _tmpCentroNombre;
            if (_cursor.isNull(_cursorIndexOfCentroNombre)) {
              _tmpCentroNombre = null;
            } else {
              _tmpCentroNombre = _cursor.getString(_cursorIndexOfCentroNombre);
            }
            final int _tmpTrenId;
            _tmpTrenId = _cursor.getInt(_cursorIndexOfTrenId);
            final String _tmpTrenNombre;
            if (_cursor.isNull(_cursorIndexOfTrenNombre)) {
              _tmpTrenNombre = null;
            } else {
              _tmpTrenNombre = _cursor.getString(_cursorIndexOfTrenNombre);
            }
            final int _tmpLineaId;
            _tmpLineaId = _cursor.getInt(_cursorIndexOfLineaId);
            final String _tmpLineaNombre;
            if (_cursor.isNull(_cursorIndexOfLineaNombre)) {
              _tmpLineaNombre = null;
            } else {
              _tmpLineaNombre = _cursor.getString(_cursorIndexOfLineaNombre);
            }
            final String _tmpFecha;
            _tmpFecha = _cursor.getString(_cursorIndexOfFecha);
            final String _tmpHora;
            if (_cursor.isNull(_cursorIndexOfHora)) {
              _tmpHora = null;
            } else {
              _tmpHora = _cursor.getString(_cursorIndexOfHora);
            }
            final Double _tmpTramoMetros;
            if (_cursor.isNull(_cursorIndexOfTramoMetros)) {
              _tmpTramoMetros = null;
            } else {
              _tmpTramoMetros = _cursor.getDouble(_cursorIndexOfTramoMetros);
            }
            final int _tmpOperadorId;
            _tmpOperadorId = _cursor.getInt(_cursorIndexOfOperadorId);
            final String _tmpOperadorNombre;
            if (_cursor.isNull(_cursorIndexOfOperadorNombre)) {
              _tmpOperadorNombre = null;
            } else {
              _tmpOperadorNombre = _cursor.getString(_cursorIndexOfOperadorNombre);
            }
            final Integer _tmpConteoIndividuos;
            if (_cursor.isNull(_cursorIndexOfConteoIndividuos)) {
              _tmpConteoIndividuos = null;
            } else {
              _tmpConteoIndividuos = _cursor.getInt(_cursorIndexOfConteoIndividuos);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final Double _tmpCalibrePromedio;
            if (_cursor.isNull(_cursorIndexOfCalibrePromedio)) {
              _tmpCalibrePromedio = null;
            } else {
              _tmpCalibrePromedio = _cursor.getDouble(_cursorIndexOfCalibrePromedio);
            }
            final Double _tmpCalibreMin;
            if (_cursor.isNull(_cursorIndexOfCalibreMin)) {
              _tmpCalibreMin = null;
            } else {
              _tmpCalibreMin = _cursor.getDouble(_cursorIndexOfCalibreMin);
            }
            final Double _tmpCalibreMax;
            if (_cursor.isNull(_cursorIndexOfCalibreMax)) {
              _tmpCalibreMax = null;
            } else {
              _tmpCalibreMax = _cursor.getDouble(_cursorIndexOfCalibreMax);
            }
            final Double _tmpPesoMuestra;
            if (_cursor.isNull(_cursorIndexOfPesoMuestra)) {
              _tmpPesoMuestra = null;
            } else {
              _tmpPesoMuestra = _cursor.getDouble(_cursorIndexOfPesoMuestra);
            }
            final String _tmpFotoUrl;
            if (_cursor.isNull(_cursorIndexOfFotoUrl)) {
              _tmpFotoUrl = null;
            } else {
              _tmpFotoUrl = _cursor.getString(_cursorIndexOfFotoUrl);
            }
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpComentarioSupervisor;
            if (_cursor.isNull(_cursorIndexOfComentarioSupervisor)) {
              _tmpComentarioSupervisor = null;
            } else {
              _tmpComentarioSupervisor = _cursor.getString(_cursorIndexOfComentarioSupervisor);
            }
            final String _tmpSupervisorNombre;
            if (_cursor.isNull(_cursorIndexOfSupervisorNombre)) {
              _tmpSupervisorNombre = null;
            } else {
              _tmpSupervisorNombre = _cursor.getString(_cursorIndexOfSupervisorNombre);
            }
            final boolean _tmpSincronizada;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSincronizada);
            _tmpSincronizada = _tmp != 0;
            _item = new MuestraEntity(_tmpId,_tmpCentroId,_tmpCentroNombre,_tmpTrenId,_tmpTrenNombre,_tmpLineaId,_tmpLineaNombre,_tmpFecha,_tmpHora,_tmpTramoMetros,_tmpOperadorId,_tmpOperadorNombre,_tmpConteoIndividuos,_tmpObservaciones,_tmpCalibrePromedio,_tmpCalibreMin,_tmpCalibreMax,_tmpPesoMuestra,_tmpFotoUrl,_tmpEstado,_tmpComentarioSupervisor,_tmpSupervisorNombre,_tmpSincronizada);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MuestraEntity>> observarPorEstado(final String estado) {
    final String _sql = "SELECT * FROM muestras_cache WHERE estado = ? ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, estado);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"muestras_cache"}, new Callable<List<MuestraEntity>>() {
      @Override
      @NonNull
      public List<MuestraEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCentroId = CursorUtil.getColumnIndexOrThrow(_cursor, "centroId");
          final int _cursorIndexOfCentroNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "centroNombre");
          final int _cursorIndexOfTrenId = CursorUtil.getColumnIndexOrThrow(_cursor, "trenId");
          final int _cursorIndexOfTrenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "trenNombre");
          final int _cursorIndexOfLineaId = CursorUtil.getColumnIndexOrThrow(_cursor, "lineaId");
          final int _cursorIndexOfLineaNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "lineaNombre");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfHora = CursorUtil.getColumnIndexOrThrow(_cursor, "hora");
          final int _cursorIndexOfTramoMetros = CursorUtil.getColumnIndexOrThrow(_cursor, "tramoMetros");
          final int _cursorIndexOfOperadorId = CursorUtil.getColumnIndexOrThrow(_cursor, "operadorId");
          final int _cursorIndexOfOperadorNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "operadorNombre");
          final int _cursorIndexOfConteoIndividuos = CursorUtil.getColumnIndexOrThrow(_cursor, "conteoIndividuos");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfCalibrePromedio = CursorUtil.getColumnIndexOrThrow(_cursor, "calibrePromedio");
          final int _cursorIndexOfCalibreMin = CursorUtil.getColumnIndexOrThrow(_cursor, "calibreMin");
          final int _cursorIndexOfCalibreMax = CursorUtil.getColumnIndexOrThrow(_cursor, "calibreMax");
          final int _cursorIndexOfPesoMuestra = CursorUtil.getColumnIndexOrThrow(_cursor, "pesoMuestra");
          final int _cursorIndexOfFotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUrl");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfComentarioSupervisor = CursorUtil.getColumnIndexOrThrow(_cursor, "comentarioSupervisor");
          final int _cursorIndexOfSupervisorNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "supervisorNombre");
          final int _cursorIndexOfSincronizada = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizada");
          final List<MuestraEntity> _result = new ArrayList<MuestraEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MuestraEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpCentroId;
            _tmpCentroId = _cursor.getInt(_cursorIndexOfCentroId);
            final String _tmpCentroNombre;
            if (_cursor.isNull(_cursorIndexOfCentroNombre)) {
              _tmpCentroNombre = null;
            } else {
              _tmpCentroNombre = _cursor.getString(_cursorIndexOfCentroNombre);
            }
            final int _tmpTrenId;
            _tmpTrenId = _cursor.getInt(_cursorIndexOfTrenId);
            final String _tmpTrenNombre;
            if (_cursor.isNull(_cursorIndexOfTrenNombre)) {
              _tmpTrenNombre = null;
            } else {
              _tmpTrenNombre = _cursor.getString(_cursorIndexOfTrenNombre);
            }
            final int _tmpLineaId;
            _tmpLineaId = _cursor.getInt(_cursorIndexOfLineaId);
            final String _tmpLineaNombre;
            if (_cursor.isNull(_cursorIndexOfLineaNombre)) {
              _tmpLineaNombre = null;
            } else {
              _tmpLineaNombre = _cursor.getString(_cursorIndexOfLineaNombre);
            }
            final String _tmpFecha;
            _tmpFecha = _cursor.getString(_cursorIndexOfFecha);
            final String _tmpHora;
            if (_cursor.isNull(_cursorIndexOfHora)) {
              _tmpHora = null;
            } else {
              _tmpHora = _cursor.getString(_cursorIndexOfHora);
            }
            final Double _tmpTramoMetros;
            if (_cursor.isNull(_cursorIndexOfTramoMetros)) {
              _tmpTramoMetros = null;
            } else {
              _tmpTramoMetros = _cursor.getDouble(_cursorIndexOfTramoMetros);
            }
            final int _tmpOperadorId;
            _tmpOperadorId = _cursor.getInt(_cursorIndexOfOperadorId);
            final String _tmpOperadorNombre;
            if (_cursor.isNull(_cursorIndexOfOperadorNombre)) {
              _tmpOperadorNombre = null;
            } else {
              _tmpOperadorNombre = _cursor.getString(_cursorIndexOfOperadorNombre);
            }
            final Integer _tmpConteoIndividuos;
            if (_cursor.isNull(_cursorIndexOfConteoIndividuos)) {
              _tmpConteoIndividuos = null;
            } else {
              _tmpConteoIndividuos = _cursor.getInt(_cursorIndexOfConteoIndividuos);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final Double _tmpCalibrePromedio;
            if (_cursor.isNull(_cursorIndexOfCalibrePromedio)) {
              _tmpCalibrePromedio = null;
            } else {
              _tmpCalibrePromedio = _cursor.getDouble(_cursorIndexOfCalibrePromedio);
            }
            final Double _tmpCalibreMin;
            if (_cursor.isNull(_cursorIndexOfCalibreMin)) {
              _tmpCalibreMin = null;
            } else {
              _tmpCalibreMin = _cursor.getDouble(_cursorIndexOfCalibreMin);
            }
            final Double _tmpCalibreMax;
            if (_cursor.isNull(_cursorIndexOfCalibreMax)) {
              _tmpCalibreMax = null;
            } else {
              _tmpCalibreMax = _cursor.getDouble(_cursorIndexOfCalibreMax);
            }
            final Double _tmpPesoMuestra;
            if (_cursor.isNull(_cursorIndexOfPesoMuestra)) {
              _tmpPesoMuestra = null;
            } else {
              _tmpPesoMuestra = _cursor.getDouble(_cursorIndexOfPesoMuestra);
            }
            final String _tmpFotoUrl;
            if (_cursor.isNull(_cursorIndexOfFotoUrl)) {
              _tmpFotoUrl = null;
            } else {
              _tmpFotoUrl = _cursor.getString(_cursorIndexOfFotoUrl);
            }
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpComentarioSupervisor;
            if (_cursor.isNull(_cursorIndexOfComentarioSupervisor)) {
              _tmpComentarioSupervisor = null;
            } else {
              _tmpComentarioSupervisor = _cursor.getString(_cursorIndexOfComentarioSupervisor);
            }
            final String _tmpSupervisorNombre;
            if (_cursor.isNull(_cursorIndexOfSupervisorNombre)) {
              _tmpSupervisorNombre = null;
            } else {
              _tmpSupervisorNombre = _cursor.getString(_cursorIndexOfSupervisorNombre);
            }
            final boolean _tmpSincronizada;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSincronizada);
            _tmpSincronizada = _tmp != 0;
            _item = new MuestraEntity(_tmpId,_tmpCentroId,_tmpCentroNombre,_tmpTrenId,_tmpTrenNombre,_tmpLineaId,_tmpLineaNombre,_tmpFecha,_tmpHora,_tmpTramoMetros,_tmpOperadorId,_tmpOperadorNombre,_tmpConteoIndividuos,_tmpObservaciones,_tmpCalibrePromedio,_tmpCalibreMin,_tmpCalibreMax,_tmpPesoMuestra,_tmpFotoUrl,_tmpEstado,_tmpComentarioSupervisor,_tmpSupervisorNombre,_tmpSincronizada);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object obtenerPorId(final int id, final Continuation<? super MuestraEntity> $completion) {
    final String _sql = "SELECT * FROM muestras_cache WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MuestraEntity>() {
      @Override
      @Nullable
      public MuestraEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCentroId = CursorUtil.getColumnIndexOrThrow(_cursor, "centroId");
          final int _cursorIndexOfCentroNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "centroNombre");
          final int _cursorIndexOfTrenId = CursorUtil.getColumnIndexOrThrow(_cursor, "trenId");
          final int _cursorIndexOfTrenNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "trenNombre");
          final int _cursorIndexOfLineaId = CursorUtil.getColumnIndexOrThrow(_cursor, "lineaId");
          final int _cursorIndexOfLineaNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "lineaNombre");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfHora = CursorUtil.getColumnIndexOrThrow(_cursor, "hora");
          final int _cursorIndexOfTramoMetros = CursorUtil.getColumnIndexOrThrow(_cursor, "tramoMetros");
          final int _cursorIndexOfOperadorId = CursorUtil.getColumnIndexOrThrow(_cursor, "operadorId");
          final int _cursorIndexOfOperadorNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "operadorNombre");
          final int _cursorIndexOfConteoIndividuos = CursorUtil.getColumnIndexOrThrow(_cursor, "conteoIndividuos");
          final int _cursorIndexOfObservaciones = CursorUtil.getColumnIndexOrThrow(_cursor, "observaciones");
          final int _cursorIndexOfCalibrePromedio = CursorUtil.getColumnIndexOrThrow(_cursor, "calibrePromedio");
          final int _cursorIndexOfCalibreMin = CursorUtil.getColumnIndexOrThrow(_cursor, "calibreMin");
          final int _cursorIndexOfCalibreMax = CursorUtil.getColumnIndexOrThrow(_cursor, "calibreMax");
          final int _cursorIndexOfPesoMuestra = CursorUtil.getColumnIndexOrThrow(_cursor, "pesoMuestra");
          final int _cursorIndexOfFotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUrl");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfComentarioSupervisor = CursorUtil.getColumnIndexOrThrow(_cursor, "comentarioSupervisor");
          final int _cursorIndexOfSupervisorNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "supervisorNombre");
          final int _cursorIndexOfSincronizada = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizada");
          final MuestraEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpCentroId;
            _tmpCentroId = _cursor.getInt(_cursorIndexOfCentroId);
            final String _tmpCentroNombre;
            if (_cursor.isNull(_cursorIndexOfCentroNombre)) {
              _tmpCentroNombre = null;
            } else {
              _tmpCentroNombre = _cursor.getString(_cursorIndexOfCentroNombre);
            }
            final int _tmpTrenId;
            _tmpTrenId = _cursor.getInt(_cursorIndexOfTrenId);
            final String _tmpTrenNombre;
            if (_cursor.isNull(_cursorIndexOfTrenNombre)) {
              _tmpTrenNombre = null;
            } else {
              _tmpTrenNombre = _cursor.getString(_cursorIndexOfTrenNombre);
            }
            final int _tmpLineaId;
            _tmpLineaId = _cursor.getInt(_cursorIndexOfLineaId);
            final String _tmpLineaNombre;
            if (_cursor.isNull(_cursorIndexOfLineaNombre)) {
              _tmpLineaNombre = null;
            } else {
              _tmpLineaNombre = _cursor.getString(_cursorIndexOfLineaNombre);
            }
            final String _tmpFecha;
            _tmpFecha = _cursor.getString(_cursorIndexOfFecha);
            final String _tmpHora;
            if (_cursor.isNull(_cursorIndexOfHora)) {
              _tmpHora = null;
            } else {
              _tmpHora = _cursor.getString(_cursorIndexOfHora);
            }
            final Double _tmpTramoMetros;
            if (_cursor.isNull(_cursorIndexOfTramoMetros)) {
              _tmpTramoMetros = null;
            } else {
              _tmpTramoMetros = _cursor.getDouble(_cursorIndexOfTramoMetros);
            }
            final int _tmpOperadorId;
            _tmpOperadorId = _cursor.getInt(_cursorIndexOfOperadorId);
            final String _tmpOperadorNombre;
            if (_cursor.isNull(_cursorIndexOfOperadorNombre)) {
              _tmpOperadorNombre = null;
            } else {
              _tmpOperadorNombre = _cursor.getString(_cursorIndexOfOperadorNombre);
            }
            final Integer _tmpConteoIndividuos;
            if (_cursor.isNull(_cursorIndexOfConteoIndividuos)) {
              _tmpConteoIndividuos = null;
            } else {
              _tmpConteoIndividuos = _cursor.getInt(_cursorIndexOfConteoIndividuos);
            }
            final String _tmpObservaciones;
            if (_cursor.isNull(_cursorIndexOfObservaciones)) {
              _tmpObservaciones = null;
            } else {
              _tmpObservaciones = _cursor.getString(_cursorIndexOfObservaciones);
            }
            final Double _tmpCalibrePromedio;
            if (_cursor.isNull(_cursorIndexOfCalibrePromedio)) {
              _tmpCalibrePromedio = null;
            } else {
              _tmpCalibrePromedio = _cursor.getDouble(_cursorIndexOfCalibrePromedio);
            }
            final Double _tmpCalibreMin;
            if (_cursor.isNull(_cursorIndexOfCalibreMin)) {
              _tmpCalibreMin = null;
            } else {
              _tmpCalibreMin = _cursor.getDouble(_cursorIndexOfCalibreMin);
            }
            final Double _tmpCalibreMax;
            if (_cursor.isNull(_cursorIndexOfCalibreMax)) {
              _tmpCalibreMax = null;
            } else {
              _tmpCalibreMax = _cursor.getDouble(_cursorIndexOfCalibreMax);
            }
            final Double _tmpPesoMuestra;
            if (_cursor.isNull(_cursorIndexOfPesoMuestra)) {
              _tmpPesoMuestra = null;
            } else {
              _tmpPesoMuestra = _cursor.getDouble(_cursorIndexOfPesoMuestra);
            }
            final String _tmpFotoUrl;
            if (_cursor.isNull(_cursorIndexOfFotoUrl)) {
              _tmpFotoUrl = null;
            } else {
              _tmpFotoUrl = _cursor.getString(_cursorIndexOfFotoUrl);
            }
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpComentarioSupervisor;
            if (_cursor.isNull(_cursorIndexOfComentarioSupervisor)) {
              _tmpComentarioSupervisor = null;
            } else {
              _tmpComentarioSupervisor = _cursor.getString(_cursorIndexOfComentarioSupervisor);
            }
            final String _tmpSupervisorNombre;
            if (_cursor.isNull(_cursorIndexOfSupervisorNombre)) {
              _tmpSupervisorNombre = null;
            } else {
              _tmpSupervisorNombre = _cursor.getString(_cursorIndexOfSupervisorNombre);
            }
            final boolean _tmpSincronizada;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSincronizada);
            _tmpSincronizada = _tmp != 0;
            _result = new MuestraEntity(_tmpId,_tmpCentroId,_tmpCentroNombre,_tmpTrenId,_tmpTrenNombre,_tmpLineaId,_tmpLineaNombre,_tmpFecha,_tmpHora,_tmpTramoMetros,_tmpOperadorId,_tmpOperadorNombre,_tmpConteoIndividuos,_tmpObservaciones,_tmpCalibrePromedio,_tmpCalibreMin,_tmpCalibreMax,_tmpPesoMuestra,_tmpFotoUrl,_tmpEstado,_tmpComentarioSupervisor,_tmpSupervisorNombre,_tmpSincronizada);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
