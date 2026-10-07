package cl.aldemar.trazamar.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import cl.aldemar.trazamar.data.local.entities.CentroEntity;
import cl.aldemar.trazamar.data.local.entities.LineaEntity;
import cl.aldemar.trazamar.data.local.entities.TrenEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class CatalogoDao_Impl implements CatalogoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CentroEntity> __insertionAdapterOfCentroEntity;

  private final EntityInsertionAdapter<TrenEntity> __insertionAdapterOfTrenEntity;

  private final EntityInsertionAdapter<LineaEntity> __insertionAdapterOfLineaEntity;

  public CatalogoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCentroEntity = new EntityInsertionAdapter<CentroEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `centros_cache` (`id`,`nombre`,`codigo`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CentroEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getNombre());
        statement.bindString(3, entity.getCodigo());
      }
    };
    this.__insertionAdapterOfTrenEntity = new EntityInsertionAdapter<TrenEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `trenes_cache` (`id`,`nombre`,`centroId`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TrenEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getNombre());
        statement.bindLong(3, entity.getCentroId());
      }
    };
    this.__insertionAdapterOfLineaEntity = new EntityInsertionAdapter<LineaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `lineas_cache` (`id`,`nombre`,`trenId`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LineaEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getNombre());
        statement.bindLong(3, entity.getTrenId());
      }
    };
  }

  @Override
  public Object insertarCentros(final List<CentroEntity> centros,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCentroEntity.insert(centros);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertarTrenes(final List<TrenEntity> trenes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTrenEntity.insert(trenes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertarLineas(final List<LineaEntity> lineas,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLineaEntity.insert(lineas);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CentroEntity>> observarCentros() {
    final String _sql = "SELECT * FROM centros_cache ORDER BY nombre";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"centros_cache"}, new Callable<List<CentroEntity>>() {
      @Override
      @NonNull
      public List<CentroEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfCodigo = CursorUtil.getColumnIndexOrThrow(_cursor, "codigo");
          final List<CentroEntity> _result = new ArrayList<CentroEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CentroEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpNombre;
            _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            final String _tmpCodigo;
            _tmpCodigo = _cursor.getString(_cursorIndexOfCodigo);
            _item = new CentroEntity(_tmpId,_tmpNombre,_tmpCodigo);
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
  public Flow<List<TrenEntity>> observarTrenes(final int centroId) {
    final String _sql = "SELECT * FROM trenes_cache WHERE centroId = ? ORDER BY nombre";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, centroId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"trenes_cache"}, new Callable<List<TrenEntity>>() {
      @Override
      @NonNull
      public List<TrenEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfCentroId = CursorUtil.getColumnIndexOrThrow(_cursor, "centroId");
          final List<TrenEntity> _result = new ArrayList<TrenEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TrenEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpNombre;
            _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            final int _tmpCentroId;
            _tmpCentroId = _cursor.getInt(_cursorIndexOfCentroId);
            _item = new TrenEntity(_tmpId,_tmpNombre,_tmpCentroId);
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
  public Flow<List<LineaEntity>> observarLineas(final int trenId) {
    final String _sql = "SELECT * FROM lineas_cache WHERE trenId = ? ORDER BY nombre";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, trenId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"lineas_cache"}, new Callable<List<LineaEntity>>() {
      @Override
      @NonNull
      public List<LineaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfTrenId = CursorUtil.getColumnIndexOrThrow(_cursor, "trenId");
          final List<LineaEntity> _result = new ArrayList<LineaEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LineaEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpNombre;
            _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            final int _tmpTrenId;
            _tmpTrenId = _cursor.getInt(_cursorIndexOfTrenId);
            _item = new LineaEntity(_tmpId,_tmpNombre,_tmpTrenId);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
