import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Login.css';

export default function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [cargando, setCargando] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  async function onSubmit(e) {
    e.preventDefault();
    setError('');
    setCargando(true);
    try {
      await login(username, password);
      navigate('/');
    } catch (err) {
      setError(err.message);
    } finally {
      setCargando(false);
    }
  }

  return (
    <div className="login-screen">
      <aside className="login-hero">
        <svg className="login-hero__lines" viewBox="0 0 400 600" preserveAspectRatio="none">
          {[0, 1, 2, 3, 4, 5, 6].map((i) => (
            <path
              key={i}
              d={`M -20 ${70 + i * 80} Q 100 ${20 + i * 80}, 200 ${70 + i * 80} T 420 ${70 + i * 80}`}
              stroke={i % 2 === 0 ? 'rgba(242,247,247,0.28)' : 'rgba(47,168,140,0.35)'}
              strokeWidth="2"
              fill="none"
            />
          ))}
        </svg>
        <div className="login-hero__content">
          <span className="login-hero__mark">TrazaMar</span>
          <h1>Trazabilidad de muestreo, línea por línea.</h1>
          <p>
            Registro en terreno, revisión técnica y análisis histórico de muestras de choritos
            para ALDEMAR SpA — MVP académico DSY1105.
          </p>
        </div>
      </aside>

      <main className="login-form-side">
        <form className="login-card" onSubmit={onSubmit}>
          <h2>Ingresar al panel</h2>
          <p className="login-card__sub">Acceso para analistas, supervisores y administradores.</p>

          <div className="field">
            <label htmlFor="username">Usuario</label>
            <input id="username" value={username} onChange={(e) => setUsername(e.target.value)} placeholder="analista1" autoFocus />
          </div>
          <div className="field">
            <label htmlFor="password">Contraseña</label>
            <input id="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="••••••••" />
          </div>

          {error && <div className="login-error">{error}</div>}

          <button className="btn btn-primary login-submit" type="submit" disabled={cargando}>
            {cargando ? 'Ingresando…' : 'Ingresar'}
          </button>

          <p className="login-hint">
            El registro y la revisión en terreno se realizan desde la app móvil TrazaMar.
          </p>
        </form>
      </main>
    </div>
  );
}
