package com.example.vpnserverlist.ui.util



import com.example.vpnserverlist.R

fun flagForCountry(country: String): Int {
    return when (country.lowercase()) {
        "finland" -> R.drawable.fi
        "germany" -> R.drawable.de
        "usa", "united states" -> R.drawable.ic_launcher_foreground
        "netherlands", "holland" -> R.drawable.amsterdam_flag
        else -> R.drawable.ic_launcher_foreground
    }
}
