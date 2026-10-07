@file:OptIn(ExperimentalMaterial3Api::class)

package cl.aldemar.trazamar.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.aldemar.trazamar.data.session.SesionUsuario
import cl.aldemar.trazamar.ui.theme.*

private data class Acceso(val titulo: String, val descripcion: String, val icono: ImageVector, val color: Color, val onClick: () -> Unit)

private val ROL_LABEL = mapOf(
    "admin" to "Administrador",
    "operador" to "Operador de muestreo",
    "supervisor" to "Supervisor técnico",
    "analista" to "Analista de reportes",
)

@Composable
fun HomeScreen(
    sesion: SesionUsuario,
    onNuevaMuestra: () -> Unit,
    onHistorial: () -> Unit,
    onAdminUsuarios: () -> Unit,
    onAdminCatalogos: () -> Unit,
    onCerrarSesion: () -> Unit,
) {
    val accesos = buildList {
        when (sesion.rol) {
            "operador" -> {
                add(Acceso("Nueva muestra", "Registrar muestreo en terreno", Icons.Default.AddAPhoto, BayPrimary, onNuevaMuestra))
                add(Acceso("Mi historial", "Ver mis muestras y su estado", Icons.Default.History, KelpSecondary, onHistorial))
            }
            "supervisor" -> {
                add(Acceso("Revisión técnica", "Validar u observar muestras", Icons.Default.FactCheck, Amber, onHistorial))
                add(Acceso("Historial completo", "Trazabilidad por centro/tren/línea", Icons.Default.History, KelpSecondary, onHistorial))
            }
            "analista" -> {
                add(Acceso("Historial y reportes", "Filtrar, analizar y exportar", Icons.Default.QueryStats, KelpSecondary, onHistorial))
            }
            "admin" -> {
                add(Acceso("Cuentas de usuario", "Crear y administrar roles", Icons.Default.ManageAccounts, BayPrimary, onAdminUsuarios))
                add(Acceso("Centros / trenes / líneas", "Administrar catálogos", Icons.Default.Category, KelpSecondary, onAdminCatalogos))
                add(Acceso("Historial general", "Ver todas las muestras", Icons.Default.History, Amber, onHistorial))
            }
        }
    }

    Scaffold(
        containerColor = Tide,
        topBar = {
            TopAppBar(
                title = { Text("TrazaMar", fontWeight = FontWeight.Bold, color = Color.White) },
                actions = {
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.Default.Logout, contentDescription = "Cerrar sesión", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BayPrimary),
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = KelpSecondary, modifier = Modifier.size(48.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            sesion.nombreCompleto?.firstOrNull()?.toString() ?: "?",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(sesion.nombreCompleto ?: "", fontWeight = FontWeight.Bold, color = Ink)
                    Text(ROL_LABEL[sesion.rol] ?: sesion.rol ?: "", color = InkMuted, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("¿Qué necesitas hacer hoy?", style = MaterialTheme.typography.titleMedium, color = Ink)
            Spacer(Modifier.height(12.dp))

            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(accesos) { acceso -> TarjetaAcceso(acceso) }
            }
        }
    }
}

@Composable
private fun TarjetaAcceso(acceso: Acceso) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        tonalElevation = 1.dp,
        shadowElevation = 2.dp,
        onClick = acceso.onClick,
        modifier = Modifier.fillMaxWidth().height(140.dp),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Surface(shape = CircleShape, color = acceso.color.copy(alpha = 0.15f)) {
                Icon(acceso.icono, contentDescription = null, tint = acceso.color, modifier = Modifier.padding(10.dp).size(22.dp))
            }
            Column {
                Text(acceso.titulo, fontWeight = FontWeight.Bold, color = Ink, style = MaterialTheme.typography.titleMedium)
                Text(acceso.descripcion, color = InkMuted, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            }
        }
    }
}
