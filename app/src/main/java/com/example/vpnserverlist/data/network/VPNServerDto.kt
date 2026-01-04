package com.example.vpnserverlist.data.network

data class VPNServerDto(
    val id: Int,
    val country: String?,
    val city: String?,
    val ping: Int?,
    val isPremium: Boolean? = false
)
