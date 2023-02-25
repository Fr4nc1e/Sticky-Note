package com.example.ondiet.di

import com.example.ondiet.data.repository.NoteRepositoryImpl
import com.example.ondiet.domain.model.Note
import com.example.ondiet.domain.repository.NoteRepository
import com.example.ondiet.presentation.createnote.usecase.CreateNoteUseCase
import com.example.ondiet.presentation.home.usecase.HomeUseCase
import com.example.ondiet.presentation.home.usecase.component.DeleteNotesUseCase
import com.example.ondiet.presentation.home.usecase.component.GetAllNotesUseCase
import com.example.ondiet.presentation.search.usecase.SearchNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(Note::class)
        )
            .compactOnLaunch()
            .build()

        return Realm.open(config)
    }

    @Singleton
    @Provides
    fun provideNoteRepository(realm: Realm): NoteRepository {
        return NoteRepositoryImpl(realm = realm)
    }

    @Singleton
    @Provides
    fun provideHomeUseCase(repository: NoteRepository): HomeUseCase {
        return HomeUseCase(
            getAllNotes = GetAllNotesUseCase(repository = repository),
            deleteNotes = DeleteNotesUseCase(repository = repository)
        )
    }

    @Singleton
    @Provides
    fun provideCreateNoteUseCase(repository: NoteRepository): CreateNoteUseCase {
        return CreateNoteUseCase(repository = repository)
    }

    @Singleton
    @Provides
    fun provideSearchNoteUseCase(repository: NoteRepository): SearchNoteUseCase {
        return SearchNoteUseCase(repository = repository)
    }
}
