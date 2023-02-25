package com.example.ondiet.presentation.search.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ondiet.core.presentation.event.CoreUiEvent
import com.example.ondiet.core.presentation.util.asString
import com.example.ondiet.presentation.home.screen.component.NoteCard
import com.example.ondiet.presentation.search.event.SearchEvent
import com.example.ondiet.presentation.search.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    onNavigate: (String) -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(viewModel.showKeyBoardState) {
        focusRequester.requestFocus()
    }
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CoreUiEvent.Navigate -> {
                    onNavigate(event.route)
                }
                is CoreUiEvent.SnackBarEvent -> {
                    snackBarHostState.showSnackbar(event.uiText.asString(context))
                }
                else -> {}
            }
        }
    }

    Box(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            OutlinedTextField(
                value = viewModel.inputState.value.text,
                onValueChange = {
                    viewModel.onEvent(SearchEvent.Input(it))
                },
                modifier = Modifier.focusRequester(focusRequester).fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { viewModel.onEvent(SearchEvent.OnSearch) }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "")
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.onEvent(SearchEvent.OnSearch)
                }),
                shape = RoundedCornerShape(16.dp)
            )
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
                            viewModel.onEvent(SearchEvent.EnterDetailScreen(noteId = it._id))
                        },
                        onDeleteClick = {
                            viewModel.onEvent(SearchEvent.DeleteNote(noteId = it._id))
                        }
                    )
                }
            }
        }

        if (viewModel.loadingState.value.isLoading) {
            CircularProgressIndicator(modifier.align(Alignment.Center))
        }
    }
}
