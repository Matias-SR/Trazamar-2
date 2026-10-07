require('dotenv').config();
const bcrypt = require('bcryptjs');
const sequelize = require('../config/database');
const Usuario = require('../models/Usuario');
const { Centro, Tren, Linea } = require('../models/Catalogos');
const Muestra = require('../models/Muestra');

async function seed() {
  await sequelize.sync({ force: true }); // ⚠️ recrea las tablas: solo para datos de prueba académicos

  const passwordDemo = await bcrypt.hash('Aqua2026*', 10);

  const [admin, operador, supervisor, analista] = await Usuario.bulkCreate(
    [
      { username: 'admin', passwordHash: passwordDemo, nombreCompleto: 'Admin Académico', rol: 'admin' },
      { username: 'operador1', passwordHash: passwordDemo, nombreCompleto: 'Matias Sandoval (Operador)', rol: 'operador' },
      { username: 'supervisor1', passwordHash: passwordDemo, nombreCompleto: 'Yonathan Machado (Supervisor)', rol: 'supervisor' },
      { username: 'analista1', passwordHash: passwordDemo, nombreCompleto: 'Maximiliano Caceres (Analista)', rol: 'analista' },
    ],
    { returning: true }
  );

  const centro1 = await Centro.create({ nombre: 'Centro Ficticio Reloncaví', codigo: 'CTR-001' });
  const centro2 = await Centro.create({ nombre: 'Centro Ficticio Chiloé Norte', codigo: 'CTR-002' });

  const tren1 = await Tren.create({ nombre: 'Tren A', centroId: centro1.id });
  const tren2 = await Tren.create({ nombre: 'Tren B', centroId: centro1.id });
  const tren3 = await Tren.create({ nombre: 'Tren A', centroId: centro2.id });

  const linea1 = await Linea.create({ nombre: 'Línea 1', trenId: tren1.id });
  const linea2 = await Linea.create({ nombre: 'Línea 2', trenId: tren1.id });
  const linea3 = await Linea.create({ nombre: 'Línea 1', trenId: tren2.id });
  const linea4 = await Linea.create({ nombre: 'Línea 1', trenId: tren3.id });

  await Muestra.bulkCreate([
    {
      centroId: centro1.id, trenId: tren1.id, lineaId: linea1.id,
      fecha: '2026-09-01', hora: '09:30', tramoMetros: 1.5, operadorId: operador.id,
      conteoIndividuos: 128, observaciones: 'Muestra representativa, buena densidad.',
      calibrePromedio: 5.4, calibreMin: 4.1, calibreMax: 6.8, pesoMuestra: 2.3,
      estado: 'validada', supervisorId: supervisor.id, comentarioSupervisor: 'Datos consistentes.',
      fechaRevision: new Date('2026-09-02'),
    },
    {
      centroId: centro1.id, trenId: tren1.id, lineaId: linea2.id,
      fecha: '2026-09-02', hora: '10:15', tramoMetros: 1.0, operadorId: operador.id,
      conteoIndividuos: 95, observaciones: 'Sector con menor densidad relativa.',
      estado: 'pendiente',
    },
    {
      centroId: centro1.id, trenId: tren2.id, lineaId: linea3.id,
      fecha: '2026-09-03', hora: '08:50', tramoMetros: 1.2, operadorId: operador.id,
      conteoIndividuos: 110, observaciones: 'Falta claridad en foto, se solicitó repetir.',
      estado: 'observada', supervisorId: supervisor.id, comentarioSupervisor: 'Repetir fotografía, se ve borrosa.',
      fechaRevision: new Date('2026-09-04'),
    },
    {
      centroId: centro2.id, trenId: tren3.id, lineaId: linea4.id,
      fecha: '2026-09-04', hora: '11:05', tramoMetros: 1.5, operadorId: operador.id,
      conteoIndividuos: 140, observaciones: 'Buen calibre general.',
      calibrePromedio: 6.1, calibreMin: 5.0, calibreMax: 7.2, pesoMuestra: 2.8,
      estado: 'corregida',
    },
  ]);

  console.log('✅ Seed completado.');
  console.log('Usuarios de prueba (contraseña para todos: Aqua2026*):');
  console.log('  admin / operador1 / supervisor1 / analista1');
  process.exit(0);
}

seed().catch((err) => {
  console.error('❌ Error en seed:', err);
  process.exit(1);
});
