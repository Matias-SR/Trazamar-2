const express = require('express');
const { Op, fn, col, literal } = require('sequelize');
const { Parser } = require('json2csv');
const Muestra = require('../models/Muestra');
const Usuario = require('../models/Usuario');
const { Centro, Tren, Linea } = require('../models/Catalogos');
const { autenticar, autorizar } = require('../middleware/auth');

const router = express.Router();
router.use(autenticar, autorizar('analista', 'admin', 'supervisor'));

function construirWhere(query) {
  const { centroId, trenId, lineaId, estado, fechaDesde, fechaHasta } = query;
  const where = {};
  if (centroId) where.centroId = centroId;
  if (trenId) where.trenId = trenId;
  if (lineaId) where.lineaId = lineaId;
  if (estado) where.estado = estado;
  if (fechaDesde || fechaHasta) {
    where.fecha = {};
    if (fechaDesde) where.fecha[Op.gte] = fechaDesde;
    if (fechaHasta) where.fecha[Op.lte] = fechaHasta;
  }
  return where;
}

// GET /api/reportes/resumen -> KPIs y series para gráficos del panel del analista
router.get('/resumen', async (req, res) => {
  const where = construirWhere(req.query);

  const [total, porEstado, porCentro, conteoPorFecha] = await Promise.all([
    Muestra.count({ where }),
    Muestra.findAll({ where, attributes: ['estado', [fn('COUNT', col('id')), 'total']], group: ['estado'] }),
    Muestra.findAll({
      where,
      attributes: [[col('centro.nombre'), 'centro'], [fn('COUNT', col('Muestra.id')), 'total']],
      include: [{ model: Centro, as: 'centro', attributes: [] }],
      group: ['centro.nombre'],
      raw: true,
    }),
    Muestra.findAll({
      where,
      attributes: ['fecha', [fn('SUM', col('conteoIndividuos')), 'totalIndividuos'], [fn('COUNT', col('id')), 'totalMuestras']],
      group: ['fecha'],
      order: [['fecha', 'ASC']],
    }),
  ]);

  const promedioConteo = await Muestra.findOne({
    where,
    attributes: [[fn('AVG', col('conteoIndividuos')), 'promedio']],
    raw: true,
  });

  res.json({
    total,
    promedioConteo: promedioConteo?.promedio ? Number(promedioConteo.promedio).toFixed(1) : null,
    porEstado: porEstado.map((r) => ({ estado: r.estado, total: Number(r.get('total')) })),
    porCentro: porCentro.map((r) => ({ centro: r.centro, total: Number(r.total) })),
    serieTemporal: conteoPorFecha.map((r) => ({
      fecha: r.fecha,
      totalIndividuos: Number(r.get('totalIndividuos')) || 0,
      totalMuestras: Number(r.get('totalMuestras')),
    })),
  });
});

// GET /api/reportes/exportar -> descarga CSV de muestras filtradas
router.get('/exportar', async (req, res) => {
  const where = construirWhere(req.query);

  const muestras = await Muestra.findAll({
    where,
    include: [
      { model: Centro, as: 'centro' },
      { model: Tren, as: 'tren' },
      { model: Linea, as: 'linea' },
      { model: Usuario, as: 'operador', attributes: ['nombreCompleto'] },
      { model: Usuario, as: 'supervisor', attributes: ['nombreCompleto'] },
    ],
    order: [['fecha', 'DESC']],
  });

  const filas = muestras.map((m) => ({
    id: m.id,
    fecha: m.fecha,
    hora: m.hora,
    centro: m.centro?.nombre,
    tren: m.tren?.nombre,
    linea: m.linea?.nombre,
    tramoMetros: m.tramoMetros,
    operador: m.operador?.nombreCompleto,
    conteoIndividuos: m.conteoIndividuos,
    calibrePromedio: m.calibrePromedio,
    calibreMin: m.calibreMin,
    calibreMax: m.calibreMax,
    pesoMuestra: m.pesoMuestra,
    estado: m.estado,
    supervisor: m.supervisor?.nombreCompleto,
    comentarioSupervisor: m.comentarioSupervisor,
    observaciones: m.observaciones,
  }));

  const parser = new Parser();
  const csv = parser.parse(filas);

  res.header('Content-Type', 'text/csv');
  res.attachment(`trazamar_reporte_${Date.now()}.csv`);
  res.send(csv);
});

module.exports = router;
