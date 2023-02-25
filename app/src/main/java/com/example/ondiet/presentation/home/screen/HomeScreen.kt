package com.example.ondiet.presentation.home.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ondiet.core.presentation.event.CoreUiEvent
import com.example.ondiet.presentation.home.event.HomeEvent
import com.example.ondiet.presentation.home.screen.component.NoteCard
import com.example.ondiet.presentation.home.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CoreUiEvent.Navigate -> {
                    onNavigate(event.route)
                }
                is CoreUiEvent.SnackBarEvent -> {}
                else -> {}
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(viewModel.notes.value) {
            NoteCard(
                note = it,
                onNoteCardClick = {
                    viewModel.onEvent(HomeEvent.EnterDetailScreen(noteId = it._id))
                },
                onDeleteClick = {
                    viewModel.onEvent(HomeEvent.DeleteNote(noteId = it._id))
                }
            )
        }
    }
}
