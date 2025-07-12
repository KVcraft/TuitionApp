package com.example.tuitionapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<NotificationModel> notificationList;

    public NotificationAdapter(List<NotificationModel> notificationList) {
        this.notificationList = notificationList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, message, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            message = itemView.findViewById(R.id.textMessage);
            date = itemView.findViewById(R.id.textDate);
        }
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationModel model = notificationList.get(position);
        holder.title.setText(model.title);
        holder.message.setText(model.message);
        holder.date.setText(model.date);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
