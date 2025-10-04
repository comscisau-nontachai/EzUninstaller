package nontachai.becomedev.uninstaller.domain.model

import android.graphics.drawable.Drawable
import kotlinx.serialization.Serializable



data class AppInfoModel(
    val name: String = "",
    val packageName: String = "",
    val icon: Drawable? = null,
    val size: Long? = null, // optional if you also want storage size
    var isChecked : Boolean = false
) {
}