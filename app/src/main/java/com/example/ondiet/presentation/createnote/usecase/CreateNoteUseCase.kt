package com.example.ondiet.presentation.createnote.usecase

import com.example.ondiet.domain.model.Note
import com.example.ondiet.domain.repository.NoteRepository

class CreateNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.insertNote(note)
    }
}
