package com.example.tuitionapp;

import android.os.Bundle;
import android.widget.ExpandableListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CourseMaterialsActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    CourseExpandableListAdapter adapter;
    List<String> courseList;
    HashMap<String, List<String>> materialMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course_material);

        expandableListView = findViewById(R.id.expandableListView);

        prepareData();
        adapter = new CourseExpandableListAdapter(this, courseList, materialMap);
        expandableListView.setAdapter(adapter);
    }

    private void prepareData() {
        courseList = new ArrayList<>();
        materialMap = new HashMap<>();

        courseList.add("Course One");
        courseList.add("Course Two");

        List<String> courseOneMaterials = new ArrayList<>();
        courseOneMaterials.add("Material 1");
        courseOneMaterials.add("Material 2");

        List<String> courseTwoMaterials = new ArrayList<>();
        courseTwoMaterials.add("Material A");
        courseTwoMaterials.add("Material B");

        materialMap.put(courseList.get(0), courseOneMaterials);
        materialMap.put(courseList.get(1), courseTwoMaterials);
    }
}
