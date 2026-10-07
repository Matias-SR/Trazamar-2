const express = require('express');
const { Op } = require('sequelize');
const Muestra = require('../models/Muestra');
const Usuario = require('../models/Usuario');
const { Centro, Tren, Linea } = require('../models/Catalogos');
const { autenticar, autorizar } = require('../middleware/auth');
const upload = require('../config/upload');

const router = express.Router();
router.use(autenticar);

const includeCompleto = [
  { model: Centro, as: 'centro' },
  { model: Tren, as: 'tren' },
  { model: Linea, as: 'linea' },
  { model: Usuario, as: 'operador', attributes: ['id', 'nombreCompleto', 'username'] },
  { model: Usuario, as: 'supervisor', attributes: ['id', 'nombreCompleto', 'username'] },
];

// -------- Validación de campos obligatorios (RF05) --------
function validarMuestra(body, { paraGuardarFinal }) {
  const errores = [];
  if (!body.centroId) errores.push('centroId es obligatorio.');
  if (!body.trenId) errores.push('trenId es obligatorio.');
  if (!body.lineaId) errores.push('lineaId es obligatorio.');
  if (!body.fecha) errores.push('fecha es obligatoria.');
  if (!body.operadorId) errores.push('operadorId es obligatorio.');

  if (body.hora && !/^([01]\\d|2[0-3]):[0-5]\\d$/.test(body.hora)) {
    errores.push('hora debe tener formato HH:mm.');
  }
  if (body.conteoIndividuos != null && (!Number.isInteger(Number(body.conteoIndividuos)) || Number(body.conteoIndividuos) < 0)) {
    errores.push('conteoIndividuos debe ser un entero mayor o igual a 0.');
  }
  if (paraGuardarFinal) {
    if (body.conteoIndividuos === undefined || body.conteoIndividuos === null)
      errores.push('conteoIndividuos es obligatorio para cerrar el registro.');
  }
  return errores;
}

// GET /api/muestras -> historial con filtros (RF07, RF08)
// Filtros: centroId, trenId, lineaId, estado, operadorId, fechaDesde, fechaHasta, q (texto libre en observaciones)
router.get('/', async (req, res) => {
  const { centroId, trenId, lineaId, estado, operadorId, fechaDesde, fechaHasta, q } = req.query;
  const where = {};
  if (centroId) where.centroId = centroId;
  if (trenId) where.trenId = trenId;
  if (lineaId) where.lineaId = lineaId;
  if (estado) where.estado = estado;
  if (operadorId) where.operadorId = operadorId;
  if (fechaDesde || fechaHasta) {
    where.fecha = {};
    if (fechaDesde) where.fecha[Op.gte] = fechaDesde;
    if (fechaHasta) where.fecha[Op.lte] = fechaHasta;
  }
  if (q) where.observaciones = { [Op.like]: `%${q}%` };

  // El rol "cliente/revisor" y "analista" solo ven registros validados/cerrados según el caso;
  // operador y supervisor ven todo lo relevante a su flujo.
  if (req.usuario.rol === 'analista') {
    // El analista puede ver todo el historial para elaborar reportes, no solo validadas,
    // pero no puede modificar (se controla en las rutas de escritura).
  }

  if (req.usuario.rol === 'operador') where.operadorId = req.usuario.id;

  const muestras = await Muestra.findAll({ where, include: includeCompleto, order: [['fecha', 'DESC'], ['id', 'DESC']] });
  res.json(muestras);
});

// GET /api/muestras/:id
router.get('/:id', async (req, res) => {
  const muestra = await Muestra.findByPk(req.params.id, { include: includeCompleto });
  if (!muestra) return res.status(404).json({ error: 'Muestra no encontrada.' });
  if (req.usuario.rol === 'operador' && muestra.operadorId !== req.usuario.id) {
    return res.status(403).json({ error: 'No puede consultar una muestra de otro operador.' });
  }
  res.json(muestra);
});

// POST /api/muestras -> crear nueva muestra (RF02) - Operador
router.post('/', autorizar('operador', 'admin'), async (req, res) => {
  const datos = { ...req.body };
  if (req.usuario.rol === 'operador') datos.operadorId = req.usuario.id;

  const errores = validarMuestra(datos, { paraGuardarFinal: false });
  if (errores.length) return res.status(400).json({ errores });

  try {
    const muestra = await Muestra.create({
      ...datos,
      estado: 'borrador',
    });
    res.status(201).json(muestra);
  } catch (err) {
    res.status(500).json({ error: 'Error al crear la muestra.', detalle: err.message });
  }
});

