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

import com.altaelimia.todoapp.databinding.ItemTaskBinding;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final CallBackListener onCallBackListener;
    Context context;
    //    ArrayList<String> ids, titles;
    int status;
    //    DatabaseHelper db;
    List<Task> taskList;
    TaskDao taskDao;

//    public TaskAdapter(Context context,
//                       ArrayList<String> ids,
//                       ArrayList<String> titles,
//                       int status) {
//
//        this.context = context;
//        this.ids = ids;
//        this.titles = titles;
//        this.status = status;
//        db = new DatabaseHelper(context);
//    }

    public TaskAdapter(Context context, List<Task> taskList, int status, CallBackListener onCallBackListener) {
        this.context = context;
        this.taskList = taskList;
        this.status = status;
        this.onCallBackListener = onCallBackListener;
        this.taskDao = RoomAppDatabase.getInstance(context).taskDao();
    }

//    public Task getTaskAt(int position) {
//        return taskList.get(position);
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(context)
//               .inflate(R.layout.item_task, parent, false);

        ItemTaskBinding binding = ItemTaskBinding.inflate(
                LayoutInflater.from(context), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Task task = taskList.get(position);

//        holder.binding.txtTitle.setText(titles.get(position));

        holder.binding.txtTitle.setText(task.title);
//        holder.binding.checkBox.setOnCheckedChangeListener(null);

        if (task.isChecked) {

            holder.binding.checkBox.setChecked(true);

            holder.binding.txtTitle.setPaintFlags(
                    holder.binding.txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
        } else {
            holder.binding.checkBox.setChecked(false);
            holder.binding.txtTitle.setPaintFlags(
                    holder.binding.txtTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
            );
        }
        holder.binding.checkBox.setOnClickListener(view -> {
            //            RoomAppDatabase.getInstance(context)
//                    .taskDao()
//                    .updateStatus(task.id, isChecked ? 1 : 0);
//            task.isChecked = isChecked ? 1 : 0;
            boolean isChecked = holder.binding.checkBox.isChecked();

            task.setChecked(isChecked);

            taskDao.update(task);
            notifyDataSetChanged();
            // this the calling on listener
            onCallBackListener.onCallBack(task);// if i add paramater >> i should pass it here
//            taskDao.getAllTasks();
        });

//        holder.binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//
//            String id = ids.get(position);
//
//            if(isChecked){
//                db.updateStatus(id, 1);
//            } else {
//                db.updateStatus(id, 0);
//            }
//
//            if(context instanceof MainActivity){
//                ((MainActivity) context).refreshFragments();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public Task getTaskAt(int position) {
        return taskList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

//        TextView txtTitle;
//        CheckBox checkBox;

        ItemTaskBinding binding;

        public ViewHolder(ItemTaskBinding binding) {
//            super(itemView);
            super(binding.getRoot());
            this.binding = binding;
//            txtTitle = itemView.findViewById(R.id.txtTitle);
//            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
