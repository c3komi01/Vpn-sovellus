package com.example.vpnserverlist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.vpnserverlist.data.model.VPNServer
import com.example.vpnserverlist.ui.components.ServerItem
import com.example.vpnserverlist.ui.state.ConnectionState
import com.example.vpnserverlist.ui.state.UiState

private enum class SortMode { FASTEST, COUNTRY, CITY }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerListScreen(
    uiState: UiState,
    onSelectServer: (VPNServer) -> Unit,
    onBack: () -> Unit
) {
    when (uiState) {
        is UiState.Loading -> LoadingScreen()

        is UiState.Error -> ErrorScreen(
            message = uiState.message,
            onRetry = {}
        )

        is UiState.Content -> ServerListContent(
            state = uiState,
            onSelectServer = onSelectServer,
            onBack = onBack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ServerListContent(
    state: UiState.Content,
    onSelectServer: (VPNServer) -> Unit,
    onBack: () -> Unit
) {
    var sortMode by rememberSaveable { mutableStateOf(SortMode.FASTEST) }
    var showLatency by rememberSaveable { mutableStateOf(true) }

    val isLocked =
        state.connectionState == ConnectionState.CONNECTING ||
                state.connectionState == ConnectionState.CONNECTED

    val servers = remember(state.servers, sortMode) {
        when (sortMode) {
            SortMode.FASTEST -> state.servers.sortedBy { it.ping }
            SortMode.COUNTRY -> state.servers.sortedBy { it.country }
            SortMode.CITY -> state.servers.sortedBy { it.city }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Public, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Server locations")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF1B1630))
        ) {
            if (isLocked) {
                LockedBanner()
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(servers) { server ->
                    ServerItem(
                        server = server,
                        isSelected = server == state.selected,
                        showLatency = showLatency,
                        enabled = !isLocked,
                        onClick = {
                            onSelectServer(server)
                            onBack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LockedBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF33285C))
    ) {
        Text(
            text = "Disconnect VPN to change location",
            modifier = Modifier.padding(14.dp),
            color = Color.White
        )
    }
}
