package nontachai.becomedev.uninstaller

import android.app.Application
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
import nontachai.becomedev.uninstaller.presentation.screen.HomeScreen
import nontachai.becomedev.uninstaller.presentation.ui.theme.*
import kotlin.coroutines.coroutineContext

@ExperimentalMaterial3Api
@Composable
fun MyApp() {
    EzUnintallerTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { AppBar() }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                // A surface container using the 'background' color from the theme
                HomeScreen() // Pass the context
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun AppBar() {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Ez Uninstaller App" , color = Color.White )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO: handle actions */ }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More actions" , tint = Color.White)
            }
        },
        colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
            containerColor = CoolGray // dark gray
        )
    )
}
