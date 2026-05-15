package com.altaelimia.todoapp.Adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.altaelimia.todoapp.CallBack.CallBackListener;
import com.altaelimia.todoapp.Class.Task;
import com.altaelimia.todoapp.databinding.ItemTaskBinding;

import java.util.List;
import java.util.Objects;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.ViewHolder> {

    private final CallBackListener onCallBackListener;

    public TaskAdapter(CallBackListener onCallBackListener) {
        super(DIFF_CALLBACK);
        this.onCallBackListener = onCallBackListener;
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.isChecked == newItem.isChecked &&
                    Objects.equals(oldItem.title, newItem.title);
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding binding = ItemTaskBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), onCallBackListener);
    }

    public Task getTaskAt(int position) {
        return getItem(position);
    }

    public void setTasks(List<Task> tasks) {
        submitList(tasks);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTaskBinding binding;

        public ViewHolder(ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Task task, CallBackListener listener) {
            binding.txtTitle.setText(task.title);
            binding.checkBox.setChecked(task.isChecked);
            
            updateTitleStyle(task.isChecked);

            binding.checkBox.setOnClickListener(view -> {
                boolean isChecked = binding.checkBox.isChecked();
                task.setChecked(isChecked);
                updateTitleStyle(isChecked);
                listener.onCallBack(task);
            });
        }

        private void updateTitleStyle(boolean isChecked) {
            if (isChecked) {
                binding.txtTitle.setPaintFlags(binding.txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                binding.txtTitle.setPaintFlags(binding.txtTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }
    }
}
