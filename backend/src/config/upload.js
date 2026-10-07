const multer = require('multer');
const path = require('path');
const fs = require('fs');

const uploadDir = path.join(__dirname, '..', '..', 'uploads');
if (!fs.existsSync(uploadDir)) fs.mkdirSync(uploadDir, { recursive: true });

const storage = multer.diskStorage({
  destination: (req, file, cb) => cb(null, uploadDir),
  filename: (req, file, cb) => {
    const ext = path.extname(file.originalname) || '.jpg';
    const nombre = `muestra_${Date.now()}_${Math.round(Math.random() * 1e6)}${ext}`;
    cb(null, nombre);
  },
});

const fileFilter = (req, file, cb) => {
  const permitido = ['image/jpeg', 'image/png', 'image/webp'];
  if (permitido.includes(file.mimetype)) cb(null, true);
  else cb(new Error('Formato de imagen no soportado. Use JPG, PNG o WEBP.'));
};

const upload = multer({ storage, fileFilter, limits: { fileSize: 8 * 1024 * 1024 } });

module.exports = upload;
