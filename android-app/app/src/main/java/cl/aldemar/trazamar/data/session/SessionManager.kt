package cl.aldemar.trazamar.data.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "trazamar_session")

data class SesionUsuario(
    val token: String?,
    val id: Int?,
    val username: String?,
    val nombreCompleto: String?,
    val rol: String?,
)

class SessionManager(private val context: Context) {

    private object Claves {
        val TOKEN = stringPreferencesKey("token")
        val ID = intPreferencesKey("id")
        val USERNAME = stringPreferencesKey("username")
        val NOMBRE = stringPreferencesKey("nombreCompleto")
        val ROL = stringPreferencesKey("rol")
    }

    val sesion: Flow<SesionUsuario> = context.dataStore.data.map { prefs ->
        SesionUsuario(
            token = prefs[Claves.TOKEN],
            id = prefs[Claves.ID],
            username = prefs[Claves.USERNAME],
            nombreCompleto = prefs[Claves.NOMBRE],
            rol = prefs[Claves.ROL],
        )
    }

    suspend fun sesionActual(): SesionUsuario = sesion.first()

    suspend fun guardarSesion(token: String, id: Int, username: String, nombreCompleto: String, rol: String) {
        context.dataStore.edit { prefs ->
            prefs[Claves.TOKEN] = token
            prefs[Claves.ID] = id
            prefs[Claves.USERNAME] = username
            prefs[Claves.NOMBRE] = nombreCompleto
            prefs[Claves.ROL] = rol
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { it.clear() }
    }
}
