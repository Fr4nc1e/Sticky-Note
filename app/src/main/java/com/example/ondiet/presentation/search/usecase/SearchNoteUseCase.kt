package com.example.ondiet.presentation.search.usecase

import com.example.ondiet.domain.model.Note
import com.example.ondiet.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class SearchNoteUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(title: String): Flow<List<Note>> {
        return repository.filterNotes(title)
    }
}
