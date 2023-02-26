package com.example.ondiet.presentation.notedetail.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ondiet.core.presentation.state.LoadingState
import com.example.ondiet.core.presentation.state.TextState
import com.example.ondiet.domain.model.Note
import com.example.ondiet.presentation.notedetail.event.NoteDetailEvent
import com.example.ondiet.presentation.notedetail.usecase.NoteDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val noteDetailUseCase: NoteDetailUseCase
) : ViewModel() {

    private val _note = mutableStateOf(Note())
    val note: State<Note> = _note

    private val _titleState = mutableStateOf(TextState())
    val titleState: State<TextState> = _titleState

    private val _descriptionState = mutableStateOf(TextState())
    val descriptionState: State<TextState> = _descriptionState

    private val _loadingState = mutableStateOf(LoadingState())
    val loadingState: State<LoadingState> = _loadingState

    private val _onModify = mutableStateOf(true)
    val onModify: State<Boolean> = _onModify

    init {
        savedStateHandle.get<String>("noteId")?.let { noteId ->
            getNote(ObjectId(hexString = noteId))
        }
    }

    fun onEvent(event: NoteDetailEvent) {
        when (event) {
            is NoteDetailEvent.EnterDescription -> {
                _descriptionState.value = _descriptionState.value.copy(
                    text = event.text
                )
            }
            is NoteDetailEvent.EnterTitle -> {
                _titleState.value = _titleState.value.copy(
                    text = event.text
                )
            }
            NoteDetailEvent.ModifyCompleted -> {
                _onModify.value = !_onModify.value
                modifyNote()
            }
            NoteDetailEvent.ModifyClicked -> {
                _onModify.value = !_onModify.value
            }
        }
    }

    private fun getNote(noteId: ObjectId) = viewModelScope.launch {
        noteDetailUseCase.getNote(noteId)?.let {
            _note.value = it
            _titleState.value = _titleState.value.copy(
                text = it.title
            )
            _descriptionState.value = _descriptionState.value.copy(
                text = it.description
            )
        }
    }

    private fun modifyNote() = viewModelScope.launch(Dispatchers.IO) {
        _loadingState.value = _loadingState.value.copy(
            isLoading = true
        )
        noteDetailUseCase.modifyNote(
            note = Note().apply {
                _id = _note.value._id
                title = _titleState.value.text
                description = _descriptionState.value.text
            }
        )
        delay(1000L)
        _loadingState.value = _loadingState.value.copy(
            isLoading = false
        )
    }
}
