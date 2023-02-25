package com.example.ondiet.presentation.createnote.event

sealed class CreateNoteEvent {
    data class EnterTitle(val text: String) : CreateNoteEvent()
    data class EnterDescription(val text: String) : CreateNoteEvent()
    object Complete : CreateNoteEvent()
}
