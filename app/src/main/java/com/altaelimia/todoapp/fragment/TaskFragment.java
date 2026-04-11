package com.altaelimia.todoapp.fragment;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.altaelimia.todoapp.Adapter.TaskAdapter;
import com.altaelimia.todoapp.CallBack.CallBackListener;
import com.altaelimia.todoapp.Class.Task;
import com.altaelimia.todoapp.ViewModel.TaskViewModel;
import com.altaelimia.todoapp.databinding.FragmentTaskBinding;

import java.util.ArrayList;

public class TaskFragment extends Fragment implements CallBackListener {

    private static final String ARG_STATUS = "status";
    private int status;
    private FragmentTaskBinding binding;
    private TaskAdapter adapter;
    TaskViewModel viewModel;

    public TaskFragment() {
    }

    public static TaskFragment newInstance(int status) {

        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STATUS, status);
        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTaskBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        if (getArguments() != null) {
            status = getArguments().getInt(ARG_STATUS);
        }
        adapter = new TaskAdapter(requireContext(), new ArrayList<>(), status, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);

        setupSwipe();

        return binding.getRoot();
    }

    private void checkTasks() {

        viewModel.getTasksByStatus(status == 1).observe(getViewLifecycleOwner(), tasks -> {

            adapter.setTasks(tasks);

            if (tasks.isEmpty()) {
                binding.txtEmpty.setVisibility(View.VISIBLE);
            } else {
                binding.txtEmpty.setVisibility(View.GONE);
            }

        });
    }

    private void setupSwipe() {

        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    Paint paint = new Paint();

                    {
                        paint.setTextSize(50);
                        paint.setFakeBoldText(true);
                    }

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                         int direction) {

                        int position = viewHolder.getAdapterPosition();
                        Task task = adapter.getTaskAt(position);

                        if (direction == ItemTouchHelper.RIGHT) {
                            viewModel.delete(task);
                        } else if (direction == ItemTouchHelper.LEFT) {
                            showEditDialog(task);
                            adapter.notifyItemChanged(position);
                        }
                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c,
                                            @NonNull RecyclerView recyclerView,
                                            @NonNull RecyclerView.ViewHolder viewHolder,
                                            float dX, float dY,
                                            int actionState,
                                            boolean isCurrentlyActive) {

                        View itemView = viewHolder.itemView;
                        float margin = 40;
                        if (dX > 0) {
                            paint.setColor(Color.RED);
                            c.drawRect(itemView.getLeft(),
                                    itemView.getTop(),
                                    dX,
                                    itemView.getBottom(),
                                    paint);

                            paint.setColor(Color.WHITE);
                            c.drawText("حذف",
                                    itemView.getLeft() + margin,
                                    itemView.getTop() + itemView.getHeight() / 2 + 20,
                                    paint);

                        } else {
                            paint.setColor(Color.GREEN);
                            c.drawRect(itemView.getRight() + dX,
                                    itemView.getTop(),
                                    itemView.getRight(),
                                    itemView.getBottom(),
                                    paint);

                            paint.setColor(Color.WHITE);
                            float textWidth = paint.measureText("تعديل");
                            c.drawText("تعديل",
                                    itemView.getRight() - textWidth - margin,
                                    itemView.getTop() + itemView.getHeight() / 2 + 20,
                                    paint);
                        }

                        super.onChildDraw(c, recyclerView, viewHolder,
                                dX, dY, actionState, isCurrentlyActive);
                    }
                };

        new ItemTouchHelper(simpleCallback)
                .attachToRecyclerView(binding.recyclerView);
    }

    private void showEditDialog(Task task) {

        EditText editText = new EditText(getContext());
        editText.setText(task.title);

        new AlertDialog.Builder(getContext())
                .setTitle("تعديل المهمة")
                .setView(editText)
                .setPositiveButton("حفظ", (dialog, which) -> {

                    task.title = editText.getText().toString().trim();
                    viewModel.update(task);
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkTasks();
    }

    @Override
    public void onCallBack(Task task) {
        viewModel.update(task);
    }

}
