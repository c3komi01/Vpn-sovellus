package com.example.vpnserverlist.vpn

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.vpnserverlist.R
import com.wireguard.android.backend.Backend
import com.wireguard.android.backend.GoBackend
import com.wireguard.android.backend.Tunnel
import com.wireguard.config.Config
import java.io.BufferedReader
import java.io.StringReader

class WireGuardVpnService : VpnService() {

    companion object {
        const val ACTION_CONNECT = "com.example.vpnserverlist.CONNECT"
        const val ACTION_DISCONNECT = "com.example.vpnserverlist.DISCONNECT"
        const val EXTRA_CONFIG = "WG_CONFIG"

        private const val CHANNEL_ID = "vpn_channel"
        private const val NOTIFICATION_ID = 42
    }

    private lateinit var backend: Backend
    private var tunnel: Tunnel? = null

    override fun onCreate() {
        super.onCreate()
        backend = GoBackend(this)
        ensureForeground()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_CONNECT -> {
                val configText = intent.getStringExtra(EXTRA_CONFIG)
                if (!configText.isNullOrBlank()) {
                    VpnStatus.setConnecting()
                    connect(configText)
                }
            }

            ACTION_DISCONNECT -> {
                disconnect()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun connect(configText: String) {
        try {
            val config = Config.parse(
                BufferedReader(StringReader(configText))
            )

            val t = tunnel ?: object : Tunnel {
                override fun getName() = "wg-tunnel"
                override fun onStateChange(newState: Tunnel.State) {}
            }.also { tunnel = it }

            backend.setState(t, Tunnel.State.UP, config)
            VpnStatus.setConnected()

        } catch (e: Exception) {
            VpnStatus.setError(e.message ?: "WireGuard connection failed")
            stopSelf()
        }
    }

    private fun disconnect() {
        try {
            tunnel?.let {
                backend.setState(it, Tunnel.State.DOWN, null)
            }
        } catch (_: Exception) {
            // игнорируем, главное — корректно закрыть сервис
        } finally {
            VpnStatus.setDisconnected()
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    private fun ensureForeground() {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "VPN",
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }

        val notification: Notification =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("VPN active")
                .setContentText("WireGuard tunnel running")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .build()

        startForeground(NOTIFICATION_ID, notification)
    }
}
