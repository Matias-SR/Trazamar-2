import { createContext, useContext, useEffect, useState } from 'react';
import { api } from '../api/client';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(null);
  const [cargando, setCargando] = useState(true);

  useEffect(() => {
    async function restaurarSesion() {
      const guardado = localStorage.getItem('trazamar_usuario');
      const token = localStorage.getItem('trazamar_token');
      if (!guardado || !token) {
        setCargando(false);
        return;
      }

      try {
        const data = await api.me();
        setUsuario(data.usuario);
      } catch {
        localStorage.removeItem('trazamar_token');
        localStorage.removeItem('trazamar_usuario');
      } finally {
        setCargando(false);
      }
    }

    restaurarSesion();
  }, []);

  async function login(username, password) {
    const data = await api.login(username, password);
    if (!['analista', 'admin', 'supervisor'].includes(data.usuario.rol)) {
      throw new Error('Este panel web es exclusivo para analistas, supervisores y administradores. Usa la app móvil con tu rol.');
    }
    localStorage.setItem('trazamar_token', data.token);
    localStorage.setItem('trazamar_usuario', JSON.stringify(data.usuario));
    setUsuario(data.usuario);
  }

  function logout() {
    localStorage.removeItem('trazamar_token');
    localStorage.removeItem('trazamar_usuario');
    setUsuario(null);
  }

  return (
    <AuthContext.Provider value={{ usuario, login, logout, cargando }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
