package com.altaelimia.todoapp.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.altaelimia.todoapp.CallBack.CallBackListener;
import com.altaelimia.todoapp.Database.RoomAppDatabase;
import com.altaelimia.todoapp.Class.Task;
import com.altaelimia.todoapp.CallBack.TaskDao;
import com.altaelimia.todoapp.databinding.ItemTaskBinding;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final CallBackListener onCallBackListener;
    Context context;
    private int status;
    private List<Task> taskList;
    private TaskDao taskDao;

    public TaskAdapter(Context context, List<Task> taskList, int status, CallBackListener onCallBackListener) {
        this.context = context;
        this.taskList = taskList;
        this.status = status;
        this.onCallBackListener = onCallBackListener;
        this.taskDao = RoomAppDatabase.getInstance(context).taskDao();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        ItemTaskBinding binding = ItemTaskBinding.inflate(
//                LayoutInflater.from(context), parent, false);

        return new ViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Task task = taskList.get(position);

        holder.binding.txtTitle.setText(task.title);

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
            boolean isChecked = holder.binding.checkBox.isChecked();

            task.setChecked(isChecked);

            taskDao.update(task);
            notifyItemChanged(holder.getBindingAdapterPosition());
//            notifyDataSetChanged();
            // this the calling on listener
            onCallBackListener.onCallBack(task);// if i add paramater >> i should pass it here
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public Task getTaskAt(int position) {
        return taskList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemTaskBinding binding;

        public ViewHolder(ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }
}
