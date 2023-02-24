package com.example.ondiet.presentation.home.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ondiet.core.presentation.component.navigation.Screen
import com.example.ondiet.presentation.home.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit = {},
    viewmodel: HomeViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier
    ) {
        Button(onClick = { onNavigate(Screen.NoteDetailScreen.route) }) {
            Text(text = "detail")
        }
    }
}