// PUT /api/muestras/:id -> editar registro (conteo, observaciones, calibre, etc.) mientras esté en borrador/observada
router.put('/:id', autorizar('operador', 'admin'), async (req, res) => {
  const muestra = await Muestra.findByPk(req.params.id);
  if (!muestra) return res.status(404).json({ error: 'Muestra no encontrada.' });
  if (req.usuario.rol === 'operador' && muestra.operadorId !== req.usuario.id) {
    return res.status(403).json({ error: 'No puede modificar una muestra de otro operador.' });
  }

  if (!['borrador', 'observada'].includes(muestra.estado) && req.usuario.rol !== 'admin') {
    return res.status(409).json({ error: `No se puede editar una muestra en estado "${muestra.estado}".` });
  }

  const camposEditables = [
    'centroId', 'trenId', 'lineaId', 'fecha', 'hora', 'tramoMetros', 'operadorId',
    'conteoIndividuos', 'observaciones', 'calibrePromedio', 'calibreMin', 'calibreMax', 'pesoMuestra',
  ];
  camposEditables.forEach((campo) => {
    if (req.body[campo] !== undefined) muestra[campo] = req.body[campo];
  });

  // Si el operador corrige una muestra observada, vuelve a quedar pendiente de revisión
  if (muestra.estado === 'observada') muestra.estado = 'corregida';

  await muestra.save();
  res.json(muestra);
});

// POST /api/muestras/:id/enviar -> pasa de borrador a pendiente de revisión (aplica validación completa RF05)
router.post('/:id/enviar', autorizar('operador', 'admin'), async (req, res) => {
  const muestra = await Muestra.findByPk(req.params.id);
  if (!muestra) return res.status(404).json({ error: 'Muestra no encontrada.' });
  if (req.usuario.rol === 'operador' && muestra.operadorId !== req.usuario.id) {
    return res.status(403).json({ error: 'No puede enviar una muestra de otro operador.' });
  }

  const errores = validarMuestra(muestra.toJSON(), { paraGuardarFinal: true });
  if (!muestra.fotoUrl) errores.push('Debe adjuntar una fotografía antes de enviar a revisión.');
  if (errores.length) return res.status(400).json({ errores });

  muestra.estado = 'pendiente';
  await muestra.save();
  res.json(muestra);
});

// POST /api/muestras/:id/foto -> adjuntar/capturar fotografía (RF04)
router.post('/:id/foto', autorizar('operador', 'admin'), upload.single('foto'), async (req, res) => {
  const muestra = await Muestra.findByPk(req.params.id);
  if (!muestra) return res.status(404).json({ error: 'Muestra no encontrada.' });
  if (req.usuario.rol === 'operador' && muestra.operadorId !== req.usuario.id) {
    return res.status(403).json({ error: 'No puede adjuntar una foto a una muestra de otro operador.' });
  }
  if (!req.file) return res.status(400).json({ error: 'No se recibió ningún archivo de imagen.' });

  muestra.fotoUrl = `/uploads/${req.file.filename}`;
  await muestra.save();
  res.json({ fotoUrl: muestra.fotoUrl });
});

// PATCH /api/muestras/:id/revision -> Supervisor valida, observa o solicita corrección (RF09)
router.patch('/:id/revision', autorizar('supervisor', 'admin'), async (req, res) => {
  const { estado, comentarioSupervisor } = req.body;
  const estadosValidos = ['validada', 'observada', 'corregida', 'pendiente'];
  if (!estadosValidos.includes(estado)) {
    return res.status(400).json({ error: `Estado inválido. Use uno de: ${estadosValidos.join(', ')}` });
  }

  const muestra = await Muestra.findByPk(req.params.id);
  if (!muestra) return res.status(404).json({ error: 'Muestra no encontrada.' });

  muestra.estado = estado;
  muestra.comentarioSupervisor = comentarioSupervisor || muestra.comentarioSupervisor;
  muestra.supervisorId = req.usuario.id;
  muestra.fechaRevision = new Date();
  await muestra.save();

  res.json(muestra);
});

// DELETE /api/muestras/:id -> solo admin, para limpieza de datos de prueba
router.delete('/:id', autorizar('admin'), async (req, res) => {
  const muestra = await Muestra.findByPk(req.params.id);
  if (!muestra) return res.status(404).json({ error: 'Muestra no encontrada.' });
  await muestra.destroy();
  res.json({ mensaje: 'Muestra eliminada.' });
});

module.exports = router;
