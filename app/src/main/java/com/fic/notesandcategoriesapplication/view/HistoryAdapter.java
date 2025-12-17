package com.fic.notesandcategoriesapplication.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fic.notesandcategoriesapplication.R;
import com.fic.notesandcategoriesapplication.model.entity.History;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<History> historyList = new ArrayList<>();

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History currentHistory = historyList.get(position);

        // Formatear texto para ser más legible
        String actionDisplay = currentHistory.getAction().toUpperCase();

        holder.textViewAction.setText(actionDisplay);
        holder.textViewDetails.setText(currentHistory.getDetails());
        holder.textViewCreatedAt.setText(currentHistory.getFormattedDate());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void setHistory(List<History> history) {
        this.historyList = history;
        notifyDataSetChanged();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewAction;
        private final TextView textViewDetails;
        private final TextView textViewCreatedAt;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAction = itemView.findViewById(R.id.text_view_history_action);
            textViewDetails = itemView.findViewById(R.id.text_view_history_details);
            textViewCreatedAt = itemView.findViewById(R.id.text_view_history_created_at);
        }
    }
}