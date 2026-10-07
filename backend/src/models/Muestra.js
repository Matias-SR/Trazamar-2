const { DataTypes, Model } = require('sequelize');
const sequelize = require('../config/database');
const Usuario = require('./Usuario');
const { Centro, Tren, Linea } = require('./Catalogos');

class Muestra extends Model {}

Muestra.init(
  {
    id: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },

    // Trazabilidad
    centroId: { type: DataTypes.INTEGER, allowNull: false },
    trenId: { type: DataTypes.INTEGER, allowNull: false },
    lineaId: { type: DataTypes.INTEGER, allowNull: false },
    fecha: { type: DataTypes.DATEONLY, allowNull: false },
    hora: { type: DataTypes.STRING, allowNull: true }, // HH:mm
    tramoMetros: { type: DataTypes.FLOAT, allowNull: true },
    operadorId: { type: DataTypes.INTEGER, allowNull: false },

    // Conteo y observaciones
    conteoIndividuos: { type: DataTypes.INTEGER, allowNull: true },
    observaciones: { type: DataTypes.TEXT, allowNull: true },

    // Opcionales (RF06)
    calibrePromedio: { type: DataTypes.FLOAT, allowNull: true },
    calibreMin: { type: DataTypes.FLOAT, allowNull: true },
    calibreMax: { type: DataTypes.FLOAT, allowNull: true },
    pesoMuestra: { type: DataTypes.FLOAT, allowNull: true },

    // Evidencia fotográfica
    fotoUrl: { type: DataTypes.STRING, allowNull: true },

    // Estado de revisión (RF09)
    estado: {
      type: DataTypes.ENUM('borrador', 'pendiente', 'observada', 'corregida', 'validada'),
      allowNull: false,
      defaultValue: 'borrador',
    },
    comentarioSupervisor: { type: DataTypes.TEXT, allowNull: true },
    supervisorId: { type: DataTypes.INTEGER, allowNull: true },
    fechaRevision: { type: DataTypes.DATE, allowNull: true },
  },
  {
    sequelize,
    modelName: 'Muestra',
    tableName: 'muestras',
    timestamps: true,
  }
);

Muestra.belongsTo(Centro, { foreignKey: 'centroId', as: 'centro' });
Muestra.belongsTo(Tren, { foreignKey: 'trenId', as: 'tren' });
Muestra.belongsTo(Linea, { foreignKey: 'lineaId', as: 'linea' });
Muestra.belongsTo(Usuario, { foreignKey: 'operadorId', as: 'operador' });
Muestra.belongsTo(Usuario, { foreignKey: 'supervisorId', as: 'supervisor' });

module.exports = Muestra;
