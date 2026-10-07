const express = require('express');
const { Centro, Tren, Linea } = require('../models/Catalogos');
const { autenticar, autorizar } = require('../middleware/auth');

const router = express.Router();

// Lectura de catálogos: cualquier usuario autenticado los necesita (para formularios)
router.get('/centros', autenticar, async (req, res) => {
  const centros = await Centro.findAll({
    where: { activo: true },
    include: [{ association: 'trenes', where: { activo: true }, required: false,
      include: [{ association: 'lineas', where: { activo: true }, required: false }] }],
    order: [['nombre', 'ASC']],
  });
  res.json(centros);
});

router.get('/trenes', autenticar, async (req, res) => {
  const where = { activo: true };
  if (req.query.centroId) where.centroId = req.query.centroId;
  const trenes = await Tren.findAll({ where, order: [['nombre', 'ASC']] });
  res.json(trenes);
});

router.get('/lineas', autenticar, async (req, res) => {
  const where = { activo: true };
  if (req.query.trenId) where.trenId = req.query.trenId;
  const lineas = await Linea.findAll({ where, order: [['nombre', 'ASC']] });
  res.json(lineas);
});

// Escritura de catálogos: solo administrador (RF de administración académica)
router.use(autenticar, autorizar('admin'));

router.post('/centros', async (req, res) => {
  const { nombre, codigo } = req.body;
  if (!nombre || !codigo) return res.status(400).json({ error: 'nombre y codigo son obligatorios.' });
  const centro = await Centro.create({ nombre, codigo });
  res.status(201).json(centro);
});

router.post('/trenes', async (req, res) => {
  const { nombre, centroId } = req.body;
  if (!nombre || !centroId) return res.status(400).json({ error: 'nombre y centroId son obligatorios.' });
  const tren = await Tren.create({ nombre, centroId });
  res.status(201).json(tren);
});

router.post('/lineas', async (req, res) => {
  const { nombre, trenId } = req.body;
  if (!nombre || !trenId) return res.status(400).json({ error: 'nombre y trenId son obligatorios.' });
  const linea = await Linea.create({ nombre, trenId });
  res.status(201).json(linea);
});

router.delete('/centros/:id', async (req, res) => {
  const c = await Centro.findByPk(req.params.id);
  if (!c) return res.status(404).json({ error: 'No encontrado.' });
  c.activo = false; await c.save();
  res.json({ mensaje: 'Centro desactivado.' });
});

router.delete('/trenes/:id', async (req, res) => {
  const t = await Tren.findByPk(req.params.id);
  if (!t) return res.status(404).json({ error: 'No encontrado.' });
  t.activo = false; await t.save();
  res.json({ mensaje: 'Tren desactivado.' });
});

router.delete('/lineas/:id', async (req, res) => {
  const l = await Linea.findByPk(req.params.id);
  if (!l) return res.status(404).json({ error: 'No encontrado.' });
  l.activo = false; await l.save();
  res.json({ mensaje: 'Línea desactivada.' });
});

module.exports = router;
