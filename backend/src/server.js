require('dotenv').config();
const express = require('express');
const cors = require('cors');
const path = require('path');
const sequelize = require('./config/database');

require('./models/Usuario');
require('./models/Catalogos');
require('./models/Muestra');

const authRoutes = require('./routes/auth.routes');
const usuariosRoutes = require('./routes/usuarios.routes');
const catalogosRoutes = require('./routes/catalogos.routes');
const muestrasRoutes = require('./routes/muestras.routes');
const reportesRoutes = require('./routes/reportes.routes');

const app = express();
const PORT = Number(process.env.PORT) || 4000;

app.use(cors());
app.use(express.json());
app.use('/uploads', express.static(path.join(__dirname, '..', 'uploads')));

app.get('/api/health', (req, res) => {
  res.json({ ok: true, servicio: 'TrazaMar API', hora: new Date().toISOString() });
});

app.use('/api/auth', authRoutes);
app.use('/api/usuarios', usuariosRoutes);
app.use('/api/catalogos', catalogosRoutes);
app.use('/api/muestras', muestrasRoutes);
app.use('/api/reportes', reportesRoutes);

app.use((req, res) => {
  res.status(404).json({ error: 'Ruta no encontrada.' });
});

app.use((err, req, res, next) => {
  console.error(err);
  const status = err.name === 'MulterError' || err.message?.includes('Formato de imagen') ? 400 : 500;
  res.status(status).json({
    error: status === 400 ? err.message : 'Error interno del servidor.',
    ...(process.env.NODE_ENV !== 'production' ? { detalle: err.message } : {}),
  });
});

async function iniciar() {
  try {
    await sequelize.authenticate();
    await sequelize.sync();
    app.listen(PORT, () => {
      console.log(`TrazaMar API escuchando en http://localhost:${PORT}`);
    });
  } catch (err) {
    console.error('No se pudo iniciar TrazaMar API:', err);
    process.exit(1);
  }
}

if (require.main === module) iniciar();

module.exports = app;
