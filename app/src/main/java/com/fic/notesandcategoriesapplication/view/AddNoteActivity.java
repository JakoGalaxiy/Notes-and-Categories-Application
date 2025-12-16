package com.fic.notesandcategoriesapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.fic.notesandcategoriesapplication.R;
import com.fic.notesandcategoriesapplication.controller.NoteController;
import com.fic.notesandcategoriesapplication.model.Category;
import com.fic.notesandcategoriesapplication.model.Note;

import java.util.ArrayList;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity {

    // CLAVES DEFINIDAS PARA LA COMUNICACIÓN (Asegúrate de que coinciden con MainActivity)
    public static final String EXTRA_NOTE_ID = "com.fic.notesandcategoriesapplication.EXTRA_NOTE_ID";
    public static final String EXTRA_TITLE = "com.fic.notesandcategoriesapplication.EXTRA_TITLE";
    public static final String EXTRA_CONTENT = "com.fic.notesandcategoriesapplication.EXTRA_CONTENT";
    public static final String EXTRA_CATEGORY_ID = "com.fic.notesandcategoriesapplication.EXTRA_CATEGORY_ID";
    public static final String EXTRA_CREATED_AT = "com.fic.notesandcategoriesapplication.EXTRA_CREATED_AT";

    private NoteController noteController;
    private EditText noteTitleEditText, noteContentEditText, categoryNameEditText;
    private Spinner categorySpinner;
    private Button addNoteButton, addCategoryButton;
    private List<Category> availableCategories = new ArrayList<>();

    // Variables de estado para la EDICIÓN
    private int noteIdToEdit = -1;
    private long noteCreatedAt = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Inicializar vistas
        noteTitleEditText = findViewById(R.id.note_title_edit_text);
        noteContentEditText = findViewById(R.id.note_content_edit_text);
        categorySpinner = findViewById(R.id.category_spinner);
        addNoteButton = findViewById(R.id.add_note_button);
        categoryNameEditText = findViewById(R.id.category_name_edit_text);
        addCategoryButton = findViewById(R.id.add_category_button);

        // Inicializar el Controller/ViewModel
        noteController = new ViewModelProvider(this).get(NoteController.class);

        // --- LÓGICA DE EDICIÓN: Obtener datos pasados por Intent ---
        Intent intent = getIntent();
        int currentNoteCategoryId = intent.getIntExtra(EXTRA_CATEGORY_ID, -1); // Obtener la ID de categoría primero

        if (intent.hasExtra(EXTRA_NOTE_ID)) {
            // Modo Edición
            noteIdToEdit = intent.getIntExtra(EXTRA_NOTE_ID, -1);
            // USAR LA CLAVE CORREGIDA: EXTRA_CREATED_AT (si la enviaste)
            noteCreatedAt = intent.getLongExtra(EXTRA_CREATED_AT, System.currentTimeMillis());

            // Llenar campos usando las claves corregidas:
            noteTitleEditText.setText(intent.getStringExtra(EXTRA_TITLE));
            noteContentEditText.setText(intent.getStringExtra(EXTRA_CONTENT));

            // Cambiar el texto del botón
            addNoteButton.setText("GUARDAR CAMBIOS");
        }
        // Si no hay EXTRA_NOTE_ID, estamos en Modo Añadir (el texto del botón se mantiene como "AGREGAR NOTA")

        // Observar categorías para el Spinner
        noteController.getAllCategories().observe(this, categories -> {
            availableCategories = categories;
            List<String> categoryNames = new ArrayList<>();
            int selectedCategoryPosition = -1;

            for (int i = 0; i < categories.size(); i++) {
                Category cat = categories.get(i);
                categoryNames.add(cat.getCategoryName());

                // Si estamos en modo Edición, buscar la posición de la categoría actual de la nota
                if (cat.getCategoryId() == currentNoteCategoryId) {
                    selectedCategoryPosition = i;
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, categoryNames);
            categorySpinner.setAdapter(adapter);

            // Seleccionar la categoría si estamos editando
            if (noteIdToEdit != -1 && selectedCategoryPosition != -1) {
                categorySpinner.setSelection(selectedCategoryPosition);
            }
        });

        // Lógica para agregar/actualizar Nota
        addNoteButton.setOnClickListener(v -> {
            String title = noteTitleEditText.getText().toString().trim();
            String content = noteContentEditText.getText().toString().trim();
            int selectedPosition = categorySpinner.getSelectedItemPosition();

            if (title.isEmpty() || content.isEmpty() || selectedPosition == Spinner.INVALID_POSITION) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (availableCategories.isEmpty()) {
                Toast.makeText(this, "Debe haber al menos una categoría.", Toast.LENGTH_SHORT).show();
                return;
            }

            int categoryId = availableCategories.get(selectedPosition).getCategoryId();

            if (noteIdToEdit != -1) {
                // *** MODO EDICIÓN (ACTUALIZAR) ***
                // Usamos la fecha de creación original (noteCreatedAt)
                Note updatedNote = new Note(title, content, noteCreatedAt, categoryId);
                updatedNote.setNoteId(noteIdToEdit);
                noteController.updateNote(updatedNote);

                Toast.makeText(this, "Nota actualizada.", Toast.LENGTH_SHORT).show();
            } else {
                // *** MODO AÑADIR (INSERCIÓN) ***
                Note newNote = new Note(title, content, System.currentTimeMillis(), categoryId);
                noteController.insertNote(newNote);

                Toast.makeText(this, "Nota agregada.", Toast.LENGTH_SHORT).show();
            }

            finish(); // Cierra la actividad
        });

        // Lógica para agregar Categoría (Existente)
        addCategoryButton.setOnClickListener(v -> {
            String categoryName = categoryNameEditText.getText().toString().trim();
            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Ingresa un nombre de categoría.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Verifica que la categoría no exista ya (opcional, pero buena práctica)
            boolean exists = false;
            for (Category cat : availableCategories) {
                if (cat.getCategoryName().equalsIgnoreCase(categoryName)) {
                    exists = true;
                    break;
                }
            }

            if (exists) {
                Toast.makeText(this, "La categoría ya existe.", Toast.LENGTH_SHORT).show();
            } else {
                noteController.insertCategory(new Category(categoryName));
                categoryNameEditText.setText("");
                Toast.makeText(this, "Categoría agregada.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}