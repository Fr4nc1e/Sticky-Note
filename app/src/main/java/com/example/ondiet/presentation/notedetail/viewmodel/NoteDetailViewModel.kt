package com.example.ondiet.presentation.notedetail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ondiet.core.presentation.state.LoadingState
import com.example.ondiet.core.presentation.state.TextState
import com.example.ondiet.domain.model.Note
import com.example.ondiet.presentation.notedetail.event.NoteDetailEvent
import com.example.ondiet.presentation.notedetail.usecase.NoteDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.types.RealmInstant
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val noteDetailUseCase: NoteDetailUseCase
) : ViewModel() {

    private val _note = MutableStateFlow(Note())
    val note = _note.asStateFlow()

    private val _titleState = MutableStateFlow(TextState())
    val titleState = _titleState.asStateFlow()

    private val _descriptionState = MutableStateFlow(TextState())
    val descriptionState = _descriptionState.asStateFlow()

    private val _loadingState = MutableStateFlow(LoadingState())
    val loadingState = _loadingState.asStateFlow()

    private val _onModify = MutableStateFlow(true)
    val onModify = _onModify.asStateFlow()

    init {
        savedStateHandle.get<String>("noteId")?.let { noteId ->
            getNote(ObjectId(hexString = noteId))
        }
    }

    fun onEvent(event: NoteDetailEvent) {
        when (event) {
            is NoteDetailEvent.EnterDescription -> {
                _descriptionState.value = TextState(text = event.text)
            }
            is NoteDetailEvent.EnterTitle -> {
                _titleState.value = TextState(text = event.text)
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
        noteDetailUseCase.getNote(noteId)?.collectLatest {
            it?.let {
                _note.value = it
                _titleState.value = _titleState.value.copy(
                    text = it.title
                )
                _descriptionState.value = _descriptionState.value.copy(
                    text = it.description
                )
            }
        }
    }

    private fun modifyNote() = viewModelScope.launch(Dispatchers.IO) {
        _loadingState.value = LoadingState(isLoading = true)
        noteDetailUseCase.modifyNote(
            note = Note().apply {
                _id = _note.value._id
                title = _titleState.value.text
                description = _descriptionState.value.text
                timestamp = RealmInstant.now()
            }
        )
        delay(1000L)
        _loadingState.value = LoadingState(isLoading = false)
    }
}
