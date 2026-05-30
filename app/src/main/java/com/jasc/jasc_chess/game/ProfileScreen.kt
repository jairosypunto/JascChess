package com.jasc.jasc_chess.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jasc.jasc_chess.R

@Composable
fun ProfileScreen(navController: NavController) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // --- CABECERA ---
        TextButton(onClick = { navController.popBackStack() }) {
            Text("← Volver al Menú", color = Color(0xFF8B5A2B))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- AVATAR Y NOMBRE ---
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF8B5A2B)),
                contentAlignment = Alignment.Center
            ) {
                // Imagen de perfil usando logojasc
                Image(
                    painter = painterResource(id = R.drawable.perfil),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Jairo Salazar Castaño", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                text = "Ingeniero en Sistemas | Apasionado por el código y la estrategia",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- ESTADÍSTICAS ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Puntaje", "120", Modifier.weight(1f))
            StatCard("Partidas", "45", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- APOYO ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Coffee, contentDescription = null, tint = Color(0xFF6F4E37))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Invítame un café", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Nequi:", fontWeight = FontWeight.Bold)
                Text("301 617 3378", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color(0xFF8B5A2B))
                Button(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B)),
                    onClick = { clipboardManager.setText(AnnotatedString("3016173378")) }
                ) {
                    Text("Copiar Nequi")
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color.LightGray, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(16.dp))

                Text("Bancolombia (Ahorros):", fontWeight = FontWeight.Bold)
                Text("653-474295-16", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color(0xFF8B5A2B))
                Button(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006633)),
                    onClick = { clipboardManager.setText(AnnotatedString("65347429516")) }
                ) {
                    Text("Copiar Cuenta Bancolombia")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- PIE DE PÁGINA CORREGIDO ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Jasc Chess Pro © 2026", color = Color.Gray.copy(alpha = 0.8f), fontSize = 12.sp)
            Text("ingenierojasc@gmail.com", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF8B5A2B))
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFF1C40F))
            Text(value, fontWeight = FontWeight.Black, fontSize = 20.sp)
            Text(title, fontSize = 12.sp, color = Color.Gray)
        }
    }
}