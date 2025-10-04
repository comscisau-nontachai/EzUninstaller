package nontachai.becomedev.uninstaller

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nontachai.becomedev.uninstaller.presentation.navigation.NavGraph
import nontachai.becomedev.uninstaller.presentation.ui.theme.EzUnintallerTheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import com.google.android.gms.ads.MobileAds
import nontachai.becomedev.uninstaller.di.appModule
import nontachai.becomedev.uninstaller.presentation.screen.HomeScreen
import nontachai.becomedev.uninstaller.presentation.ui.theme.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import kotlin.coroutines.coroutineContext

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any application-wide resources here if needed

        startKoin {
            androidContext(this@MyApp)
            modules(appModule)
        }

        // Initialize Google Mobile Ads SDK
        MobileAds.initialize(this) { initializationStatus ->
            // This callback is called once the initialization is complete.

            // Get the status of all adapters.
            val statusMap = initializationStatus.adapterStatusMap

            for (adapterClass in statusMap.keys) {
                val status = statusMap[adapterClass]

                // Log the adapter's class name and its initialization status.
                Log.d("LOGD", String.format(
                    "Adapter: %s, State: %s, Description: %s, Latency: %dms",
                    adapterClass,
                    status?.initializationState,
                    status?.description,
                    status?.latency
                ))
            }

            // You can now safely make your first ad request.
            // Note: It's also safe to request ads before this callback,
            // as the SDK will queue the request.
        }
    }
}
