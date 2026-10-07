# TrazaMar (AquaSample) — MVP de trazabilidad de muestreo de choritos

Proyecto para ALDEMAR SpA — DSY1105, equipo NotFound-Devs.
Este repositorio contiene **tres componentes independientes** que conforman la solución completa:

```
trazamar/
├── backend/        API REST (Node.js + Express + Sequelize/SQLite)
├── web-panel/      Panel web para el Analista (React + Vite)
└── android-app/    App móvil (Kotlin + Jetpack Compose) — Operador, Supervisor, Analista, Admin
```

## 1. ¿Por qué esta arquitectura?

- **Backend único** para que la app móvil (Operador/Supervisor/Analista) y el panel web (Analista/Admin/Supervisor
  desde su computador) compartan siempre los mismos datos, en tiempo real.
- **SQLite** como motor de base de datos: cero configuración para correr el proyecto en un contexto académico, pero
  el código usa Sequelize, por lo que migrar a PostgreSQL en el futuro solo requiere cambiar `dialect` en
  `backend/src/config/database.js`.
- **Room (SQLite local)** en la app Android como caché: el Operador y Supervisor pueden seguir viendo su último
  historial sincronizado aunque pierdan conexión en terreno.
- **Roles y permisos** aplicados en el backend (middleware JWT) — cada endpoint valida el rol, no solo la app.

## 2. Cómo correr el Backend

```bash
cd backend
npm install
cp .env.example .env
npm run seed      # crea las tablas y datos ficticios de prueba
npm run dev        # o "npm start"
```

El servidor queda en `http://localhost:4000`. Health check: `GET /api/health`.

### Usuarios de prueba (creados por el seed)

| Usuario       | Contraseña   | Rol                     |
|---------------|--------------|-------------------------|
| `admin`       | `Aqua2026*`  | Administrador           |
| `operador1`   | `Aqua2026*`  | Operador de muestreo    |
| `supervisor1` | `Aqua2026*`  | Supervisor técnico      |
| `analista1`   | `Aqua2026*`  | Analista de reportes    |

> En producción real, cambia `JWT_SECRET` en `.env` y usa contraseñas propias — estas son solo para la demo académica.

## 3. Cómo correr el Panel Web (Analista / Supervisor / Admin desde el computador)

```bash
cd web-panel
npm install
npm run dev
```

Se abre en `http://localhost:5173` (Vite). Por defecto apunta a `http://localhost:4000/api` (ver `web-panel/.env`,
variable `VITE_API_URL`) — cambia esa URL si el backend corre en otra máquina.

El panel web es para **analista, supervisor y administrador** (el operador solo usa la app móvil).

## 4. Cómo correr la App Android

1. Abre la carpeta `android-app/` con **Android Studio** (Koala o superior).
2. Abre el proyecto y deja que Android Studio sincronice Gradle. Si Android Studio solicita instalar SDK/Build Tools, acepta las versiones indicadas por el proyecto.
3. Ejecuta en un **emulador**: la URL base ya apunta a `http://10.0.2.2:4000/api/`, que es como el emulador ve el
   `localhost` de tu computador — así que solo necesitas tener el backend corriendo en tu PC.
4. Si vas a probar en un **celular físico**, cambia `BASE_URL` en `android-app/app/build.gradle.kts` por la IP de tu
   computador en la red local (ej. `http://192.168.1.10:4000/api/`) y asegúrate de que el firewall permita la conexión.

### Permisos que pedirá la app
- **Cámara**: para fotografiar la evidencia de cada muestra (Paso 2 del registro).
- **Galería / almacenamiento**: como alternativa a la cámara.

## 5. Roles del sistema (resumen funcional)

| Rol                    | Dónde opera          | Qué puede hacer |
|-------------------------|----------------------|------------------|
| **Operador de muestreo** | App móvil            | Crear muestras (formulario → foto → conteo → resumen), ver su propio historial, corregir muestras observadas. |
| **Supervisor técnico**   | App móvil            | Ver muestras pendientes/corregidas, **validar** u **observar** con comentario. |
| **Analista de reportes** | App móvil **y** Panel Web | Ver historial completo, filtrar por centro/tren/línea/estado/fecha, ver gráficos (dashboard web) y exportar CSV. |
| **Administrador**        | App móvil (y backend)| Crear/editar/desactivar cuentas y asignar roles; administrar centros, trenes y líneas. |

