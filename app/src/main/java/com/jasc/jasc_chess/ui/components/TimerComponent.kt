package com.jasc.jasc_chess.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun TimerComponent(
    oroTimeMillis: Long,
    plataTimeMillis: Long,
    isOroTurn: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Reloj del Jugador ORO
        TimeBox(
            label = "ORO",
            timeMillis = oroTimeMillis,
            isActive = isOroTurn,
            activeColor = Color(0xFFD4AF37) // Color Oro Premium
        )

        // Reloj del Jugador PLATA
        TimeBox(
            label = "PLATA",
            timeMillis = plataTimeMillis,
            isActive = !isOroTurn,
            activeColor = Color(0xFFC0C0C0) // Color Plata Premium
        )
    }
}

@Composable
fun TimeBox(
    label: String,
    timeMillis: Long,
    isActive: Boolean,
    activeColor: Color
) {
    val totalSeconds = timeMillis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val timeString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(140.dp)
            .background(
                color = if (isActive) Color(0x33000000) else Color(0x1A000000),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (isActive) 2.dp else 1.dp,
                color = if (isActive) activeColor else Color.Gray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isActive) activeColor else Color.Gray
        )
        Text(
            text = timeString,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = if (isActive) Color.White else Color.LightGray
        )
    }
}