package com.example.vpnserverlist.data.mapper

import com.example.vpnserverlist.data.model.VPNServer
import com.example.vpnserverlist.data.network.VPNServerDto

fun VPNServerDto.toDomain(): VPNServer =
    VPNServer(
        id = id,
        country = country ?: "Unknown",
        city = city ?: "Unknown",
        ping = ping ?: -1,
        isPremium = isPremium ?: false
    )
