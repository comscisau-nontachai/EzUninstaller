package nontachai.becomedev.uninstaller.domain.model

import android.graphics.drawable.Drawable

class AppInfoModel(
    val name: String,
    val packageName: String,
    val icon: Drawable,
    val size: Long? = null // optional if you also want storage size
) {
}