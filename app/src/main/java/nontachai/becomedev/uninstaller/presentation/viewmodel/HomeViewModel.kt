package nontachai.becomedev.uninstaller.presentation.viewmodel

import android.R.attr.packageNames
import android.app.PendingIntent
import android.app.appsearch.StorageInfo
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.os.StatFs
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import nontachai.becomedev.uninstaller.domain.model.AppInfoModel
import nontachai.becomedev.uninstaller.domain.model.StorageInfoModel
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nontachai.becomedev.uninstaller.core.utils.UninstallReceiver
import kotlin.jvm.java

class HomeViewModel(context: Context) : ViewModel() {

    private val _appList = MutableStateFlow<List<AppInfoModel>>(emptyList())
    val appList: StateFlow<List<AppInfoModel>> = _appList

    var showDialog by mutableStateOf(false)

    fun onShowDialog() {
        showDialog = true
    }

    fun onDismissDialog() {
        showDialog = false
    }


    init {
        getInstalledApps(context = context)
    }

    fun getInstalledApps(context: Context) {
        val packageManager = context.packageManager
        val allApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val apps =
            allApps.filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 } // Filter out system apps
                .map { app ->

                    AppInfoModel(
                        name = app.loadLabel(packageManager).toString(),
                        packageName = app.packageName,
                        icon = app.loadIcon(packageManager),
                        size = app.sourceDir?.let { path ->
                            val file = java.io.File(path)
                            file.length()
                        },
                    )
                }
                .sortedBy { it.name.lowercase() } // Sort alphabetically
        _appList.value = apps
    }

    fun setApps(apps: List<AppInfoModel>) {
        _appList.value = apps.toMutableList()
    }

    // update single item check
    fun updateChecked(packageName: String, checked: Boolean) {
        _appList.update { list ->
            list.map {
                if (it.packageName == packageName) it.copy(isChecked = checked) else it
            }.toMutableList()
        }
    }

    fun updateCheckedAll(checked: Boolean) {
        _appList.update { list ->
            list.map {
                it.copy(isChecked = checked)
            }.toMutableList()
        }
    }


    fun getInternalStorage(): StorageInfoModel {
        val path = Environment.getDataDirectory()   // internal storage path (/data)
        val stat = StatFs(path.path)

        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        val availableBlocks = stat.availableBlocksLong

        val total = totalBlocks * blockSize
        val free = availableBlocks * blockSize
        val used = total - free

        return StorageInfoModel(total, free, used)
    }

    fun uninstallAppsByPackageNames(packageNames: List<String>,context: Context) {
        for (packageName in packageNames) {
            val uri = Uri.parse("package:$packageName")
            val intent = Intent(Intent.ACTION_DELETE, uri)
            intent.putExtra(Intent.EXTRA_RETURN_RESULT, true) // Request a result back

            try {
                //uninstallLauncher.launch(intent)
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            } catch (e: ActivityNotFoundException) {
                Log.e("Uninstall", "No activity found to handle uninstall for $packageName: ${e.message}")
            }
        }
    }


}
