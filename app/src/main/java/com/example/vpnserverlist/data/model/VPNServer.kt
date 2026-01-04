package com.example.vpnserverlist.data.model

data class VPNServer(
    val id: Int,
    val country: String,
    val city: String,
    val ping: Int,
    val isPremium: Boolean
)
