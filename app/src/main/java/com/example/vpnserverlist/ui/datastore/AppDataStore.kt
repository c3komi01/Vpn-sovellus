package com.example.vpnserverlist.ui.datastore


import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "vpn_prefs")

class AppDataStore(private val context: Context) {

    companion object {
        val SELECTED_SERVER_ID = intPreferencesKey("selected_server_id")
        val IS_CONNECTED = booleanPreferencesKey("is_connected")
    }

    val selectedServerId: Flow<Int?> =
        context.dataStore.data.map { prefs ->
            prefs[SELECTED_SERVER_ID]
        }

    val isConnected: Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[IS_CONNECTED] ?: false
        }

    suspend fun saveSelectedServer(id: Int) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_SERVER_ID] = id
        }
    }

    suspend fun saveConnectionState(connected: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_CONNECTED] = connected
        }
    }
}
