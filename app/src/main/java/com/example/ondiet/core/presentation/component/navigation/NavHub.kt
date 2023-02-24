package com.example.ondiet.core.presentation.component.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ondiet.presentation.createnote.CreateNoteScreen
import com.example.ondiet.presentation.home.screen.HomeScreen
import com.example.ondiet.presentation.notedetail.NoteDetailScreen

@Composable
fun NavHub(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(
                modifier = modifier,
                onNavigate = navController::navigate
            )
        }
        composable(Screen.NoteDetailScreen.route) {
            NoteDetailScreen()
        }
        composable(Screen.CreateNoteScreen.route) {
            CreateNoteScreen()
        }
    }
}
