const { DataTypes, Model } = require('sequelize');
const sequelize = require('../config/database');

class Centro extends Model {}
Centro.init(
  {
    id: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },
    nombre: { type: DataTypes.STRING, allowNull: false },
    codigo: { type: DataTypes.STRING, allowNull: false, unique: true },
    activo: { type: DataTypes.BOOLEAN, defaultValue: true },
  },
  { sequelize, modelName: 'Centro', tableName: 'centros', timestamps: true }
);

class Tren extends Model {}
Tren.init(
  {
    id: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },
    nombre: { type: DataTypes.STRING, allowNull: false },
    centroId: { type: DataTypes.INTEGER, allowNull: false },
    activo: { type: DataTypes.BOOLEAN, defaultValue: true },
  },
  { sequelize, modelName: 'Tren', tableName: 'trenes', timestamps: true }
);

class Linea extends Model {}
Linea.init(
  {
    id: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },
    nombre: { type: DataTypes.STRING, allowNull: false },
    trenId: { type: DataTypes.INTEGER, allowNull: false },
    activo: { type: DataTypes.BOOLEAN, defaultValue: true },
  },
  { sequelize, modelName: 'Linea', tableName: 'lineas', timestamps: true }
);

Centro.hasMany(Tren, { foreignKey: 'centroId', as: 'trenes' });
Tren.belongsTo(Centro, { foreignKey: 'centroId', as: 'centro' });

Tren.hasMany(Linea, { foreignKey: 'trenId', as: 'lineas' });
Linea.belongsTo(Tren, { foreignKey: 'trenId', as: 'tren' });

module.exports = { Centro, Tren, Linea };
