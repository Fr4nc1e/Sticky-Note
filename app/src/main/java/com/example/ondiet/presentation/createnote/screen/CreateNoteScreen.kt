package com.example.ondiet.presentation.createnote.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ondiet.R
import com.example.ondiet.core.presentation.event.UiEvent
import com.example.ondiet.presentation.createnote.event.CreateNoteEvent
import com.example.ondiet.presentation.createnote.viewmodel.CreateNoteViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    viewModel: CreateNoteViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                UiEvent.NavigateUp -> {
                    onNavigateUp()
                }
                else -> {}
            }
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        if (viewModel.loadingState.value.isLoading) {
            CircularProgressIndicator(modifier.align(Center))
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .wrapContentSize(Center)
                    .padding(8.dp)
            ) {
                TextField(
                    value = viewModel.titleState.value.text,
                    onValueChange = {
                        if (it.length < 20) {
                            viewModel.onEvent(CreateNoteEvent.EnterTitle(it))
                        }
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.title))
                    }
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                TextField(
                    value = viewModel.descriptionState.value.text,
                    onValueChange = {
                        viewModel.onEvent(CreateNoteEvent.EnterDescription(it))
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.description))
                    }
                )
            }

            Button(onClick = { viewModel.onEvent(CreateNoteEvent.Complete) }) {
                Text(text = stringResource(R.string.complete))
            }
        }
    }
}
