package com.example.vpnserverlist

import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vpnserverlist.data.network.RetrofitClient
import com.example.vpnserverlist.data.repository.VPNServerRepository
import com.example.vpnserverlist.ui.navigation.AppNavGraph
import com.example.vpnserverlist.ui.theme.VPNServerListTheme
import com.example.vpnserverlist.ui.viewmodel.ServerViewModel
import com.example.vpnserverlist.vpn.VpnLauncher

class MainActivity : ComponentActivity() {

    private val vpnPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // результат нам не нужен — факт разрешения хранит система
        }

    private fun requestVpnPermission() {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            vpnPermissionLauncher.launch(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            VPNServerListTheme {

                val vm: ServerViewModel = viewModel {
                    ServerViewModel(
                        repo = VPNServerRepository( api = RetrofitClient.api())
                    )
                }

                val pendingConfig = vm.pendingConfig.collectAsState().value

                LaunchedEffect(pendingConfig) {
                    if (!pendingConfig.isNullOrBlank()) {
                        VpnLauncher.start(
                            context = this@MainActivity,
                            config = pendingConfig
                        )
                        vm.consumePendingConfig()
                    }
                }

                AppNavGraph(
                    vm = vm,
                    requestVpnPermission = { requestVpnPermission() }
                )
            }
        }
    }
}
