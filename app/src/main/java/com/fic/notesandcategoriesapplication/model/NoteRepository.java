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

    // --- LiveData de consultas (Ya existentes) ---
    public LiveData<List<CategoryWithNotes>> getAllCategoriesWithNotes() {
        return allCategoriesWithNotes;
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<List<Note>> searchNotes(String query) {
        String searchQuery = "%" + query + "%";
        return noteCategoryDao.searchNotesByText(searchQuery);
    }

    // --- MÉTODOS DE MANIPULACIÓN DE DATOS (YA EXISTENTES) ---

    public void insertNote(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            noteCategoryDao.insertNote(note);
        });
    }

    public void insertCategory(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            noteCategoryDao.insertCategory(category);
        });
    }

    // --- MÉTODOS DE ACTUALIZACIÓN Y ELIMINACIÓN (NUEVOS) ---

    // 1. Actualizar Nota
    public void updateNote(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            noteCategoryDao.updateNote(note);
        });
    }

    // 2. Eliminar Nota
    public void deleteNote(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            noteCategoryDao.deleteNote(note);
        });
    }

    // 3. Actualizar Categoría
    public void updateCategory(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            noteCategoryDao.updateCategory(category);
        });
    }

    // 4. Eliminar Categoría
    public void deleteCategory(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            noteCategoryDao.deleteCategory(category);
        });
    }
}