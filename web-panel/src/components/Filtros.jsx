import { useEffect, useState } from 'react';
import { api } from '../api/client';
import './Filtros.css';

const ESTADOS = [
  { value: '', label: 'Todos los estados' },
  { value: 'pendiente', label: 'Pendiente' },
  { value: 'observada', label: 'Observada' },
  { value: 'corregida', label: 'Corregida' },
  { value: 'validada', label: 'Validada' },
];

export default function Filtros({ valores, onChange }) {
  const [centros, setCentros] = useState([]);

  useEffect(() => {
    api.centros().then(setCentros).catch(() => setCentros([]));
  }, []);

  const centroSel = centros.find((c) => String(c.id) === String(valores.centroId));
  const trenSel = centroSel?.trenes?.find((t) => String(t.id) === String(valores.trenId));

  function set(campo, valor) {
    const siguiente = { ...valores, [campo]: valor };
    if (campo === 'centroId') { siguiente.trenId = ''; siguiente.lineaId = ''; }
    if (campo === 'trenId') { siguiente.lineaId = ''; }
    onChange(siguiente);
  }

  return (
    <div className="filtros panel">
      <div className="field">
        <label>Centro</label>
        <select value={valores.centroId || ''} onChange={(e) => set('centroId', e.target.value)}>
          <option value="">Todos los centros</option>
          {centros.map((c) => (
            <option key={c.id} value={c.id}>{c.nombre}</option>
          ))}
        </select>
      </div>

      <div className="field">
        <label>Tren</label>
        <select value={valores.trenId || ''} onChange={(e) => set('trenId', e.target.value)} disabled={!centroSel}>
          <option value="">Todos los trenes</option>
          {(centroSel?.trenes || []).map((t) => (
            <option key={t.id} value={t.id}>{t.nombre}</option>
          ))}
        </select>
      </div>

      <div className="field">
        <label>Línea</label>
        <select value={valores.lineaId || ''} onChange={(e) => set('lineaId', e.target.value)} disabled={!trenSel}>
          <option value="">Todas las líneas</option>
          {(trenSel?.lineas || []).map((l) => (
            <option key={l.id} value={l.id}>{l.nombre}</option>
          ))}
        </select>
      </div>

      <div className="field">
        <label>Estado</label>
        <select value={valores.estado || ''} onChange={(e) => set('estado', e.target.value)}>
          {ESTADOS.map((e) => <option key={e.value} value={e.value}>{e.label}</option>)}
        </select>
      </div>

      <div className="field">
        <label>Desde</label>
        <input type="date" value={valores.fechaDesde || ''} onChange={(e) => set('fechaDesde', e.target.value)} />
      </div>

      <div className="field">
        <label>Hasta</label>
        <input type="date" value={valores.fechaHasta || ''} onChange={(e) => set('fechaHasta', e.target.value)} />
      </div>

      <button
        type="button"
        className="btn btn-ghost filtros__limpiar"
        onClick={() => onChange({ centroId: '', trenId: '', lineaId: '', estado: '', fechaDesde: '', fechaHasta: '' })}
      >
        Limpiar
      </button>
    </div>
  );
}
