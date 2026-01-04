package com.example.vpnserverlist.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun BottomBar(
    onHome: () -> Unit,
    onLocations: () -> Unit,
    onSettings: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = onHome,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onLocations,
            icon = { Icon(Icons.Default.Public, contentDescription = "Locations") },
            label = { Text("Locations") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onSettings,
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") }
        )
    }
}
