package com.fic.notesandcategoriesapplication.controller;

// ... (Imports existentes) ...
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fic.notesandcategoriesapplication.model.Category;
import com.fic.notesandcategoriesapplication.model.CategoryWithNotes;
import com.fic.notesandcategoriesapplication.model.Note;
import com.fic.notesandcategoriesapplication.model.NoteRepository;

import java.util.List;

public class NoteController extends AndroidViewModel {

    private NoteRepository noteRepository;
    private LiveData<List<CategoryWithNotes>> allCategoriesWithNotes;
    private LiveData<List<Category>> allCategories;

    public NoteController(Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        allCategoriesWithNotes = noteRepository.getAllCategoriesWithNotes();
        allCategories = noteRepository.getAllCategories();
    }

    // --- Datos de la Interfaz Principal (Relación 1:N) ---

    public LiveData<List<CategoryWithNotes>> getAllCategoriesWithNotes() {
        return allCategoriesWithNotes;
    }

    // --- Datos para agregar notas/categorías ---

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    // --- Acciones del usuario (Controller logic) ---

    public void insertNote(Note note) {
        noteRepository.insertNote(note);
    }

    public void insertCategory(Category category) {
        noteRepository.insertCategory(category);
    }

    public void updateNote(Note note) {
        noteRepository.updateNote(note);
    }

    public void delete(Note note) {
        noteRepository.deleteNote(note);
    }

    public LiveData<List<Note>> searchNotes(String query) {
        if (query == null || query.trim().isEmpty()) {
            return noteRepository.searchNotes("A_VERY_UNLIKELY_TEXT_TO_BE_FOUND");
        }
        return noteRepository.searchNotes(query);
    }
}