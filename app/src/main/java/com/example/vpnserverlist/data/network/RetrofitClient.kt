package com.example.vpnserverlist.data.network

import com.example.vpnserverlist.vpn.VpnState
import com.example.vpnserverlist.vpn.VpnStatus
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val PUBLIC_BASE_URL = "http://152.42.141.50:8090/"
    private const val VPN_BASE_URL = "http://10.8.0.1:8090/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    @Volatile
    private var retrofit: Retrofit? = null

    private fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun api(): VpnApi {
        val vpnConnected = VpnStatus.state.value == VpnState.CONNECTED
        val baseUrl = if (vpnConnected) VPN_BASE_URL else PUBLIC_BASE_URL

        val current = retrofit
        if (current == null || current.baseUrl().toString() != baseUrl) {
            retrofit = buildRetrofit(baseUrl)
        }

        return retrofit!!.create(VpnApi::class.java)
    }
}
