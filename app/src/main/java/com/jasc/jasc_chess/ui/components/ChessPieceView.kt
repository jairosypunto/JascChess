package com.jasc.jasc_chess.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jasc.jasc_chess.model.ChessPiece
import com.jasc.jasc_chess.model.PieceColor

@Composable
fun ChessPieceView(
    piece: ChessPiece,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = piece.type.name.take(2),
            color = if (piece.color == PieceColor.ORO) Color(0xFFD4AF37) else Color(0xFFC0C0C0),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}