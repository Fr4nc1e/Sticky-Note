package com.example.ondiet.presentation.home.usecase.component

import com.example.ondiet.domain.repository.NoteRepository
import org.mongodb.kbson.ObjectId

class DeleteNotesUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: ObjectId) {
        return repository.deleteNote(id = noteId)
    }
}
