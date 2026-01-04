package com.example.vpnserverlist.vpn

import android.content.Context
import android.content.Intent

object VpnLauncher {

    fun start(context: Context, config: String) {
        val intent = Intent(context, WireGuardVpnService::class.java).apply {
            action = WireGuardVpnService.ACTION_CONNECT
            putExtra(WireGuardVpnService.EXTRA_CONFIG, config)
        }
        context.startService(intent)
    }

    fun stop(context: Context) {
        val intent = Intent(context, WireGuardVpnService::class.java).apply {
            action = WireGuardVpnService.ACTION_DISCONNECT
        }
        context.startService(intent)
    }
}
