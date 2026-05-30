package com.jasc.jasc_chess.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jasc.jasc_chess.game.GameState
import com.jasc.jasc_chess.model.PieceColor
import com.jasc.jasc_chess.model.Position
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChessBoardView(
    gameStateFlow: StateFlow<GameState>,
    onCellClick: (Position) -> Unit,
    modifier: Modifier = Modifier
) {
    val gameState by gameStateFlow.collectAsState()

    // Definición de colores
    val colorOro = Color(0xFFD4AF37)
    val colorPlata = Color(0xFFC0C0C0)
    val casillaClara = Color(0xFFF5F5DC)
    val casillaOscura = Color(0xFF2C2C2C)
    val seleccion = Color(0xFFEFE5DA)
    val movimientoVal = Color(0xFF3A5F41)

    // Contenedor principal del tablero
    Box(
        modifier = modifier
            .padding(16.dp)
            .aspectRatio(1f) // Mantiene la forma cuadrada
            .shadow(12.dp, shape = RoundedCornerShape(16.dp))
            .background(Color(0xFF8C8C8C))
            .border(8.dp, Color(0xFF595959), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(4.dp))
        ) {
            // Generación dinámica del tablero 8x8
            for (row in 0 until gameState.boardSize) { // Usamos gameState.boardSize para flexibilidad
                Row(modifier = Modifier.weight(1f)) {
                    for (col in 0 until gameState.boardSize) {
                        val currentPos = Position(row, col)
                        val isDarkCell = (row + col) % 2 == 1
                        val piece = gameState.pieces.find { it.position == currentPos }
                        val isValidMove = gameState.validMoves.contains(currentPos)
                        val isSelected = gameState.selectedPosition == currentPos

                        val cellBackground = when {
                            isSelected -> seleccion
                            isValidMove -> movimientoVal
                            isDarkCell -> casillaOscura
                            else -> casillaClara
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(cellBackground)
                                .border(0.5.dp, Color(0x22FFFFFF))
                                .combinedClickable(onClick = { onCellClick(currentPos) }),
                            contentAlignment = Alignment.Center
                        ) {
                            piece?.let { p ->
                                val pieceDisplayColor = if (p.color == PieceColor.ORO) colorOro else colorPlata
                                Text(
                                    text = p.type.name.take(2),
                                    color = pieceDisplayColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}