package com.fic.notesandcategoriesapplication.view; // o .view.adapters

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fic.notesandcategoriesapplication.R;
import com.fic.notesandcategoriesapplication.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(Note.DATE_FORMAT, Locale.getDefault());

    // --- NUEVAS INTERFACES PARA MANEJAR CLICKS (Existente) ---
    public interface OnItemClickListener {
        void onEditClick(Note note);
        void onDeleteClick(Note note);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // --- NUEVO MÉTODO PARA OBTENER UNA NOTA POR POSICIÓN (Existente) ---
    public Note getNoteAt(int position) {
        return notes.get(position);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_simple, parent, false);
        // Pasa el listener al ViewHolder
        return new NoteViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.noteTitleTextView.setText(currentNote.getNoteTitle());
        holder.noteContentTextView.setText(currentNote.getNoteContent());
        holder.noteDateTextView.setText(dateFormat.format(new Date(currentNote.getCreatedAt())));

        // Asignar la nota al ViewHolder para que los botones sepan a qué nota se refieren
        holder.currentNote = currentNote;
    }

    // ************************************************
    // *** MÉTODO REQUERIDO: getItemCount() (CORREGIDO) ***
    // ************************************************
    @Override
    public int getItemCount() {
        return notes.size();
    }

    // ************************************************************
    // *** MÉTODO DE ACTUALIZACIÓN DE DATOS: setNotes() (CORREGIDO) ***
    // ************************************************************
    public void setNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView noteTitleTextView;
        private final TextView noteContentTextView;
        private final TextView noteDateTextView;
        // NUEVOS BOTONES
        private final ImageButton editButton;
        private final ImageButton deleteButton;
        // Referencia a la nota actual
        public Note currentNote;

        public NoteViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            noteTitleTextView = itemView.findViewById(R.id.note_title_text_view);
            noteContentTextView = itemView.findViewById(R.id.note_content_text_view);
            noteDateTextView = itemView.findViewById(R.id.note_date_text_view);

            // Inicializar NUEVOS BOTONES
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);

            // Lógica de click para Editar
            editButton.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onEditClick(currentNote);
                }
            });

            // Lógica de click para Eliminar
            deleteButton.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(currentNote);
                }
            });
        }
    }
}