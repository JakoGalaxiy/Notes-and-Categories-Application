package com.fic.notesandcategoriesapplication.model;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {

    private NoteCategoryDao noteCategoryDao;
    private LiveData<List<CategoryWithNotes>> allCategoriesWithNotes;
    private LiveData<List<Category>> allCategories;

    public NoteRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        noteCategoryDao = db.noteCategoryDao();
        allCategoriesWithNotes = noteCategoryDao.getCategoriesWithNotes();
        allCategories = noteCategoryDao.getAllCategories();
    }

    // LiveData que contiene la relación 1:N
    public LiveData<List<CategoryWithNotes>> getAllCategoriesWithNotes() {
        return allCategoriesWithNotes;
    }

    // LiveData de todas las categorías (útil para el spinner de AddNote)
    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    // Búsqueda avanzada
    public LiveData<List<Note>> searchNotes(String query) {
        // En Room, la búsqueda con LIKE requiere envolver el texto con '%'
        String searchQuery = "%" + query + "%";
        return noteCategoryDao.searchNotesByText(searchQuery);
    }

    // Inserción de nota
    public void insertNote(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            noteCategoryDao.insertNote(note);
        });
    }

    // Inserción de categoría
    public void insertCategory(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            noteCategoryDao.insertCategory(category);
        });
    }
}