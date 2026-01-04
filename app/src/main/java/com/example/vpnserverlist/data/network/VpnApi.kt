package com.example.vpnserverlist.data.network

import com.example.vpnserverlist.data.network.ConnectRequest
import com.example.vpnserverlist.data.network.ConnectResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface VpnApi {

    // На твоем backend реально /serverss (ты сам проверял curl)
    @GET("servers")
    suspend fun getServers(): List<VPNServerDto>

    // На твоем backend реально /vpn/connect (ты сам проверял curl)
    @POST("vpn/connect")
    suspend fun connect(@Body request: ConnectRequest): ConnectResponse
}

