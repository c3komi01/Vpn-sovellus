package com.example.vpnserverlist.data.repository

import com.example.vpnserverlist.data.mapper.toDomain
import com.example.vpnserverlist.data.model.VPNServer
import com.example.vpnserverlist.data.network.ConnectRequest
import com.example.vpnserverlist.data.network.VpnApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class VPNServerRepository(
    private val api: VpnApi
) {
    private val deviceId: String = UUID.randomUUID().toString()

    suspend fun loadServers(): List<VPNServer> = withContext(Dispatchers.IO) {
        api.getServers().map { it.toDomain() }
    }

    suspend fun requestConfig(serverId: Int): String = withContext(Dispatchers.IO) {
        val resp = api.connect(
            ConnectRequest(
                serverId = serverId.toString(),
                deviceId = deviceId
            )
        )
        resp.config
    }
}
