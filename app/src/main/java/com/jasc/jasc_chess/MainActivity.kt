package com.jasc.jasc_chess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jasc.jasc_chess.game.BoardViewModel
import com.jasc.jasc_chess.game.GameScreen
import com.jasc.jasc_chess.menu.MainMenuScreen
import com.jasc.jasc_chess.ui.theme.Jasc_chessTheme
// En MainActivity.kt
import com.jasc.jasc_chess.game.ProfileScreen // IMPORT OBLIGATORIO
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Jasc_chessTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    // Instanciamos el ViewModel una sola vez aquí
                    val sharedViewModel: BoardViewModel = viewModel()

// Dentro de MainActivity.kt, en el bloque NavHost
                    NavHost(navController, startDestination = "menu") {
                        composable("menu") { MainMenuScreen(navController, sharedViewModel) }
                        composable("juego") { GameScreen(sharedViewModel) }
                        // AGREGA ESTA LÍNEA:
                        composable("perfil") { ProfileScreen(navController) }
                    }
                }
            }
        }
    }
}