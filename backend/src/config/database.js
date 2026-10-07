const { Sequelize } = require('sequelize');
const path = require('path');

const storage = process.env.DB_STORAGE || path.join(__dirname, '..', '..', 'trazamar.sqlite');

const sequelize = new Sequelize({
  dialect: 'sqlite',
  storage,
  logging: process.env.DB_LOGGING === 'true' ? console.log : false,
});

module.exports = sequelize;
