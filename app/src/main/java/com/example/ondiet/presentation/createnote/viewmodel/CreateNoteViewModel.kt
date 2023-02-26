package com.example.ondiet.presentation.createnote.viewmodel

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val createNoteUseCase: CreateNoteUseCase
) : ViewModel() {
    private val _titleState = MutableStateFlow(TextState())
    val titleState = _titleState.asStateFlow()

    private val _descriptionState = MutableStateFlow(TextState())
    val descriptionState = _descriptionState.asStateFlow()

    private val _loadingState = MutableStateFlow(LoadingState())
    val loadingState = _loadingState.asStateFlow()

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
                _descriptionState.value = TextState(text = event.text)
            }
            is CreateNoteEvent.EnterTitle -> {
                _titleState.value = TextState(text = event.text)
            }
        }
    }

    private fun complete() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.value = LoadingState(isLoading = true)
            createNoteUseCase(
                note = Note().apply {
                    title = _titleState.value.text
                    description = _descriptionState.value.text
                }
            )
            delay(1000L)
            _loadingState.value = LoadingState(isLoading = false)
            _eventFlow.emit(CoreUiEvent.NavigateUp)
        }
    }
}
