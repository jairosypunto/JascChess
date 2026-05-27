            // =================================================================================
            // 2. TABLERO DE AJEDREZ (CORREGIDO: ALINEACIÓN COMPATIBLE CON IMÁGENES ROMANAS)
            // =================================================================================
            Box(
                modifier = Modifier
                    .padding(horizontal = 1.dp, vertical = 1.dp) // Margen exterior respecto a la pantalla
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .shadow(20.dp, RoundedCornerShape(6.dp))
                    .background(Color(0xFF4A2E1B))
                    .border(4.dp, Color(0xFF2D1B10), RoundedCornerShape(6.dp)) // Borde de madera
                    // AQUÍ AJUSTAS: "top" es el espacio entre el borde de madera y el juego
                    .padding(start = 4.dp, end = 4.dp, bottom = 4.dp, top = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // Coordenadas Verticales (Números del 8 al 1)
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(12.dp)
                            .padding(vertical = 4.dp), // Ligero ajuste para alinear con las filas reales
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for (i in 8 downTo 1) {
                            Text(
                                text = i.toString(),
                                color = Color(0xFFE2E8F0),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Contenedor principal: Tablero + Letras inferiores
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        // Tablero de casillas con borde unificado
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .border(1.5.dp, Color(0xFF1E1B4B))
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                for (row in 0..7) {
                                    Row(modifier = Modifier.weight(1f)) {
                                        for (col in 0..7) {
                                            val currentPos = Position(row, col)
                                            val isDarkCell = (row + col) % 2 == 1
                                            val piece = gameState.pieces.find { it.position == currentPos }
                                            val isValidMove = gameState.validMoves.contains(currentPos)
                                            val isSelected = gameState.selectedPosition == currentPos
                                            val esCasillaPista = gameState.casillaPista == currentPos

                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .fillMaxHeight()
                                                    .drawBehind {
                                                        // Líneas de textura sutiles según el color de la casilla
                                                        if (isDarkCell) {
                                                            drawLine(
                                                                color = Color(0x1F22D3EE),
                                                                start = Offset(0f, size.height * 0.3f),
                                                                end = Offset(size.width, size.height * 0.7f),
                                                                strokeWidth = 1.dp.toPx()
                                                            )
                                                        } else {
                                                            drawLine(
                                                                color = Color(0x2694A3B8),
                                                                start = Offset(size.width * 0.2f, 0f),
                                                                end = Offset(size.width * 0.8f, size.height),
                                                                strokeWidth = 1.2.dp.toPx()
                                                            )
                                                        }
                                                    }
                                                    .background(
                                                        when {
                                                            isSelected -> Color(0xB3F59E0B)
                                                            isValidMove -> Color(0xAA10B981)
                                                            esCasillaPista -> Color(0xCC7C3AED)
                                                            isDarkCell -> Color(0xFF1E3A8A)
                                                            else -> Color(0xFFF8FAFC)
                                                        }
                                                    )
                                                    .border(
                                                        0.3.dp,
                                                        if (isDarkCell) Color(0x1AFFFFFF) else Color(0x1F000000)
                                                    )
                                                    .clickable {
                                                        viewModel.onCellSelected(currentPos)
                                                    },
                                                contentAlignment = if (estiloSeleccionado == EstiloFichas.TRADICIONAL) {
                                                    Alignment.Center
                                                } else {
                                                    Alignment.BottomCenter
                                                }
                                            ) {
                                                piece?.let { currentPiece ->
                                                    Box(
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentAlignment = if (estiloSeleccionado == EstiloFichas.TRADICIONAL) {
                                                            Alignment.Center
                                                        } else {
                                                            Alignment.BottomCenter
                                                        }
                                                    ) {
                                                        val resId = obtenerResourcePieza(
                                                            currentPiece.type,
                                                            currentPiece.color,
                                                            estiloSeleccionado
                                                        )

                                                        if (resId != null) {
                                                            Image(
                                                                painter = painterResource(id = resId),
                                                                contentDescription = null,
                                                                contentScale = ContentScale.Inside,
                                                                modifier = Modifier
                                                                    .fillMaxSize()
                                                                    .graphicsLayer(
                                                                        // 1. ESCALADO EQUILIBRADO
                                                                        scaleX = when (estiloSeleccionado) {
                                                                            EstiloFichas.TRADICIONAL -> 1.2f
                                                                            EstiloFichas.ROMANO -> when (currentPiece.type) {
                                                                                PieceType.PEON -> 1.25f
                                                                                PieceType.TORRE -> 1.35f
                                                                                PieceType.CABALLO -> 1.40f
                                                                                PieceType.ALFIL -> 1.40f
                                                                                PieceType.REINA -> 1.45f
                                                                                PieceType.REY -> 1.50f
                                                                            }
                                                                            else -> 1.0f
                                                                        },
                                                                        scaleY = when (estiloSeleccionado) {
                                                                            EstiloFichas.TRADICIONAL -> 1.2f
                                                                            EstiloFichas.ROMANO -> when (currentPiece.type) {
                                                                                PieceType.PEON -> 1.25f
                                                                                PieceType.TORRE -> 1.35f
                                                                                PieceType.CABALLO -> 1.40f
                                                                                PieceType.ALFIL -> 1.40f
                                                                                PieceType.REINA -> 1.45f
                                                                                PieceType.REY -> 1.50f
                                                                            }
                                                                            else -> 1.0f
                                                                        },
                                                                        // 2. CONTROL DE AJUSTE VERTICAL INTELIGENTE
                                                                        translationY = when (estiloSeleccionado) {
                                                                            EstiloFichas.TRADICIONAL -> -10f
                                                                            EstiloFichas.ROMANO -> {
                                                                                if (currentPiece.color == PieceColor.PLATA) {
                                                                                    // Zona superior (Fichas Negras / Plata)
                                                                                    when (currentPiece.type) {
                                                                                        PieceType.PEON -> 0f
                                                                                        PieceType.TORRE -> -4f
                                                                                        PieceType.CABALLO -> -4f
                                                                                        PieceType.ALFIL -> -6f
                                                                                        PieceType.REINA -> -8f
                                                                                        PieceType.REY -> -8f
                                                                                    }
                                                                                } else {
                                                                                    // Zona inferior (Fichas Blancas / Oro)
                                                                                    when (currentPiece.type) {
                                                                                        PieceType.PEON -> -12f
                                                                                        PieceType.TORRE -> -18f
                                                                                        PieceType.CABALLO -> -20f
                                                                                        PieceType.ALFIL -> -22f
                                                                                        PieceType.REINA -> -25f
                                                                                        PieceType.REY -> -28f
                                                                                    }
                                                                                }
                                                                            }
                                                                            else -> 0f
                                                                        },
                                                                        // 3. ROTACIÓN EN PERSPECTIVA 3D (Cara a Cara)
                                                                        rotationY = if (estiloSeleccionado == EstiloFichas.ROMANO) {
                                                                            if (currentPiece.color == PieceColor.PLATA) 35f else -35f
                                                                        } else {
                                                                            0f
                                                                        }
                                                                    )
                                                            )
                                                        } else {
                                                            // Renderizado alternativo usando tipografía / texto (Ej: Egipcio o Gladiador)
                                                            val graphicPiece = obtenerSimboloTexto(
                                                                currentPiece.type,
                                                                estiloSeleccionado,
                                                                currentPiece.color
                                                            )
                                                            val pieceColorHex = if (currentPiece.color == PieceColor.ORO) {
                                                                if (estiloSeleccionado == EstiloFichas.EGIPCIO) colorEgipcioOro else colorGladiadorBronce
                                                            } else {
                                                                if (estiloSeleccionado == EstiloFichas.EGIPCIO) colorEgipcioNegro else colorGladiadorAcero
                                                            }

                                                            Text(
                                                                text = graphicPiece,
                                                                fontSize = 38.sp,
                                                                color = pieceColorHex,
                                                                fontWeight = FontWeight.Black,
                                                                modifier = Modifier.align(Alignment.Center)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Letras inferiores (Coordenadas de la 'a' a la 'h')
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(18.dp)
                                .padding(bottom = 2.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val letrasLetras = listOf("a", "b", "c", "d", "e", "f", "g", "h")
                            letrasLetras.forEach { letra ->
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = letra,
                                        color = Color(0xFFE2E8F0),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }