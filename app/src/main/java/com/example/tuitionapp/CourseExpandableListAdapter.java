package com.example.tuitionapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

public class CourseExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> courseList;
    private HashMap<String, List<String>> materialMap;

    public CourseExpandableListAdapter(Context context, List<String> courseList, HashMap<String, List<String>> materialMap) {
        this.context = context;
        this.courseList = courseList;
        this.materialMap = materialMap;
    }

    @Override
    public int getGroupCount() {
        return courseList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return materialMap.get(courseList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return courseList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return materialMap.get(courseList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String courseTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        }
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(courseTitle);
        textView.setTextSize(18);
        textView.setPadding(64, 24, 24, 24);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String material = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(material);
        textView.setPadding(96, 16, 16, 16);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

