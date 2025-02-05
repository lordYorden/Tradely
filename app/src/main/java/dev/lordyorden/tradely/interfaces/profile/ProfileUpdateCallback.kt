package dev.lordyorden.tradely.interfaces.profile

interface ProfileUpdateCallback {
    fun onUpdateSuccess(id: String)
    fun onUpdateFailed(id: String)
}