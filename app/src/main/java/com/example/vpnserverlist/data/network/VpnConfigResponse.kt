package com.example.vpnserverlist.data.network

import com.google.gson.annotations.SerializedName

data class VpnConfigResponse(

    @SerializedName("config")
    val wgConfig: String
)
