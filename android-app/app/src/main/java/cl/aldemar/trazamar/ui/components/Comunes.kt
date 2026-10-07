package cl.aldemar.trazamar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.aldemar.trazamar.ui.theme.*

data class EstiloEstado(val fondo: Color, val texto: Color, val etiqueta: String)

fun estiloParaEstado(estado: String): EstiloEstado = when (estado) {
    "borrador" -> EstiloEstado(Color(0xFFEEF1F1), InkMuted, "Borrador")
    "pendiente" -> EstiloEstado(AmberSoft, Color(0xFF8A5B12), "Pendiente")
    "observada" -> EstiloEstado(AmberSoft, Color(0xFF8A5B12), "Observada")
    "corregida" -> EstiloEstado(BaySoft, BayPrimary, "Corregida")
    "validada" -> EstiloEstado(KelpSoft, Color(0xFF1F7A63), "Validada")
    else -> EstiloEstado(Color(0xFFEEF1F1), InkMuted, estado)
}

@Composable
fun EstadoBadge(estado: String, modifier: Modifier = Modifier) {
    val estilo = estiloParaEstado(estado)
    Surface(
        color = estilo.fondo,
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Text(
            text = estilo.etiqueta,
            color = estilo.texto,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrazaMarTopBar(
    titulo: String,
    subtitulo: String? = null,
    alVolver: (() -> Unit)? = null,
) {
    TopAppBar(
        title = {
            androidx.compose.foundation.layout.Column {
                Text(titulo, fontWeight = FontWeight.Bold)
                if (subtitulo != null) {
                    Text(subtitulo, style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.85f))
                }
            }
        },
        navigationIcon = {
            if (alVolver != null) {
                IconButton(onClick = alVolver) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BayPrimary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
        )
    )
}
