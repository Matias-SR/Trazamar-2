import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Layout.css';

const ROL_LABEL = {
  admin: 'Administrador',
  analista: 'Analista de reportes',
  supervisor: 'Supervisor técnico',
};

export default function Layout() {
  const { usuario, logout } = useAuth();
  const navigate = useNavigate();

  function onLogout() {
    logout();
    navigate('/login');
  }

  return (
    <div className="app-shell">
      <aside className="app-sidebar">
        <div className="app-sidebar__brand">
          <span className="app-sidebar__mark">TrazaMar</span>
          <span className="app-sidebar__caption">Panel de análisis</span>
        </div>

        <nav className="app-sidebar__nav">
          <NavLink to="/" end className={({ isActive }) => `app-nav-item ${isActive ? 'is-active' : ''}`}>
            <IconChart /> Panorama
          </NavLink>
          <NavLink to="/historial" className={({ isActive }) => `app-nav-item ${isActive ? 'is-active' : ''}`}>
            <IconList /> Historial de muestras
          </NavLink>
        </nav>

        <div className="app-sidebar__user">
          <div className="app-sidebar__avatar">{usuario?.nombreCompleto?.charAt(0)}</div>
          <div className="app-sidebar__userinfo">
            <strong>{usuario?.nombreCompleto}</strong>
            <span>{ROL_LABEL[usuario?.rol] || usuario?.rol}</span>
          </div>
          <button className="app-sidebar__logout" onClick={onLogout} title="Cerrar sesión">
            <IconExit />
          </button>
        </div>
      </aside>

      <div className="app-content">
        <Outlet />
      </div>
    </div>
  );
}

function IconChart() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
      <path d="M4 20V10M12 20V4M20 20v-7" strokeLinecap="round" />
    </svg>
  );
}
function IconList() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
      <path d="M8 6h13M8 12h13M8 18h13M3 6h.01M3 12h.01M3 18h.01" strokeLinecap="round" />
    </svg>
  );
}
function IconExit() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
      <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4M16 17l5-5-5-5M21 12H9" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}
