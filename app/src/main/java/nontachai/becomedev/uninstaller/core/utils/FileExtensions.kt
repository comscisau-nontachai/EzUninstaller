package nontachai.becomedev.uninstaller.core.utils

import android.content.Context
import android.text.format.Formatter

/*fun formatSize(context: Context, size: Long): String {
    return Formatter.formatFileSize(context, size)
}*/

fun Long.convertBytesToMB(): String {
    val mb = this.toDouble() / (1024 * 1024)
    return String.format("%.2f", mb)
}

fun Long.convertBytesToFileExt(context: Context): String {
    //val gb = this.toDouble() / (1024 * 1024 * 1024)
    return Formatter.formatFileSize(context , this)
}

