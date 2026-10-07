const express = require('express');
const bcrypt = require('bcryptjs');
const Usuario = require('../models/Usuario');
const { generarToken, autenticar } = require('../middleware/auth');

const router = express.Router();

// POST /api/auth/login  { username, password }
router.post('/login', async (req, res) => {
  try {
    const { username, password } = req.body;
    if (!username || !password) {
      return res.status(400).json({ error: 'Usuario y contraseña son obligatorios.' });
    }

    const usuario = await Usuario.findOne({ where: { username } });
    if (!usuario || !usuario.activo) {
      return res.status(401).json({ error: 'Credenciales inválidas o usuario inactivo.' });
    }

    const ok = await bcrypt.compare(password, usuario.passwordHash);
    if (!ok) {
      return res.status(401).json({ error: 'Credenciales inválidas.' });
    }

    const token = generarToken(usuario);
    res.json({
      token,
      usuario: {
        id: usuario.id,
        username: usuario.username,
        nombreCompleto: usuario.nombreCompleto,
        rol: usuario.rol,
      },
    });
  } catch (err) {
    res.status(500).json({ error: 'Error interno al iniciar sesión.', detalle: err.message });
  }
});

// GET /api/auth/me  -> valida token y retorna datos del usuario actual
router.get('/me', autenticar, (req, res) => {
  res.json({ usuario: req.usuario });
});

module.exports = router;
