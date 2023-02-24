package com.example.ondiet.core.presentation.component.navigation

sealed class Screen(val route: String) {
    object HomeScreen : Screen(route = "home_screen")
    object NoteDetailScreen : Screen(route = "note_detail_screen")
    object CreateNoteScreen : Screen(route = "create_note_screen")
}
