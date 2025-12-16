package com.fic.notesandcategoriesapplication.view;

// --- Importaciones de Android SDK y AndroidX ---
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

// --- Importaciones de tu propio proyecto (MVC) ---
import com.fic.notesandcategoriesapplication.R;
import com.fic.notesandcategoriesapplication.controller.NoteController;
import com.fic.notesandcategoriesapplication.model.Note;
import com.fic.notesandcategoriesapplication.view.NoteAdapter;
import com.fic.notesandcategoriesapplication.view.CategoryNotesAdapter;

// IMPLEMENTAR LA INTERFAZ: NoteAdapter.OnItemClickListener
public class MainActivity extends AppCompatActivity implements NoteAdapter.OnItemClickListener {

    private NoteController noteController;
    private RecyclerView categoriesNotesRecyclerView;
    private EditText searchEditText;
    private NoteAdapter searchAdapter;
    private CategoryNotesAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Inicializar Vistas
        categoriesNotesRecyclerView = findViewById(R.id.categories_notes_recycler_view);
        searchEditText = findViewById(R.id.search_edit_text);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);

        // 2. Inicializar el Controller/ViewModel
        noteController = new ViewModelProvider(this).get(NoteController.class);

        // 3. Configuración del RecyclerView y Adaptadores
        categoriesNotesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchAdapter = new NoteAdapter();
        mainAdapter = new CategoryNotesAdapter();

        // **IMPORTANTE: CONECTAR EL LISTENER DE ACCIÓN AL ADAPTADOR DE BÚSQUEDA**
        searchAdapter.setOnItemClickListener(this);

        // **AÑADIDO: CONECTAR EL LISTENER DE ACCIÓN AL ADAPTADOR PRINCIPAL**
        mainAdapter.setOnItemClickListener(this);

        // Establecer el adaptador principal por defecto
        categoriesNotesRecyclerView.setAdapter(mainAdapter);

        // 4. Lógica del FAB
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(intent);
        });

        // 5. Lógica de Observación y Búsqueda Dinámica

        // --- Observar la Relación 1:N (Datos Principales) ---
        noteController.getAllCategoriesWithNotes().observe(this, categoryWithNotesList -> {
            if (searchEditText.getText().toString().isEmpty()) {
                mainAdapter.setCategoriesWithNotes(categoryWithNotesList);
                if (categoriesNotesRecyclerView.getAdapter() != mainAdapter) {
                    categoriesNotesRecyclerView.setAdapter(mainAdapter);
                }
            }
        });

        // --- Búsqueda Dinámica ---
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                String searchText = s.toString().trim();

                if (searchText.isEmpty()) {
                    categoriesNotesRecyclerView.setAdapter(mainAdapter);
                    mainAdapter.setCategoriesWithNotes(noteController.getAllCategoriesWithNotes().getValue());
                } else {
                    if (categoriesNotesRecyclerView.getAdapter() != searchAdapter) {
                        categoriesNotesRecyclerView.setAdapter(searchAdapter);
                    }
                    noteController.searchNotes(searchText).observe(MainActivity.this, notes -> {
                        searchAdapter.setNotes(notes);
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    } // Fin de onCreate

    // ******************************************************
    // *** IMPLEMENTACIÓN DE NoteAdapter.OnItemClickListener ***
    // ******************************************************

    @Override
    public void onEditClick(Note note) {
        Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);

        // *** ENVÍO DE DATOS USANDO LAS CONSTANTES DE AddNoteActivity ***
        intent.putExtra(AddNoteActivity.EXTRA_NOTE_ID, note.getNoteId());
        intent.putExtra(AddNoteActivity.EXTRA_TITLE, note.getNoteTitle());
        intent.putExtra(AddNoteActivity.EXTRA_CONTENT, note.getNoteContent());
        intent.putExtra(AddNoteActivity.EXTRA_CATEGORY_ID, note.getCategoryId());
        intent.putExtra(AddNoteActivity.EXTRA_CREATED_AT, note.getCreatedAt());

        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Note note) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar la nota: \"" + note.getNoteTitle() + "\"?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    noteController.delete(note);
                    Toast.makeText(MainActivity.this, "Nota eliminada: " + note.getNoteTitle(), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }
}