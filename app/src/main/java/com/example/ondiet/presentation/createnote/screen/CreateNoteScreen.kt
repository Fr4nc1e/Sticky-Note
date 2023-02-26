package com.example.ondiet.presentation.createnote.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ondiet.R
import com.example.ondiet.core.presentation.event.CoreUiEvent
import com.example.ondiet.core.presentation.util.asString
import com.example.ondiet.presentation.createnote.event.CreateNoteEvent
import com.example.ondiet.presentation.createnote.viewmodel.CreateNoteViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteScreen(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    onNavigateUp: () -> Unit = {},
    viewModel: CreateNoteViewModel = hiltViewModel()
) {
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val isLoading = viewModel.loadingState.collectAsState().value.isLoading

    LaunchedEffect(viewModel.showKeyBoardState) {
        focusRequester.requestFocus()
    }
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                CoreUiEvent.NavigateUp -> { onNavigateUp() }
                is CoreUiEvent.SnackBarEvent -> {
                    snackBarHostState.showSnackbar(event.uiText.asString(context))
                }
                else -> {}
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = viewModel.titleState.collectAsState().value.text,
                    onValueChange = { viewModel.onEvent(CreateNoteEvent.EnterTitle(it)) },
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.5.sp
                    ),
                    placeholder = {
                        Text(text = stringResource(id = R.string.title))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.surface,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.surface
                    )
                )
            }

            Divider(modifier = Modifier.fillMaxWidth())

            Box(modifier = Modifier.fillMaxSize()) {
                TextField(
                    value = viewModel.descriptionState.collectAsState().value.text,
                    onValueChange = {
                        viewModel.onEvent(CreateNoteEvent.EnterDescription(it))
                    },
                    modifier = Modifier.fillMaxSize(),
                    placeholder = {
                        Text(text = stringResource(id = R.string.description))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.surface,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }

        ExtendedFloatingActionButton(
            text = { Text(text = stringResource(R.string.complete)) },
            icon = {
                when {
                    isLoading -> {
                        CircularProgressIndicator()
                    }
                    else -> {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(id = R.string.complete)
                        )
                    }
                }
            },
            onClick = { viewModel.onEvent(CreateNoteEvent.Complete) },
            modifier = Modifier
                .align(BottomEnd)
                .padding(PaddingValues(end = 8.dp, bottom = 8.dp)),
            expanded = !isLoading
        )
    }
}
