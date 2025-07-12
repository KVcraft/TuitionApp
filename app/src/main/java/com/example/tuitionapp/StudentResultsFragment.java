package com.example.tuitionapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class StudentResultsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ViewResultAdapter adapter;
    private StudentResultDatabaseHelper database;

    public StudentResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_results, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize DB
        database = new StudentResultDatabaseHelper(getContext());

        // Get data from DB
        List<ViewResultModel> resultList = (List<ViewResultModel>) database.getAllResults();

        // Set adapter
        adapter = new ViewResultAdapter(resultList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
