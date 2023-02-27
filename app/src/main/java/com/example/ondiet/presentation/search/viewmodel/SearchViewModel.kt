package com.example.ondiet.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ondiet.R
import com.example.ondiet.core.presentation.component.navigation.Screen
import com.example.ondiet.core.presentation.event.CoreUiEvent
import com.example.ondiet.core.presentation.state.LoadingState
import com.example.ondiet.core.presentation.state.TextState
import com.example.ondiet.core.presentation.util.UiText
import com.example.ondiet.domain.model.Note
import com.example.ondiet.presentation.home.usecase.HomeUseCase
import com.example.ondiet.presentation.search.event.SearchEvent
import com.example.ondiet.presentation.search.usecase.SearchNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchNoteUseCase: SearchNoteUseCase,
    private val homeUseCase: HomeUseCase
) : ViewModel() {
    private val _notes = MutableStateFlow(emptyList<Note>())
    val notes = _notes.asStateFlow()

    private val _inputState = MutableStateFlow(TextState())
    val inputState = _inputState.asStateFlow()

    private val _loadingState = MutableStateFlow(LoadingState())
    val loadingState = _loadingState.asStateFlow()

    private val _showKeyBoardState = MutableSharedFlow<Boolean>()
    val showKeyBoardState = _showKeyBoardState.asSharedFlow()

    private val _eventFlow = MutableSharedFlow<CoreUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            _showKeyBoardState.emit(true)
        }
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.Input -> {
                _inputState.update { TextState(event.text) }
            }
            SearchEvent.OnSearch -> {
                searchNote()
            }
            is SearchEvent.EnterDetailScreen -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        CoreUiEvent.Navigate(
                            Screen.NoteDetailScreen.route + "/${event.noteId.toHexString()}"
                        )
                    )
                }
            }

            is SearchEvent.DeleteNote -> {
                deleteNote(noteId = event.noteId)
            }
        }
    }

    private fun searchNote() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (_inputState.value.text.trimIndent().isBlank()) {
                _eventFlow.emit(
                    CoreUiEvent.SnackBarEvent(
                        uiText = UiText.StringResource(R.string.empty_query)
                    )
                )
            } else {
                _loadingState.update {
                    LoadingState(true)
                }
                delay(1000L)
                _loadingState.update {
                    LoadingState(false)
                }
                searchNoteUseCase(_inputState.value.text.trimIndent()).collect { result ->
                    _notes.update { result }
                }
                if (_notes.value.isEmpty()) {
                    _eventFlow.emit(
                        CoreUiEvent.SnackBarEvent(
                            uiText = UiText.StringResource(R.string.no_note_exists_with_the_title)
                        )
                    )
                }
            }
        }
    }

    private fun deleteNote(noteId: ObjectId) {
        viewModelScope.launch {
            homeUseCase.deleteNotes(noteId)
        }
    }
}
