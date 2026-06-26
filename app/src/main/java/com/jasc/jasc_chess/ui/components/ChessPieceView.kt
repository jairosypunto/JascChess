package com.jasc.jasc_chess.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.jasc.jasc_chess.R
import com.jasc.jasc_chess.model.ChessPiece
import com.jasc.jasc_chess.model.PieceColor
import com.jasc.jasc_chess.model.PieceType

@Composable
fun ChessPieceView(
    piece: ChessPiece,
    isSelected: Boolean,
    resId: Int?,
    modifier: Modifier = Modifier,
    esJaqueMate: Boolean = false,
    colorGanador: PieceColor? = null
) {
    val esReyCaido = (piece.type == PieceType.REY && esJaqueMate && piece.color != colorGanador)

    val esReyTradicional = piece.type == PieceType.REY &&
            (resId == R.drawable.tradicional_rey_blanco || resId == R.drawable.tradicional_rey_negro)

    val baseScale = if (esReyTradicional) 1.1f else 1.0f

    val scale by animateFloatAsState(
        targetValue = (if (isSelected) 1.8f else if (esReyCaido) 1.2f else 1.0f) * baseScale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    val rotation by animateFloatAsState(
        targetValue = if (esReyCaido) 90f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "rotation"
    )

    val transformModifier = Modifier
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            rotationZ = rotation,
            transformOrigin = TransformOrigin(0.5f, 0.5f)
        )
        .zIndex(if (isSelected) 10f else if (esReyCaido) 5f else 1f)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (resId != null) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize().then(transformModifier)
            )
        } else {
            // CORRECCIÓN: Se eliminó la variable 'simbolo' y se usa un fallback lógico
            Text(
                text = if (esReyCaido) "😵" else piece.type.name.first().toString(),
                color = if (piece.color == PieceColor.ORO) Color(0xFFD4AF37) else Color(0xFFC0C0C0),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier
                    .fillMaxSize()
                    .then(transformModifier)
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}