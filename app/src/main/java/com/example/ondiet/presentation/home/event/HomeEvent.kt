package com.example.ondiet.presentation.home.event

import org.mongodb.kbson.ObjectId

sealed class HomeEvent {
    data class EnterDetailScreen(val noteId: ObjectId) : HomeEvent()
    object SearchNote : HomeEvent()
    data class DeleteNote(val noteId: ObjectId) : HomeEvent()
}
