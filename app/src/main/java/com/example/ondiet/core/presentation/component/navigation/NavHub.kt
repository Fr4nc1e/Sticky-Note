package com.example.ondiet.core.presentation.component.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ondiet.presentation.createnote.screen.CreateNoteScreen
import com.example.ondiet.presentation.home.screen.HomeScreen
import com.example.ondiet.presentation.notedetail.NoteDetailScreen
import com.example.ondiet.presentation.search.screen.SearchScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavHub(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackBarHostState: SnackbarHostState
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
        composable(
            route = Screen.NoteDetailScreen.route + "/{noteId}",
            arguments = listOf(
                navArgument(
                    name = "noteId"
                ) {
                    type = NavType.StringType
                }
            )
        ) {
            NoteDetailScreen(modifier = modifier)
        }
        composable(Screen.CreateNoteScreen.route) {
            CreateNoteScreen(
                modifier = modifier,
                snackBarHostState = snackBarHostState,
                onNavigateUp = navController::navigateUp
            )
        }
        composable(Screen.SearchScreen.route) {
            SearchScreen(
                modifier = modifier,
                snackBarHostState = snackBarHostState
            )
        }
    }
}
