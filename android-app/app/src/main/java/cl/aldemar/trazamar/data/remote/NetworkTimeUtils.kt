package cl.aldemar.trazamar.data.remote

import cl.aldemar.trazamar.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

object NetworkTimeUtils {

    /**
     * Obtiene la fecha y la hora actual a nivel de red (timestamp).
     * Intenta en orden:
     * 1. WorldTimeAPI (API pública de tiempo de red)
     * 2. Cabecera HTTP 'Date' del servidor backend de TrazaMar
     * 3. Fallback local LocalDateTime.now() en caso de desconexión.
     */
    suspend fun obtenerFechaHoraRed(): Pair<String, String> = withContext(Dispatchers.IO) {
        val client = OkHttpClient.Builder()
            .connectTimeout(4, TimeUnit.SECONDS)
            .readTimeout(4, TimeUnit.SECONDS)
            .build()

        // Opción 1: API pública de tiempo en red (worldtimeapi.org)
        try {
            val request = Request.Builder()
                .url("https://worldtimeapi.org/api/ip")
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val jsonStr = response.body?.string()
                if (!jsonStr.isNullOrBlank()) {
                    val json = JSONObject(jsonStr)
                    val datetimeStr = json.optString("datetime")
                    if (datetimeStr.isNotBlank()) {
                        val zdt = ZonedDateTime.parse(datetimeStr)
                        val localZdt = zdt.withZoneSameInstant(ZoneId.systemDefault())
                        val fecha = localZdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        val hora = localZdt.format(DateTimeFormatter.ofPattern("HH:mm"))
                        return@withContext fecha to hora
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Opción 2: Cabecera HTTP 'Date' del servidor backend de TrazaMar
        try {
            val request = Request.Builder()
                .url(BuildConfig.BASE_URL)
                .head()
                .build()

            val response = client.newCall(request).execute()
            val dateHeader = response.headers["Date"]
            if (!dateHeader.isNullOrBlank()) {
                val parsedDate = ZonedDateTime.parse(dateHeader, DateTimeFormatter.RFC_1123_DATE_TIME)
                val localZdt = parsedDate.withZoneSameInstant(ZoneId.systemDefault())
                val fecha = localZdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val hora = localZdt.format(DateTimeFormatter.ofPattern("HH:mm"))
                return@withContext fecha to hora
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Opción 3: Fallback a tiempo local del sistema
        val currentDateTime = LocalDateTime.now()
        val fecha = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val hora = currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        fecha to hora
    }
}
