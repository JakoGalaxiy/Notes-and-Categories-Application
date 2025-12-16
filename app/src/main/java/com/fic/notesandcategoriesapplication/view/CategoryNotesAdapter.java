package com.fic.notesandcategoriesapplication.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fic.notesandcategoriesapplication.R;
import com.fic.notesandcategoriesapplication.model.CategoryWithNotes;
import com.fic.notesandcategoriesapplication.model.Note;

import java.util.Collections;
import java.util.List;

public class CategoryNotesAdapter extends RecyclerView.Adapter<CategoryNotesAdapter.CategoryNoteViewHolder> {

    private List<CategoryWithNotes> dataList = Collections.emptyList();

    // **NUEVA PROPIEDAD DE LISTENER**
    private NoteAdapter.OnItemClickListener itemClickListener;

    // **NUEVO MÉTODO PARA ESTABLECER EL LISTENER**
    public void setOnItemClickListener(NoteAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
        // NOTA: No hacemos notifyDataSetChanged() aquí, ya que el cambio se hará desde MainActivity.
    }

    // --- VIEW HOLDER (Interno) ---
    public static class CategoryNoteViewHolder extends RecyclerView.ViewHolder {
        final TextView categoryTitleTextView;
        final RecyclerView notesRecyclerView;

        public CategoryNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitleTextView = itemView.findViewById(R.id.category_title_text_view);
            notesRecyclerView = itemView.findViewById(R.id.notes_list_recycler_view);

            // Configurar el RecyclerView anidado
            notesRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }

    // --- IMPLEMENTACIÓN DEL ADAPTADOR ---

    @NonNull
    @Override
    public CategoryNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout que contiene el título de la categoría y el RecyclerView anidado.
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_notes_group, parent, false);
        return new CategoryNoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryNoteViewHolder holder, int position) {
        CategoryWithNotes currentGroup = dataList.get(position);

        // 1. Mostrar el Título de la Categoría
        holder.categoryTitleTextView.setText(currentGroup.getCategory().getCategoryName());

        // 2. Configurar el RecyclerView anidado para las Notas
        List<Note> notes = currentGroup.getNotes();

        // Obtener o crear el NoteAdapter anidado
        NoteAdapter noteAdapter = (NoteAdapter) holder.notesRecyclerView.getTag();
        if (noteAdapter == null) {
            noteAdapter = new NoteAdapter();
            holder.notesRecyclerView.setAdapter(noteAdapter);
            holder.notesRecyclerView.setTag(noteAdapter);

            // **AÑADIDO: CONFIGURAR EL LISTENER SOLO UNA VEZ DURANTE LA CREACIÓN**
            if (itemClickListener != null) {
                noteAdapter.setOnItemClickListener(itemClickListener);
            }
        }

        // Actualizar los datos del adaptador de notas con las notas de esta categoría
        noteAdapter.setNotes(notes);

        // Opcional: Ocultar si no hay notas
        holder.notesRecyclerView.setVisibility(notes.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // Método para actualizar los datos desde LiveData (el Controller)
    public void setCategoriesWithNotes(List<CategoryWithNotes> data) {
        this.dataList = data;
        notifyDataSetChanged();
    }
}