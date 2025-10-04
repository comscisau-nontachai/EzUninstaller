package nontachai.becomedev.uninstaller.domain.model

data class StorageInfoModel(
    val total: Long,
    val free: Long,
    val used: Long
)