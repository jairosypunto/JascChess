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
import com.jasc.jasc_chess.game.ProfileScreen
import com.jasc.jasc_chess.history.HistoryScreen // <-- ESTE IMPORT FALTABA
import com.jasc.jasc_chess.menu.MainMenuScreen
import com.jasc.jasc_chess.ui.theme.Jasc_chessTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Jasc_chessTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val sharedViewModel: BoardViewModel = viewModel()

                    NavHost(navController, startDestination = "menu") {
                        composable("menu") { MainMenuScreen(navController, sharedViewModel) }
                        composable("juego") { GameScreen(sharedViewModel) }
                        composable("perfil") { ProfileScreen(navController) }
                        composable("history_screen") { HistoryScreen(navController) }
                    }
                }
            }
        }
    }
}