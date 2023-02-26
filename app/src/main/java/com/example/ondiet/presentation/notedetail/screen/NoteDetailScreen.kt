package com.example.ondiet.presentation.notedetail.screen

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ondiet.R
import com.example.ondiet.presentation.notedetail.event.NoteDetailEvent
import com.example.ondiet.presentation.notedetail.viewmodel.NoteDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: NoteDetailViewModel = hiltViewModel()
) {
    val isLoading = viewModel.loadingState.collectAsState().value.isLoading
    val readOnly = viewModel.onModify.collectAsState().value

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
                    onValueChange = { viewModel.onEvent(NoteDetailEvent.EnterTitle(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = readOnly,
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

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                TextField(
                    value = viewModel.descriptionState.collectAsState().value.text,
                    onValueChange = {
                        viewModel.onEvent(NoteDetailEvent.EnterDescription(it))
                    },
                    modifier = Modifier.fillMaxSize(),
                    readOnly = readOnly,
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
            text = {
                if (readOnly) {
                    Text(text = stringResource(R.string.modify))
                } else {
                    Text(text = stringResource(R.string.complete))
                }
            },
            icon = {
                when {
                    isLoading -> {
                        CircularProgressIndicator()
                    }
                    else -> {
                        if (readOnly) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = stringResource(id = R.string.modify)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(id = R.string.complete)
                            )
                        }
                    }
                }
            },
            onClick = {
                if (viewModel.onModify.value) {
                    viewModel.onEvent(NoteDetailEvent.ModifyClicked)
                } else {
                    viewModel.onEvent(NoteDetailEvent.ModifyCompleted)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(PaddingValues(end = 8.dp, bottom = 8.dp)),
            expanded = !isLoading
        )
    }
}
