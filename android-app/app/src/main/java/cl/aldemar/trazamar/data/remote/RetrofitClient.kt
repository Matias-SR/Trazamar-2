package cl.aldemar.trazamar.data.remote

import android.content.Context
import cl.aldemar.trazamar.BuildConfig
import cl.aldemar.trazamar.data.session.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Adjunta el token JWT (guardado en DataStore vía SessionManager) a cada
 * solicitud saliente, salvo el propio endpoint de login.
 */
class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        if (original.url.encodedPath.endsWith("/auth/login")) {
            return chain.proceed(original)
        }
        val token = runBlocking { sessionManager.sesionActual().token }
        val request = if (!token.isNullOrBlank()) {
            original.newBuilder().addHeader("Authorization", "Bearer $token").build()
        } else {
            original
        }
        return chain.proceed(request)
    }
}

object RetrofitClient {
    @Volatile private var apiService: ApiService? = null

    fun getInstance(context: Context): ApiService {
        return apiService ?: synchronized(this) {
            apiService ?: build(context.applicationContext).also { apiService = it }
        }
    }

    private fun build(context: Context): ApiService {
        val sessionManager = SessionManager(context)

        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .addInterceptor(logging)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
