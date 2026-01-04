package com.example.vpnserverlist.data.network

import com.google.gson.annotations.SerializedName

data class ConnectResponse(
    val message: String?,
    val peerId: Int?,
    val peerPublicKey: String?,
    val clientAddress: String?,
    val endpoint: String?,

    // Backend возвращает поле "config"
    @SerializedName("config")
    val config: String
)