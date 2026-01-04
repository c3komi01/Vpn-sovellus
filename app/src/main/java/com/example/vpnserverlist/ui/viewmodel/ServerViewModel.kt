package com.example.vpnserverlist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpnserverlist.data.model.VPNServer
import com.example.vpnserverlist.data.repository.VPNServerRepository
import com.example.vpnserverlist.ui.state.ConnectionState
import com.example.vpnserverlist.ui.state.UiState
import com.example.vpnserverlist.vpn.VpnStatus
import com.example.vpnserverlist.vpn.VpnState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ServerViewModel(
    private val repo: VPNServerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _pendingConfig = MutableStateFlow<String?>(null)
    val pendingConfig: StateFlow<String?> = _pendingConfig

    private var selected: VPNServer? = null
    private var serversCache: List<VPNServer> = emptyList()
    private var connectionState: ConnectionState = ConnectionState.DISCONNECTED

    init {
        observeVpnStatus()
        refreshServers()
    }

    /* ---------------- VPN STATUS SYNC ---------------- */

    private fun observeVpnStatus() {
        viewModelScope.launch {
            VpnStatus.state.collect { vpnState ->
                connectionState = when (vpnState) {
                    VpnState.DISCONNECTED -> ConnectionState.DISCONNECTED
                    VpnState.CONNECTING -> ConnectionState.CONNECTING
                    VpnState.CONNECTED -> ConnectionState.CONNECTED
                    VpnState.ERROR -> ConnectionState.DISCONNECTED
                }
                emitContent()
            }
        }

        viewModelScope.launch {
            VpnStatus.lastError.collect { error ->
                if (error != null) {
                    _uiState.value = UiState.Error(error)
                }
            }
        }
    }

    /* ---------------- DATA ---------------- */

    fun refreshServers() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                serversCache = repo.loadServers()
                emitContent()
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    e.message ?: "Failed to load servers"
                )
            }
        }
    }

    fun selectServer(server: VPNServer) {
        selected = server
        emitContent()
    }

    /* ---------------- CONNECT ---------------- */

    fun connectSelected() {
        val server = selected ?: return

        viewModelScope.launch {
            try {
                val config = repo.requestConfig(server.id)
                _pendingConfig.value = config
                // дальше всё делает VPN Service → VpnStatus
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    e.message ?: "Connect failed"
                )
            }
        }
    }

    fun consumePendingConfig() {
        _pendingConfig.value = null
    }

    /* ---------------- UI ---------------- */

    private fun emitContent() {
        _uiState.value = UiState.Content(
            servers = serversCache,
            selected = selected,
            connectionState = connectionState
        )
    }
}