## 6. Identidad visual

Paleta oficial (azules de bahía + verde alga), aplicada de forma consistente en el panel web y la app Android:

| Color | Uso |
|---|---|
| `#0D6E8C` (Bahía) | Color principal — barras, botones primarios |
| `#2FA88C` (Alga) | Color secundario — acentos, estado "validada" |
| `#E8A33D` (Ámbar) | Estado "pendiente" / "observada" |
| `#C1443C` (Rojo) | Errores y validaciones |
| `#F2F7F7` | Fondo general |
| `#1B2A2E` | Texto principal |

## 7. Decisiones de diseño y alcance del MVP

- La pantalla de **"Revisión"** del Supervisor se implementó como un **modal de detalle** dentro del Historial
  (en vez de una pantalla separada), para evitar duplicar la lógica de listado/filtrado. Cumple la misma función:
  ver el detalle completo y validar/observar con comentario.
- Los pasos **Fotografía → Conteo → Resumen** del registro de una muestra se implementaron como un **wizard de 4
  pasos dentro de una sola pantalla** (`NuevaMuestraScreen`), compartiendo un mismo ViewModel — esto es equivalente
  en experiencia de usuario a tener 4 pantallas separadas, pero más simple de mantener.
- El **modo offline** de la app está parcialmente implementado: cada vez que el Operador/Supervisor sincroniza el
  historial, se guarda una copia en una base de datos Room local. Una mejora futura natural es que el Historial lea
  automáticamente desde esa caché cuando no hay conexión (actualmente solo se usa para no perder lo último visto).
- El backend usa **SQLite** por simplicidad de instalación; el modelo de datos (Sequelize) ya está listo para
  migrar a PostgreSQL cambiando una sola línea de configuración.

## 8. Comprobaciones y correcciones incluidas

Se corrigieron problemas de seguridad e integración detectados en la revisión:

- El operador solo puede consultar, editar, fotografiar y enviar sus propias muestras.
- Al crear una muestra, el backend usa el ID del operador autenticado en vez de confiar en un ID enviado por el cliente.
- Se agregó validación del formato `HH:mm` y del conteo de individuos.
- La edición de usuarios valida que el rol sea uno de los roles soportados.
- El backend ahora devuelve 404 para rutas inexistentes y maneja errores de carga de imágenes como errores de cliente.
- La sesión del panel web se valida contra `/api/auth/me` al recargar la página.
- La URL de las fotografías en Android ya no está fija a `10.0.2.2`; se construye a partir de `BASE_URL`.
- Se eliminaron carpetas placeholder accidentales que venían dentro del proyecto.
- El backend permite configurar `PORT`, `JWT_SECRET`, `DB_LOGGING` y `DB_STORAGE` mediante `.env`.

### Verificación

La sintaxis de todos los archivos JavaScript del backend fue comprobada correctamente. La compilación completa de dependencias no pudo ejecutarse en el entorno de revisión porque el acceso al registro de npm falló durante la instalación; por eso se recomienda ejecutar `npm install` y los comandos de build indicados abajo en tu PC.

## 9. Estructura de la base de datos (resumen)

- **usuarios** (id, username, passwordHash, nombreCompleto, rol, activo)
- **centros** (id, nombre, código)
- **trenes** (id, nombre, centroId)
- **lineas** (id, nombre, trenId)
- **muestras** (id, centroId, trenId, lineaId, fecha, hora, tramoMetros, operadorId, conteoIndividuos,
  observaciones, calibrePromedio/Min/Max, pesoMuestra, fotoUrl, estado, comentarioSupervisor, supervisorId,
  fechaRevision)

Flujo de estados de una muestra: `borrador → pendiente → (validada | observada) → corregida → pendiente → validada`.
