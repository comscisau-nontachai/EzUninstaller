package nontachai.becomedev.uninstaller.presentation.screen

import android.R.attr.contentDescription
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import nontachai.becomedev.uninstaller.R
import nontachai.becomedev.uninstaller.domain.model.AppInfoModel
import nontachai.becomedev.uninstaller.presentation.ui.theme.CoolGray

@Composable
fun HomeScreen() {
    val context  = androidx.compose.ui.platform.LocalContext.current
    val appsList = remember { getInstalledApps(context) }

    Box(modifier = Modifier.fillMaxSize()) {

        // Background split into black (top) and white (bottom)
        BuildBackground()

        //content
        Column {
            StorageCard()
            LabelAndSelectAll()
            AppsList(appsList)
        }
    }

}

@Preview
@Composable
fun HomeScreenPreview() {
    //HomeScreen()
}

@Composable
fun BuildBackground(){
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
fun StorageCard() {
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
                    "Total 108.4 GB",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Used Storage",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF3F51B5)
                        )
                        Text(
                            "11.8 GB",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF3F51B5),
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
                            "96.6 GB",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        //info
        HeaderAppInfo()
    }
}

@Composable
fun HeaderAppInfo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left item
        Row(verticalAlignment = Alignment.CenterVertically) {
            androidx.compose.material3.Icon(
                painter = painterResource(R.drawable.ic_dashboard),
                contentDescription = "Total Apps",
                tint = Color.Gray,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Total Apps : 68",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

        }

        // Right item
        Row(verticalAlignment = Alignment.CenterVertically) {
            androidx.compose.material3.Icon(
                painter = painterResource(R.drawable.ic_pie_chart),
                contentDescription = "Total Size",
                tint = Color.Gray,
                modifier = Modifier.size(14.dp)

            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Total size : 268 MB",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun LabelAndSelectAll(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "All Apps", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))

        ElevatedButton(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(percent = 20),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = CoolGray,
                contentColor = Color.White
            ),
        ) {
            Text(text = "Select All", style = MaterialTheme.typography.bodyMedium, color = Color.White)
        }


    }
}

@Composable
fun AppsList(appsList: List<AppInfoModel>, isCheck: Boolean = false) {
    var isChecked by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(appsList) { item ->
            AppItem(
                appName = item.name,
                appSize = "${convertBytesToMB(item.size ?: 0L)} MB",
                iconBitmap =  item.icon.toBitmap(), // replace with your drawable resource
                checked = true,
                onCheckedChange = {
                    isChecked = it
                }
            )
        }
    }
}

@Composable
fun AppItem(
    appName: String,
    appSize: String,
    iconBitmap: Bitmap, // your drawable resource id
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Icon
        Image(
            bitmap = iconBitmap.asImageBitmap(),
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



//move to viewmodel or util
fun getInstalledApps(context: Context): List<AppInfoModel> {
    val pm: PackageManager = context.packageManager
    val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
    //apps.filter { pm.getLaunchIntentForPackage(it.packageName) != null }

    return apps.filter { pm.getLaunchIntentForPackage(it.packageName) != null }.map {
        val name = pm.getApplicationLabel(it).toString()
        val icon: Drawable = pm.getApplicationIcon(it)
        val size = it.sourceDir?.let { path ->
            val file = java.io.File(path)
            file.length()
        }
        AppInfoModel(
            name = name,
            packageName = it.packageName,
            icon = icon,
            size = size
        )
    }
}

fun convertBytesToMB(bytes: Long): String {
    val mb = bytes.toDouble() / (1024 * 1024)
    return String.format("%.2f", mb)
}