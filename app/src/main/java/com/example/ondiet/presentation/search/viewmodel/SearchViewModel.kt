package com.example.ondiet.presentation.search.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchNoteUseCase: SearchNoteUseCase,
    private val homeUseCase: HomeUseCase
) : ViewModel() {
    private val _inputState = mutableStateOf(TextState())
    val inputState: State<TextState> = _inputState

    private val _notes = mutableStateOf(emptyList<Note>())
    val notes: State<List<Note>> = _notes

    private val _loadingState = mutableStateOf(LoadingState())
    val loadingState: State<LoadingState> = _loadingState

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
                _inputState.value = _inputState.value.copy(
                    text = event.text
                )
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
                _loadingState.value = _loadingState.value.copy(isLoading = true)
                delay(1000L)
                _loadingState.value = _loadingState.value.copy(isLoading = false)
                searchNoteUseCase(_inputState.value.text.trimIndent()).collect {
                    _notes.value = it
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
