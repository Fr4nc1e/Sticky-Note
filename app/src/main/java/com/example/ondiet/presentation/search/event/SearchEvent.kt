package com.example.ondiet.presentation.search.event

import org.mongodb.kbson.ObjectId

sealed class SearchEvent {
    object OnSearch : SearchEvent()
    data class Input(val text: String) : SearchEvent()
    data class EnterDetailScreen(val noteId: ObjectId) : SearchEvent()
    data class DeleteNote(val noteId: ObjectId) : SearchEvent()
}
