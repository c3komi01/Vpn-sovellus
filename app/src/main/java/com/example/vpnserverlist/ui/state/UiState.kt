package com.example.vpnserverlist.ui.state

import com.example.vpnserverlist.data.model.VPNServer

enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED
}

sealed class UiState {

    data object Loading : UiState()

    data class Content(
        val servers: List<VPNServer>,
        val selected: VPNServer?,
        val connectionState: ConnectionState
    ) : UiState()

    data class Error(
        val message: String
    ) : UiState()
}
