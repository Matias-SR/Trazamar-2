package cl.aldemar.trazamar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.aldemar.trazamar.data.repository.AuthRepository
import cl.aldemar.trazamar.ui.screens.admin.AdminCatalogosScreen
import cl.aldemar.trazamar.ui.screens.admin.AdminUsuariosScreen
import cl.aldemar.trazamar.ui.screens.historial.HistorialScreen
import cl.aldemar.trazamar.ui.screens.home.HomeScreen
import cl.aldemar.trazamar.ui.screens.login.LoginScreen
import cl.aldemar.trazamar.ui.screens.nuevamuestra.NuevaMuestraScreen
import kotlinx.coroutines.launch

@Composable
fun TrazaMarNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val authRepo = remember { AuthRepository(context) }
    val sesion by authRepo.sesion.collectAsState(initial = null)
    val scope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = Rutas.LOGIN) {
        composable(Rutas.LOGIN) {
            LoginScreen(onLoginExitoso = {
                navController.navigate(Rutas.HOME) {
                    popUpTo(Rutas.LOGIN) { inclusive = true }
                }
            })
        }

        composable(Rutas.HOME) {
            val sesionActual = sesion
            if (sesionActual == null || sesionActual.token == null) {
                LaunchedEffect(Unit) { navController.navigate(Rutas.LOGIN) { popUpTo(0) } }
            } else {
                HomeScreen(
                    sesion = sesionActual,
                    onNuevaMuestra = { navController.navigate(Rutas.NUEVA_MUESTRA) },
                    onHistorial = { navController.navigate(Rutas.HISTORIAL) },
                    onAdminUsuarios = { navController.navigate(Rutas.ADMIN_USUARIOS) },
                    onAdminCatalogos = { navController.navigate(Rutas.ADMIN_CATALOGOS) },
                    onCerrarSesion = {
                        scope.launch {
                            authRepo.cerrarSesion()
                            navController.navigate(Rutas.LOGIN) { popUpTo(0) }
                        }
                    },
                )
            }
        }

        composable(Rutas.NUEVA_MUESTRA) {
            NuevaMuestraScreen(
                onFinalizado = { navController.popBackStack(Rutas.HOME, inclusive = false) },
                onVolverHome = { navController.popBackStack() },
            )
        }

        composable(Rutas.HISTORIAL) {
            HistorialScreen(onVolver = { navController.popBackStack() })
        }

        composable(Rutas.ADMIN_USUARIOS) {
            AdminUsuariosScreen(onVolver = { navController.popBackStack() })
        }

        composable(Rutas.ADMIN_CATALOGOS) {
            AdminCatalogosScreen(onVolver = { navController.popBackStack() })
        }
    }
}
