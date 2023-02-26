package com.example.ondiet.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ondiet.core.presentation.component.navigation.Screen
import com.example.ondiet.core.presentation.event.CoreUiEvent
import com.example.ondiet.domain.model.Note
import com.example.ondiet.presentation.home.event.HomeEvent
import com.example.ondiet.presentation.home.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase
) : ViewModel() {
    private val _notes = MutableStateFlow(emptyList<Note>())
    val notes = _notes.asStateFlow()

    private val _eventFlow = MutableSharedFlow<CoreUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            homeUseCase.getAllNotes().collect {
                _notes.value = it
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.DeleteNote -> {
                deleteNote(event.noteId)
            }
            is HomeEvent.EnterDetailScreen -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        CoreUiEvent.Navigate(
                            Screen.NoteDetailScreen.route + "/${event.noteId.toHexString()}"
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
