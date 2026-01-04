package com.example.vpnserverlist.ui.screens



import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onDone: () -> Unit) {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, tween(600))
        delay(700)
        onDone()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0B16)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "VPN",
                fontSize = 44.sp,
                fontWeight = FontWeight.Black,
                color = Color.White.copy(alpha = alpha.value)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "WireGuard secure tunnel",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = alpha.value * 0.85f)
            )
        }
    }
}
