import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import com.bugabuga.appecommerce.ui.screens.HomeScreen
import com.bugabuga.appecommerce.ui.theme.AppEcommerceTheme
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import okio.Path.Companion.toOkioPath
import java.io.File

@Composable
fun App() {
    val imageLoader = ImageLoader {
        components {
            setupDefaultComponents()
        }
        interceptor {
            memoryCacheConfig {
                maxSizePercent(0.25)
            }
            diskCacheConfig {
                directory(File(System.getProperty("java.io.tmpdir"), "image_cache").toOkioPath())
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }

    AppEcommerceTheme {
        androidx.compose.runtime.CompositionLocalProvider(
            LocalImageLoader provides imageLoader
        ) {
            Navigator(HomeScreen())
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "E-Commerce App"
    ) {
        App()
    }
}
