const express = require('express');
const bcrypt = require('bcryptjs');
const Usuario = require('../models/Usuario');
const { autenticar, autorizar } = require('../middleware/auth');

const router = express.Router();

// Todas las rutas de este módulo requieren estar autenticado como admin
router.use(autenticar, autorizar('admin'));

// GET /api/usuarios  -> listar todos los usuarios
router.get('/', async (req, res) => {
  const usuarios = await Usuario.findAll({
    attributes: ['id', 'username', 'nombreCompleto', 'rol', 'activo', 'createdAt'],
    order: [['createdAt', 'DESC']],
  });
  res.json(usuarios);
});

// POST /api/usuarios  -> crear cuenta con rol
router.post('/', async (req, res) => {
  try {
    const { username, password, nombreCompleto, rol } = req.body;
    const rolesValidos = ['admin', 'operador', 'supervisor', 'analista'];

    if (!username || !password || !nombreCompleto || !rol) {
      return res.status(400).json({ error: 'Todos los campos son obligatorios.' });
    }
    if (!rolesValidos.includes(rol)) {
      return res.status(400).json({ error: `Rol inválido. Use uno de: ${rolesValidos.join(', ')}` });
    }

    const existente = await Usuario.findOne({ where: { username } });
    if (existente) {
      return res.status(409).json({ error: 'El nombre de usuario ya existe.' });
    }

    const passwordHash = await bcrypt.hash(password, 10);
    const usuario = await Usuario.create({ username, passwordHash, nombreCompleto, rol });

    res.status(201).json({
      id: usuario.id,
      username: usuario.username,
      nombreCompleto: usuario.nombreCompleto,
      rol: usuario.rol,
      activo: usuario.activo,
    });
  } catch (err) {
    res.status(500).json({ error: 'Error al crear usuario.', detalle: err.message });
  }
});

// PUT /api/usuarios/:id  -> editar datos / rol / contraseña
router.put('/:id', async (req, res) => {
  try {
    const usuario = await Usuario.findByPk(req.params.id);
    if (!usuario) return res.status(404).json({ error: 'Usuario no encontrado.' });

    const { nombreCompleto, rol, password, activo } = req.body;
    const rolesValidos = ['admin', 'operador', 'supervisor', 'analista'];
    if (nombreCompleto) usuario.nombreCompleto = nombreCompleto;
    if (rol) {
      if (!rolesValidos.includes(rol)) {
        return res.status(400).json({ error: `Rol inválido. Use uno de: ${rolesValidos.join(', ')}` });
      }
      usuario.rol = rol;
    }
    if (typeof activo === 'boolean') usuario.activo = activo;
    if (password) usuario.passwordHash = await bcrypt.hash(password, 10);

    await usuario.save();
    res.json({
      id: usuario.id,
      username: usuario.username,
      nombreCompleto: usuario.nombreCompleto,
      rol: usuario.rol,
      activo: usuario.activo,
    });
  } catch (err) {
    res.status(500).json({ error: 'Error al actualizar usuario.', detalle: err.message });
  }
});

// DELETE /api/usuarios/:id  -> desactivar cuenta (no se elimina físicamente, por trazabilidad)
router.delete('/:id', async (req, res) => {
  const usuario = await Usuario.findByPk(req.params.id);
  if (!usuario) return res.status(404).json({ error: 'Usuario no encontrado.' });
  usuario.activo = false;
  await usuario.save();
  res.json({ mensaje: 'Usuario desactivado.' });
});

module.exports = router;
