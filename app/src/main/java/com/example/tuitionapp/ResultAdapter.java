package com.example.tuitionapp;

import android.content.Context;
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

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private final List<ResultModel> resultList;
    private final Context context;

    public ResultAdapter(Context context, List<ResultModel> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_result_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResultModel model = resultList.get(position);
        holder.nameText.setText(model.getName());

        // Populate existing result if any
        holder.resultEdit.setText(model.getResult());

        // Save user input back to model
        holder.resultEdit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                model.setResult(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        EditText resultEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            resultEdit = itemView.findViewById(R.id.resultEdit);
        }
    }

    public List<ResultModel> getResults() {
        return resultList;
    }
}

