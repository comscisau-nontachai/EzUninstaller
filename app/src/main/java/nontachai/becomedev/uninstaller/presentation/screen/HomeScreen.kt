package nontachai.becomedev.uninstaller.presentation.screen

import android.R.attr.checked
import android.R.attr.contentDescription
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nontachai.becomedev.uninstaller.AdMobBanner
import nontachai.becomedev.uninstaller.BuildConfig
import nontachai.becomedev.uninstaller.R
import nontachai.becomedev.uninstaller.core.utils.convertBytesToFileExt
import nontachai.becomedev.uninstaller.core.utils.convertBytesToMB
import nontachai.becomedev.uninstaller.domain.model.AppInfoModel
import nontachai.becomedev.uninstaller.presentation.ui.theme.Cinnabar
import nontachai.becomedev.uninstaller.presentation.ui.theme.CoolGray
import nontachai.becomedev.uninstaller.presentation.ui.theme.Red
import nontachai.becomedev.uninstaller.presentation.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel
import java.nio.file.WatchEvent

@ExperimentalMaterial3Api
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val viewModel: HomeViewModel = koinViewModel()
    val appsList by viewModel.appList.collectAsStateWithLifecycle()
    val appStorage = viewModel.getInternalStorage()
    val checkedCount = appsList.count { it.isChecked }
    val selectedApp = remember(appsList) { appsList.filter { it.isChecked } }
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()


    Box(modifier = Modifier.fillMaxSize()) {

        // Background split into black (top) and white (bottom)
        BuildBackground()
        //content
        Column {
            StorageCard(
                apps = appsList,
                internalStorage = appStorage.total,
                usedStorage = appStorage.used,
                freeStorage = appStorage.free
            )
            Spacer(modifier = Modifier.height(8.dp))
            AdMobBanner(BuildConfig.ADMOB_APP_ID)
            LabelAndSelectAll()
            AppsList(
                appsList = appsList,
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    viewModel.getInstalledApps(context)
                    coroutineScope.launch {
                        delay(1000)
                        isRefreshing = false
                    }

                }
            )


        }

        // Floating Action Button
        ExtendedFloatingActionButton(
            onClick = {
                if (selectedApp.isEmpty()) return@ExtendedFloatingActionButton
                viewModel.onShowDialog()
            },
            containerColor = Cinnabar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(56.dp)
                .padding(bottom = 12.dp),
        ) {
            Text("Uninstall (${checkedCount})", color = Color.White)
        }


        //dialog
        if (viewModel.showDialog) {

            ShowUninstallDialog(
                selectedApp = selectedApp,
                onDismiss = {
                    viewModel.onDismissDialog()
                }, onConfirm = {
//                    selectedApp.forEach { item ->
//                        viewModel.uninstallApp(item.packageName, context)
//
//                    }

                    val packageNames = selectedApp.map { it.packageName }
                    viewModel.uninstallAppsByPackageNames(packageNames, context)

                    coroutineScope.launch {
                        delay(2000)
                        viewModel.getInstalledApps(context)
                        viewModel.onDismissDialog()
                    }
                })
        }
    }

}

@Preview
@Composable
fun HomeScreenPreview() {
    //HomeScreen()
}

@Composable
fun BuildBackground() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .background(CoolGray)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {

        }
    }
}

@Composable
fun StorageCard(
    apps: List<AppInfoModel>,
    internalStorage: Long,
    usedStorage: Long,
    freeStorage: Long,
) {
    // Hovering Card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = 0.dp), // adjust overlap position
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAEAEA))
    ) {
        //storage info
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circle icon with shadow (stacked look)
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .shadow(4.dp, CircleShape)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Replace with your icon
                Image(
                    painter = painterResource(id = R.drawable.ic_smartphone),
                    contentDescription = contentDescription.toString(),
                    contentScale = ContentScale.Crop, // Or ContentScale.Fit, etc.
                    modifier = Modifier.size(36.dp) // Make the image fill the circular Box
                )

                val percentage = usedStorage.toFloat() / internalStorage.toFloat() * 100
                StorageProgress(percentage / 100f)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Internal Storage",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Total ${internalStorage.convertBytesToFileExt(LocalContext.current)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Used Storage",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            usedStorage.convertBytesToFileExt(LocalContext.current),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Cinnabar,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Free Storage",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            freeStorage.convertBytesToFileExt(LocalContext.current),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        //info
        HeaderAppInfo(
            totalApp = apps.size,
            totalAppSize = apps.sumOf { it.size ?: 0L }.convertBytesToMB().toDouble().toLong()
        )
    }
}

