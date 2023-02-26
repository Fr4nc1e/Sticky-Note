package com.example.ondiet.presentation.notedetail.usecase.component

import com.example.ondiet.domain.model.Note
import com.example.ondiet.domain.repository.NoteRepository

class ModifyNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.updateNote(note)
    }
}
