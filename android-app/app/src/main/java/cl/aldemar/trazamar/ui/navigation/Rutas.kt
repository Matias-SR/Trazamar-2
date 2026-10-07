package cl.aldemar.trazamar.ui.navigation

object Rutas {
    const val LOGIN = "login"
    const val HOME = "home"
    const val NUEVA_MUESTRA = "nueva_muestra"
    const val HISTORIAL = "historial"
    const val DETALLE_MUESTRA = "detalle_muestra/{muestraId}"
    const val ADMIN_USUARIOS = "admin_usuarios"
    const val ADMIN_CATALOGOS = "admin_catalogos"

    fun detalleMuestra(id: Int) = "detalle_muestra/$id"
}
