package com.fic.notesandcategoriesapplication.view;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.fic.notesandcategoriesapplication.R;
import com.fic.notesandcategoriesapplication.controller.NoteController;
import com.fic.notesandcategoriesapplication.model.Category;
import com.fic.notesandcategoriesapplication.model.Note;

import java.util.ArrayList;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity {

    private NoteController noteController;
    private EditText noteTitleEditText, noteContentEditText, categoryNameEditText;
    private Spinner categorySpinner;
    private Button addNoteButton, addCategoryButton;
    private List<Category> availableCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Inicializar vistas
        // ... (asumimos que R.id.x existen)
        noteTitleEditText = findViewById(R.id.note_title_edit_text);
        noteContentEditText = findViewById(R.id.note_content_edit_text);
        categorySpinner = findViewById(R.id.category_spinner);
        addNoteButton = findViewById(R.id.add_note_button);
        categoryNameEditText = findViewById(R.id.category_name_edit_text);
        addCategoryButton = findViewById(R.id.add_category_button);

        // Inicializar el Controller/ViewModel
        noteController = new ViewModelProvider(this).get(NoteController.class);

        // Observar categorías para el Spinner
        noteController.getAllCategories().observe(this, categories -> {
            availableCategories = categories;
            List<String> categoryNames = new ArrayList<>();
            for (Category cat : categories) {
                categoryNames.add(cat.getCategoryName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, categoryNames);
            categorySpinner.setAdapter(adapter);
        });

        // Lógica para agregar Nota
        addNoteButton.setOnClickListener(v -> {
            String title = noteTitleEditText.getText().toString();
            String content = noteContentEditText.getText().toString();
            int selectedPosition = categorySpinner.getSelectedItemPosition();

            if (title.isEmpty() || content.isEmpty() || selectedPosition == Spinner.INVALID_POSITION) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            int categoryId = availableCategories.get(selectedPosition).getCategoryId();
            Note newNote = new Note(title, content, System.currentTimeMillis(), categoryId);
            noteController.insertNote(newNote);

            Toast.makeText(this, "Nota agregada.", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad
        });

        // Lógica para agregar Categoría
        addCategoryButton.setOnClickListener(v -> {
            String categoryName = categoryNameEditText.getText().toString().trim();
            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Ingresa un nombre de categoría.", Toast.LENGTH_SHORT).show();
                return;
            }
            noteController.insertCategory(new Category(categoryName));
            categoryNameEditText.setText("");
            Toast.makeText(this, "Categoría agregada.", Toast.LENGTH_SHORT).show();
        });
    }
}