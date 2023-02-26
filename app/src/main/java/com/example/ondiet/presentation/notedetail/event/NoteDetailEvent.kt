package com.example.ondiet.presentation.notedetail.event

sealed class NoteDetailEvent {
    data class EnterTitle(val text: String) : NoteDetailEvent()
    data class EnterDescription(val text: String) : NoteDetailEvent()
    object ModifyClicked : NoteDetailEvent()
    object ModifyCompleted : NoteDetailEvent()
}
