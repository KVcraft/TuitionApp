package com.example.tuitionapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewResultAdapter extends RecyclerView.Adapter<ViewResultAdapter.ViewHolder> {

    private List<ViewResultModel> resultList;

    public ViewResultAdapter(List<ViewResultModel> resultList) {
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewResultModel result = resultList.get(position);
        holder.textCourseName.setText("Course: " + result.getCourseName());
        holder.textGrade.setText("Grade: " + result.getGrade());
        holder.textDate.setText("Date: " + result.getDate());
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textCourseName, textGrade, textDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCourseName = itemView.findViewById(R.id.textCourseName);
            textGrade = itemView.findViewById(R.id.textGrade);
            textDate = itemView.findViewById(R.id.textDate);
        }
    }
}

