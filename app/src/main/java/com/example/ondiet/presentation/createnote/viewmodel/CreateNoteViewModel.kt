package com.example.ondiet.presentation.createnote.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ondiet.R
import com.example.ondiet.core.presentation.event.CoreUiEvent
import com.example.ondiet.core.presentation.state.LoadingState
import com.example.ondiet.core.presentation.state.TextState
import com.example.ondiet.core.presentation.util.UiText
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

    private val _showKeyBoardState = MutableSharedFlow<Boolean>()
    val showKeyBoardState = _showKeyBoardState.asSharedFlow()

    private val _eventFlow = MutableSharedFlow<CoreUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            _showKeyBoardState.emit(true)
        }
    }

    fun onEvent(event: CreateNoteEvent) {
        when (event) {
            CreateNoteEvent.Complete -> {
                if (_titleState.value.text.isNotBlank() &&
                    _descriptionState.value.text.isNotBlank()
                ) { complete() } else {
                    viewModelScope.launch {
                        _eventFlow.emit(
                            CoreUiEvent.SnackBarEvent(
                                uiText = UiText.StringResource(R.string.blank_tilte_or_description)
                            )
                        )
                    }
                }
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
                    title = _titleState.value.text
                    description = _descriptionState.value.text
                }
            )
            delay(500L)
            _loadingState.value = _loadingState.value.copy(
                isLoading = false
            )
            _eventFlow.emit(CoreUiEvent.NavigateUp)
        }
    }
}
