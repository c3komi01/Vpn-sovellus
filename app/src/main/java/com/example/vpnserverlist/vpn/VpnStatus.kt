package com.example.vpnserverlist.vpn



import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class VpnState { DISCONNECTED, CONNECTING, CONNECTED, ERROR }

object VpnStatus {
    private val _state = MutableStateFlow(VpnState.DISCONNECTED)
    val state = _state.asStateFlow()

    private val _lastError = MutableStateFlow<String?>(null)
    val lastError = _lastError.asStateFlow()

    fun setConnecting() {
        _lastError.value = null
        _state.value = VpnState.CONNECTING
    }

    fun setConnected() {
        _lastError.value = null
        _state.value = VpnState.CONNECTED
    }

    fun setDisconnected() {
        _lastError.value = null
        _state.value = VpnState.DISCONNECTED
    }

    fun setError(message: String) {
        _lastError.value = message
        _state.value = VpnState.ERROR
    }
}
