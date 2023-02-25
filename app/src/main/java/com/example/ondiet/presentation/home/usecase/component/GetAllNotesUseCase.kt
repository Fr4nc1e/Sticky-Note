package com.example.ondiet.presentation.home.usecase.component

import com.example.ondiet.domain.model.Note
import com.example.ondiet.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetAllNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}
