package com.fic.notesandcategoriesapplication.view;

// --- Importaciones de Android SDK y AndroidX ---
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton; // Para el botón de agregar

// --- Importaciones de tu propio proyecto (MVC) ---
import com.fic.notesandcategoriesapplication.R;
import com.fic.notesandcategoriesapplication.controller.NoteController;
// NoteAdapter y CategoryNotesAdapter están en el mismo paquete 'view', pero se importan explícitamente si no se resuelven automáticamente
import com.fic.notesandcategoriesapplication.view.NoteAdapter;
import com.fic.notesandcategoriesapplication.view.CategoryNotesAdapter;


public class MainActivity extends AppCompatActivity {

    private NoteController noteController;
    private RecyclerView categoriesNotesRecyclerView;
    private EditText searchEditText;
    private NoteAdapter searchAdapter;
    private CategoryNotesAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Asume que tienes R.layout.activity_main

        // 1. Inicializar Vistas
        categoriesNotesRecyclerView = findViewById(R.id.categories_notes_recycler_view);
        searchEditText = findViewById(R.id.search_edit_text);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add); // Obtener referencia al FAB

        // 2. Inicializar el Controller/ViewModel
        noteController = new ViewModelProvider(this).get(NoteController.class);

        // 3. Configuración del RecyclerView y Adaptadores
        categoriesNotesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchAdapter = new NoteAdapter();
        mainAdapter = new CategoryNotesAdapter();

        // Establecer el adaptador principal por defecto
        categoriesNotesRecyclerView.setAdapter(mainAdapter);

        // 4. Lógica del FAB (Solución al problema de abrir la actividad)
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(intent);
        });

        // 5. Lógica de Observación y Búsqueda Dinámica

        // --- Observar la Relación 1:N (Datos Principales) ---
        noteController.getAllCategoriesWithNotes().observe(this, categoryWithNotesList -> {
            // Solo actualiza si no hay texto de búsqueda activo
            if (searchEditText.getText().toString().isEmpty()) {
                mainAdapter.setCategoriesWithNotes(categoryWithNotesList);

                // Asegurar que el adaptador principal está activo si se reciben nuevos datos
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
                    // 1. Volver a la lista principal (1:N)
                    categoriesNotesRecyclerView.setAdapter(mainAdapter);

                    // 2. Asegurar que los datos principales se carguen si se borró el texto
                    mainAdapter.setCategoriesWithNotes(noteController.getAllCategoriesWithNotes().getValue());
                } else {
                    // 1. Cambiar al adaptador de búsqueda
                    if (categoriesNotesRecyclerView.getAdapter() != searchAdapter) {
                        categoriesNotesRecyclerView.setAdapter(searchAdapter);
                    }

                    // 2. Ejecutar y observar los resultados de la búsqueda (Consulta LIKE)
                    noteController.searchNotes(searchText).observe(MainActivity.this, notes -> {
                        searchAdapter.setNotes(notes);
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}