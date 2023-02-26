package com.example.ondiet.presentation.notedetail.usecase.component

import com.example.ondiet.domain.model.Note
import com.example.ondiet.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

class GetNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: ObjectId): Flow<Note?>? {
        return repository.getNote(noteId)
    }
}
