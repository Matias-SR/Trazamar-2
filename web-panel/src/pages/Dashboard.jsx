import { useEffect, useState } from 'react';
import {
  ResponsiveContainer, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip,
  BarChart, Bar, PieChart, Pie, Cell, Legend,
} from 'recharts';
import { api } from '../api/client';
import Filtros from '../components/Filtros';
import './Dashboard.css';

const COLOR_ESTADO = {
  borrador: '#9fb2b2',
  pendiente: '#e8a33d',
  observada: '#e8a33d',
  corregida: '#0d6e8c',
  validada: '#2fa88c',
};

const ESTADO_LABEL = {
  borrador: 'Borrador', pendiente: 'Pendiente', observada: 'Observada',
  corregida: 'Corregida', validada: 'Validada',
};

export default function Dashboard() {
  const [filtros, setFiltros] = useState({ centroId: '', trenId: '', lineaId: '', estado: '', fechaDesde: '', fechaHasta: '' });
  const [resumen, setResumen] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setCargando(true);
    const params = Object.fromEntries(Object.entries(filtros).filter(([, v]) => v));
    api.resumen(params)
      .then(setResumen)
      .catch((e) => setError(e.message))
      .finally(() => setCargando(false));
  }, [filtros]);

  return (
    <div>
      <header className="page-header">
        <h1>Panorama de muestreo</h1>
        <p>Trazabilidad agregada de las muestras registradas por los operadores en terreno.</p>
      </header>

      <Filtros valores={filtros} onChange={setFiltros} />

      {error && <div className="panel dashboard-error">{error}</div>}

      {resumen && (
        <>
          <div className="kpi-row">
            <div className="panel kpi-card">
              <span className="kpi-card__label">Muestras registradas</span>
              <span className="kpi-card__value">{resumen.total}</span>
            </div>
            <div className="panel kpi-card">
              <span className="kpi-card__label">Promedio de conteo por muestra</span>
              <span className="kpi-card__value">{resumen.promedioConteo ?? '—'}</span>
            </div>
            <div className="panel kpi-card">
              <span className="kpi-card__label">Centros con actividad</span>
              <span className="kpi-card__value">{resumen.porCentro.length}</span>
            </div>
          </div>

          <div className="chart-grid">
            <div className="panel chart-card chart-card--wide">
              <h3>Individuos contados por fecha</h3>
              <ResponsiveContainer width="100%" height={260}>
                <LineChart data={resumen.serieTemporal}>
                  <CartesianGrid stroke="#e4eeee" vertical={false} />
                  <XAxis dataKey="fecha" tick={{ fontSize: 12 }} stroke="#55686c" />
                  <YAxis tick={{ fontSize: 12 }} stroke="#55686c" />
                  <Tooltip />
                  <Line type="monotone" dataKey="totalIndividuos" name="Individuos" stroke="#0d6e8c" strokeWidth={2.5} dot={{ r: 3 }} />
                </LineChart>
              </ResponsiveContainer>
            </div>

            <div className="panel chart-card">
              <h3>Estado de revisión</h3>
              <ResponsiveContainer width="100%" height={240}>
                <PieChart>
                  <Pie data={resumen.porEstado} dataKey="total" nameKey="estado" innerRadius={50} outerRadius={80} paddingAngle={3}>
                    {resumen.porEstado.map((entry) => (
                      <Cell key={entry.estado} fill={COLOR_ESTADO[entry.estado] || '#ccc'} />
                    ))}
                  </Pie>
                  <Legend formatter={(value) => ESTADO_LABEL[value] || value} />
                  <Tooltip formatter={(value, name) => [value, ESTADO_LABEL[name] || name]} />
                </PieChart>
              </ResponsiveContainer>
            </div>

            <div className="panel chart-card">
              <h3>Muestras por centro</h3>
              <ResponsiveContainer width="100%" height={240}>
                <BarChart data={resumen.porCentro} layout="vertical" margin={{ left: 24 }}>
                  <CartesianGrid stroke="#e4eeee" horizontal={false} />
                  <XAxis type="number" tick={{ fontSize: 12 }} stroke="#55686c" />
                  <YAxis type="category" dataKey="centro" width={140} tick={{ fontSize: 11 }} stroke="#55686c" />
                  <Tooltip />
                  <Bar dataKey="total" fill="#2fa88c" radius={[0, 6, 6, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>
        </>
      )}

      {cargando && !resumen && <p className="dashboard-loading">Cargando panorama…</p>}
    </div>
  );
}
