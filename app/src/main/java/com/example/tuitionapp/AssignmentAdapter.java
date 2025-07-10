package com.example.tuitionapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder> {

    private List<Assignment> assignmentList;

    public AssignmentAdapter(List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assignment, parent, false);
        return new AssignmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentViewHolder holder, int position) {
        Assignment assignment = assignmentList.get(position);
        holder.textViewTitle.setText(assignment.getTitle());
        holder.textViewSession.setText("Session: " + assignment.getSession());
        holder.textViewOpenDate.setText("Open Date: " + assignment.getOpenDate());
        holder.textViewDueDate.setText("Due Date: " + assignment.getDueDate());
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    public static class AssignmentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewSession, textViewOpenDate, textViewDueDate;

        public AssignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewSession = itemView.findViewById(R.id.textViewSession);
            textViewOpenDate = itemView.findViewById(R.id.textViewOpenDate);
            textViewDueDate = itemView.findViewById(R.id.textViewDueDate);
        }
    }
}
