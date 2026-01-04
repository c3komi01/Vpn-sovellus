package com.example.vpnserverlist.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.vpnserverlist.data.model.VPNServer
import com.example.vpnserverlist.ui.util.flagForCountry

@Composable
fun ServerItem(
    server: VPNServer,
    isSelected: Boolean,
    showLatency: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.015f else 1f,
        label = "scale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected)
            Color(0xFF3B2F6B)
        else
            Color(0xFF2A2345),
        label = "bg"
    )

    val alpha = if (enabled) 1f else 0.5f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .alpha(alpha)
            .animateContentSize()
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            runCatching { flagForCountry(server.country) }.getOrNull()?.let { res ->
                Image(
                    painter = painterResource(res),
                    contentDescription = server.country,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(Modifier.width(14.dp))
            }

            Column(Modifier.weight(1f)) {
                Text(
                    server.country,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
                Text(
                    text = if (showLatency)
                        "${server.city} • ${server.ping} ms"
                    else
                        server.city,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB7B1CC)
                )
            }

            if (showLatency) {
                PingBadge(server.ping)
            }
        }
    }
}

@Composable
private fun PingBadge(ping: Int) {
    val baseColor = when {
        ping < 50 -> Color(0xFF4CAF50)
        ping < 100 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    val color by animateColorAsState(baseColor, label = "pingColor")

    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.18f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text("$ping ms", color = color, style = MaterialTheme.typography.labelMedium)
    }
}
