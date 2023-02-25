package com.example.ondiet.presentation.createnote.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ondiet.core.presentation.event.UiEvent
import com.example.ondiet.core.presentation.state.LoadingState
import com.example.ondiet.core.presentation.state.TextState
import com.example.ondiet.domain.model.Note
import com.example.ondiet.presentation.createnote.event.CreateNoteEvent
import com.example.ondiet.presentation.createnote.usecase.CreateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val createNoteUseCase: CreateNoteUseCase
) : ViewModel() {
    private val _titleState = mutableStateOf(TextState())
    val titleState: State<TextState> = _titleState

    private val _descriptionState = mutableStateOf(TextState())
    val descriptionState: State<TextState> = _descriptionState

    private val _loadingState = mutableStateOf(LoadingState())
    val loadingState: State<LoadingState> = _loadingState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: CreateNoteEvent) {
        when (event) {
            CreateNoteEvent.Complete -> {
                complete()
            }
            is CreateNoteEvent.EnterDescription -> {
                _descriptionState.value = _descriptionState.value.copy(
                    text = event.text
                )
            }
            is CreateNoteEvent.EnterTitle -> {
                _titleState.value = _titleState.value.copy(
                    text = event.text
                )
            }
        }
    }

    private fun complete() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.value = _loadingState.value.copy(
                isLoading = true
            )
            createNoteUseCase(
                note = Note().apply {
                    if (_titleState.value.text.isNotEmpty()) {
                        title = _titleState.value.text
                    }
                    if (_descriptionState.value.text.isNotEmpty()) {
                        description = _descriptionState.value.text
                    }
                }
            )
            _titleState.value = TextState()
            _descriptionState.value = TextState()
            delay(500L)
            _loadingState.value = _loadingState.value.copy(
                isLoading = false
            )
            _eventFlow.emit(UiEvent.NavigateUp)
        }
    }
}
