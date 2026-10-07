const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:4000/api';

function getToken() {
  return localStorage.getItem('trazamar_token');
}

async function request(path, { method = 'GET', body, isBlob = false } = {}) {
  const headers = { };
  if (body) headers['Content-Type'] = 'application/json';
  const token = getToken();
  if (token) headers['Authorization'] = `Bearer ${token}`;

  const res = await fetch(`${API_URL}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  });

  if (!res.ok) {
    let mensaje = `Error ${res.status}`;
    try {
      const data = await res.json();
      mensaje = data.error || data.errores?.join(', ') || mensaje;
    } catch (_) {}
    throw new Error(mensaje);
  }

  if (isBlob) return res.blob();
  if (res.status === 204) return null;
  return res.json();
}

export const api = {
  login: (username, password) => request('/auth/login', { method: 'POST', body: { username, password } }),
  me: () => request('/auth/me'),
  resumen: (params) => request(`/reportes/resumen?${new URLSearchParams(params)}`),
  exportarCsv: (params) => request(`/reportes/exportar?${new URLSearchParams(params)}`, { isBlob: true }),
  muestras: (params) => request(`/muestras?${new URLSearchParams(params)}`),
  muestra: (id) => request(`/muestras/${id}`),
  centros: () => request('/catalogos/centros'),
};

export { API_URL, getToken };
