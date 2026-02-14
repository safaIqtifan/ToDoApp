package com.altaelimia.todoapp;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    Context context;
    ArrayList<String> ids, titles;
    int status;
    DatabaseHelper db;

    public TaskAdapter(Context context,
                       ArrayList<String> ids,
                       ArrayList<String> titles,
                       int status) {

        this.context = context;
        this.ids = ids;
        this.titles = titles;
        this.status = status;
        db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_task, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtTitle.setText(titles.get(position));

        holder.checkBox.setOnCheckedChangeListener(null);

        if(status == 1){
            holder.checkBox.setChecked(true);
            holder.txtTitle.setPaintFlags(
                    holder.txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
        } else {
            holder.checkBox.setChecked(false);
            holder.txtTitle.setPaintFlags(
                    holder.txtTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
            );
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            String id = ids.get(position);

            if(isChecked){
                db.updateStatus(id, 1);
            } else {
                db.updateStatus(id, 0);
            }

            if(context instanceof MainActivity){
                ((MainActivity) context).refreshFragments();
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
