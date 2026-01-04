package com.example.vpnserverlist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vpnserverlist.ui.screens.MainScreen
import com.example.vpnserverlist.ui.screens.SplashScreen
import com.example.vpnserverlist.ui.viewmodel.ServerViewModel
import com.example.vpnserverlist.vpn.VpnLauncher

@Composable
fun AppNavGraph(
    vm: ServerViewModel,
    requestVpnPermission: () -> Unit
) {
    val navController = rememberNavController()
    val context = LocalContext.current   // ← ВОТ ЭТОГО НЕ ХВАТАЛО

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onDone = {
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            val uiState by vm.uiState.collectAsState()

            MainScreen(
                uiState = uiState,
                onRequestPermission = requestVpnPermission,
                onRefresh = { vm.refreshServers() },
                onSelectServer = { vm.selectServer(it) },
                onConnect = { vm.connectSelected() },
                onDisconnect = {
                    VpnLauncher.stop(context)
                }
            )
        }
    }
}
