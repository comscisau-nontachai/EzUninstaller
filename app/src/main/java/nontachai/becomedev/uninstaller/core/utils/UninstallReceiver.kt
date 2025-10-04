package nontachai.becomedev.uninstaller.core.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.widget.Toast

class UninstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val status = intent?.getIntExtra(
            PackageInstaller.EXTRA_STATUS,
            PackageInstaller.STATUS_FAILURE
        )
        val msg = intent?.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE)

        println("Uninstall status: $status message: $msg")

        when (status) {
            PackageInstaller.STATUS_SUCCESS ->
                Toast.makeText(context, "Uninstall success!", Toast.LENGTH_SHORT).show()
            else ->
                Toast.makeText(context, "Uninstall failed: $msg", Toast.LENGTH_SHORT).show()
        }
    }
}