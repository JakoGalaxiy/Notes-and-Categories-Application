package com.fic.notesandcategoriesapplication.view; // o .view.adapters

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_simple, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.noteTitleTextView.setText(currentNote.getNoteTitle());
        holder.noteContentTextView.setText(currentNote.getNoteContent());
        holder.noteDateTextView.setText(dateFormat.format(new Date(currentNote.getCreatedAt())));
        // La ID de categoría podría mostrarse aquí o requerir una consulta adicional
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    // Método crucial para actualizar los datos desde LiveData
    public void setNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView noteTitleTextView;
        private final TextView noteContentTextView;
        private final TextView noteDateTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            noteTitleTextView = itemView.findViewById(R.id.note_title_text_view);
            noteContentTextView = itemView.findViewById(R.id.note_content_text_view);
            noteDateTextView = itemView.findViewById(R.id.note_date_text_view);
        }
    }
}