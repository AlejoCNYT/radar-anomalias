const express = require('express');
const path = require('path');

const app = express();
const PORT = 8082;

// Servir archivos estáticos
app.use(express.static(__dirname));

// Ruta raíz
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'index.html'));
});

// ⚠️ Eliminar rutas wildcard para evitar errores con Express 5
// app.get('/*', (req, res) => { ... });

app.listen(PORT, () => {
  console.log(`🚀 Dashboard running at http://localhost:${PORT}`);
});
