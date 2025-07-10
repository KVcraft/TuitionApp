package com.example.tuitionapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<Student> studentList;

    public StudentAdapter(List<Student> studentList) {
        this.studentList = studentList;
    }

    public List<Student> getStudents() {
        return studentList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textRoll;
        EditText editMarks;

        public ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textRoll = itemView.findViewById(R.id.textRoll);
            editMarks = itemView.findViewById(R.id.editMarks);
        }

        public void bind(Student student) {
            textName.setText(student.getName());
            textRoll.setText(student.getUsername());
            editMarks.setText(student.getMarks());

            editMarks.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override public void afterTextChanged(Editable s) {
                    student.setMarks(s.toString());
                }
            });
        }
    }

    @NonNull
    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.ViewHolder holder, int position) {
        holder.bind(studentList.get(position));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}

