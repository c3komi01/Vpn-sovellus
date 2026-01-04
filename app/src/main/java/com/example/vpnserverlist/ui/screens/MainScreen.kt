package com.example.vpnserverlist.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.vpnserverlist.data.model.VPNServer
import com.example.vpnserverlist.ui.state.ConnectionState
import com.example.vpnserverlist.ui.state.UiState

@Composable
fun MainScreen(
    uiState: UiState,
    onRequestPermission: () -> Unit,
    onRefresh: () -> Unit,
    onSelectServer: (VPNServer) -> Unit,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit
) {
    when (uiState) {
        is UiState.Loading -> LoadingScreen()

        is UiState.Error -> ErrorScreen(
            message = uiState.message,
            onRetry = onRefresh
        )

        is UiState.Content -> MainContent(
            state = uiState,
            onRequestPermission = onRequestPermission,
            onRefresh = onRefresh,
            onSelectServer = onSelectServer,
            onConnect = onConnect,
            onDisconnect = onDisconnect
        )
    }
}

@Composable
private fun MainContent(
    state: UiState.Content,
    onRequestPermission: () -> Unit,
    onRefresh: () -> Unit,
    onSelectServer: (VPNServer) -> Unit,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit
) {
    val pulse = rememberInfiniteTransition(label = "pulse")
    val scale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = if (state.connectionState == ConnectionState.CONNECTING) 1.06f else 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val bg = Color(0xFF0D0B16)
    val card = Color(0xFF17122A)
    val accent = Color(0xFF7C5CFF)

    val selectedTitle =
        state.selected?.let { "${it.country} / ${it.city}" } ?: "Select a server"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(16.dp)
    ) {

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Servers: ${state.servers.size}", color = Color.White)
            TextButton(onClick = onRefresh) { Text("Refresh") }
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "VPN state: ${state.connectionState}",
            color = Color.White.copy(alpha = 0.85f)
        )

        Spacer(Modifier.height(18.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = card),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(14.dp)) {
                Text(selectedTitle, color = Color.White)
                Spacer(Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = onRequestPermission) {
                        Text("Request VPN permission")
                    }
                    OutlinedButton(onClick = onDisconnect) {
                        Text("Disconnect")
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentAlignment = Alignment.Center
        ) {
            val isBusy = state.connectionState == ConnectionState.CONNECTING
            val canConnect =
                state.selected != null && state.connectionState != ConnectionState.CONNECTED

            Surface(
                shape = CircleShape,
                color = if (state.connectionState == ConnectionState.CONNECTED)
                    Color(0xFF25D366) else accent,
                modifier = Modifier
                    .size(150.dp)
                    .scale(if (isBusy) scale else 1f)
                    .clickable(enabled = canConnect) { onConnect() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = when (state.connectionState) {
                            ConnectionState.DISCONNECTED -> "CONNECT"
                            ConnectionState.CONNECTING -> "CONNECTING"
                            ConnectionState.CONNECTED -> "CONNECTED"
                        },
                        color = Color.White
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        Text("Tap a server:", color = Color.White.copy(alpha = 0.85f))
        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(state.servers) { server ->
                ServerRow(
                    server = server,
                    selected = state.selected?.id == server.id,
                    onClick = { onSelectServer(server) }
                )
            }
        }
    }
}

@Composable
private fun ServerRow(
    server: VPNServer,
    selected: Boolean,
    onClick: () -> Unit
) {
    val cardColor = if (selected) Color(0xFF2B2350) else Color(0xFF17122A)

    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("${server.country} / ${server.city}", color = Color.White)
                Text(
                    "Ping: ${server.ping} ms",
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            if (server.isPremium) {
                Text("Premium", color = Color(0xFFFFD54F))
            }
        }
    }
}
