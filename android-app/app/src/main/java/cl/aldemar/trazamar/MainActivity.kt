package cl.aldemar.trazamar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import cl.aldemar.trazamar.ui.navigation.TrazaMarNavGraph
import cl.aldemar.trazamar.ui.theme.Tide
import cl.aldemar.trazamar.ui.theme.TrazaMarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrazaMarTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Tide) {
                    TrazaMarNavGraph()
                }
            }
        }
    }
}