@Composable
fun StorageProgress(usedFraction: Float) {
    CircularProgressIndicator(
        progress = { usedFraction },  // value 0f..1f
        modifier = Modifier.size(64.dp),
        trackColor = Color.LightGray,
        color = Cinnabar, // custom color
        strokeWidth = 4.dp
    )
}

@Composable
fun HeaderAppInfo(
    totalApp: Int,
    totalAppSize: Long
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left item
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_dashboard),
                contentDescription = "Total Apps",
                tint = Color.Gray,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Total Apps : $totalApp",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

        }

        // Right item
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_pie_chart),
                contentDescription = "Total Size",
                tint = Color.Gray,
                modifier = Modifier.size(14.dp)

            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Total size : $totalAppSize MB",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun LabelAndSelectAll(viewModel: HomeViewModel = koinViewModel()) {
    var selectAll by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "All Apps",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))

        ElevatedButton(
            onClick = {
                selectAll = !selectAll
                viewModel.updateCheckedAll(selectAll)
            },
            shape = RoundedCornerShape(percent = 20),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = CoolGray,
                contentColor = Color.White
            ),
        ) {
            Text(
                text = "Select All",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }


    }
}

@ExperimentalMaterial3Api
@Composable
fun AppsList(
    viewModel: HomeViewModel = koinViewModel(),
    appsList: List<AppInfoModel>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
    ) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ){
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(appsList) { item ->
                AppItem(
                    appName = item.name,
                    appSize = "${(item.size ?: 0L).convertBytesToMB()} MB",
                    iconBitmap = item.icon?.toBitmap(),
                    checked = item.isChecked,
                    onCheckedChange = { checked ->
                        viewModel.updateChecked(item.packageName, checked)
                    }
                )
            }
        }
    }


}

@Composable
fun AppItem(
    appName: String,
    appSize: String,
    iconBitmap: Bitmap?, // your drawable resource id
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Icon
        Image(
            bitmap = iconBitmap?.asImageBitmap() ?: bitmapFromDrawable(
                context,
                R.drawable.ic_launcher_foreground
            ).asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(20)),
        )

        Spacer(modifier = Modifier.width(12.dp))

        // App name + size
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = appName,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = appSize,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        // Checkbox
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun ShowUninstallDialog(
    selectedApp: List<AppInfoModel>,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = { false },
        title = { Text(text = "Uninstall Apps") },
        text = { Text("Are you sure you want to uninstall selected apps?") },
        confirmButton = {
            ElevatedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = CoolGray,
                    contentColor = Color.White
                ),
                onClick = { onConfirm() }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            ElevatedButton(
                onClick = { onDismiss() }
            ) {
                Text("Cancel")
            }
        }
    )
}


fun bitmapFromDrawable(context: Context, drawableId: Int): Bitmap {
    // For VectorDrawables, this is the recommended approach.
    val drawable = ContextCompat.getDrawable(context, drawableId)
        ?: throw IllegalArgumentException("Drawable not found")

    // Create a bitmap of the same size as the drawable.
    // Use ARGB_8888 for high quality, including transparency.
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // Create a canvas to draw on the bitmap.
    val canvas = Canvas(bitmap)

    // Set the drawable's bounds to the canvas's size.
    drawable.setBounds(0, 0, canvas.width, canvas.height)

    // Draw the drawable onto the canvas.
    drawable.draw(canvas)

    return bitmap
}
