import { useEffect, useState } from 'react';
import { api, API_URL } from '../api/client';
import Filtros from '../components/Filtros';
import './Historial.css';

const ESTADO_LABEL = {
  borrador: 'Borrador', pendiente: 'Pendiente', observada: 'Observada',
  corregida: 'Corregida', validada: 'Validada',
};

export default function Historial() {
  const [filtros, setFiltros] = useState({ centroId: '', trenId: '', lineaId: '', estado: '', fechaDesde: '', fechaHasta: '' });
  const [muestras, setMuestras] = useState([]);
  const [seleccion, setSeleccion] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const [exportando, setExportando] = useState(false);

  const params = Object.fromEntries(Object.entries(filtros).filter(([, v]) => v));

  useEffect(() => {
    setCargando(true);
    api.muestras(params)
      .then(setMuestras)
      .catch((e) => setError(e.message))
      .finally(() => setCargando(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [JSON.stringify(filtros)]);

  async function exportar() {
    setExportando(true);
    try {
      const blob = await api.exportarCsv(params);
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `trazamar_historial_${Date.now()}.csv`;
      a.click();
      URL.revokeObjectURL(url);
    } catch (e) {
      setError(e.message);
    } finally {
      setExportando(false);
    }
  }

  return (
    <div>
      <header className="page-header historial-header">
        <div>
          <h1>Historial de muestras</h1>
          <p>Consulta, filtra y exporta los registros de muestreo trazados en terreno.</p>
        </div>
        <button className="btn btn-secondary" onClick={exportar} disabled={exportando || muestras.length === 0}>
          {exportando ? 'Exportando…' : 'Exportar CSV'}
        </button>
      </header>

      <Filtros valores={filtros} onChange={setFiltros} />

      {error && <div className="panel dashboard-error">{error}</div>}

      <div className="panel tabla-wrap">
        <table className="tabla-muestras">
          <thead>
            <tr>
              <th>Fecha</th>
              <th>Centro</th>
              <th>Tren / Línea</th>
              <th>Operador</th>
              <th>Conteo</th>
              <th>Estado</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {muestras.map((m) => (
              <tr key={m.id} onClick={() => setSeleccion(m)} className="tabla-muestras__fila">
                <td>{m.fecha}{m.hora ? ` · ${m.hora}` : ''}</td>
                <td>{m.centro?.nombre}</td>
                <td>{m.tren?.nombre} / {m.linea?.nombre}</td>
                <td>{m.operador?.nombreCompleto}</td>
                <td>{m.conteoIndividuos ?? '—'}</td>
                <td><span className={`badge badge-${m.estado}`}>{ESTADO_LABEL[m.estado]}</span></td>
                <td className="tabla-muestras__ver">Ver detalle</td>
              </tr>
            ))}
            {!cargando && muestras.length === 0 && (
              <tr><td colSpan={7} className="tabla-muestras__vacio">No hay muestras para los filtros seleccionados.</td></tr>
            )}
          </tbody>
        </table>
      </div>

      {seleccion && <DetalleMuestra muestra={seleccion} onClose={() => setSeleccion(null)} />}
    </div>
  );
}

function DetalleMuestra({ muestra, onClose }) {
  const fotoSrc = muestra.fotoUrl ? `${API_URL.replace('/api', '')}${muestra.fotoUrl}` : null;
  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="panel modal-card" onClick={(e) => e.stopPropagation()}>
        <div className="modal-card__header">
          <h3>Muestra #{muestra.id}</h3>
          <button className="btn btn-ghost" onClick={onClose}>Cerrar</button>
        </div>

        <div className="modal-card__body">
          {fotoSrc && <img src={fotoSrc} alt="Evidencia de la muestra" className="modal-card__foto" />}

          <dl className="detalle-grid">
            <dt>Centro</dt><dd>{muestra.centro?.nombre}</dd>
            <dt>Tren / Línea</dt><dd>{muestra.tren?.nombre} / {muestra.linea?.nombre}</dd>
            <dt>Fecha / Hora</dt><dd>{muestra.fecha} {muestra.hora}</dd>
            <dt>Tramo muestreado</dt><dd>{muestra.tramoMetros ? `${muestra.tramoMetros} m` : '—'}</dd>
            <dt>Operador</dt><dd>{muestra.operador?.nombreCompleto}</dd>
            <dt>Conteo</dt><dd>{muestra.conteoIndividuos ?? '—'} individuos</dd>
            <dt>Calibre (prom/min/max)</dt>
            <dd>{muestra.calibrePromedio ?? '—'} / {muestra.calibreMin ?? '—'} / {muestra.calibreMax ?? '—'}</dd>
            <dt>Peso de muestra</dt><dd>{muestra.pesoMuestra ? `${muestra.pesoMuestra} kg` : '—'}</dd>
            <dt>Observaciones</dt><dd>{muestra.observaciones || '—'}</dd>
            <dt>Estado</dt><dd><span className={`badge badge-${muestra.estado}`}>{ESTADO_LABEL[muestra.estado]}</span></dd>
            <dt>Supervisor</dt><dd>{muestra.supervisor?.nombreCompleto || '—'}</dd>
            <dt>Comentario del supervisor</dt><dd>{muestra.comentarioSupervisor || '—'}</dd>
          </dl>
        </div>
      </div>
    </div>
  );
}
