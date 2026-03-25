package com.altaelimia.todoapp.fragment;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.altaelimia.todoapp.Adapter.TaskAdapter;
import com.altaelimia.todoapp.CallBack.CallBackListener;
import com.altaelimia.todoapp.CallBack.TaskDao;
import com.altaelimia.todoapp.Database.RoomAppDatabase;
import com.altaelimia.todoapp.Class.TabChangedEvent;
import com.altaelimia.todoapp.Class.Task;
import com.altaelimia.todoapp.databinding.FragmentTaskBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {

    private static final String ARG_STATUS = "status";
    private int status;
    private FragmentTaskBinding binding;
    List<Task> tasks;
    List<Task> taskList;
    private TaskAdapter adapter;
    RoomAppDatabase db;
    private TaskDao taskDao;

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

        db = RoomAppDatabase.getInstance(getContext());
        taskDao = db.taskDao();
        taskList = new ArrayList<>();
        tasks = new ArrayList<>();

        if (getArguments() != null) {
            status = getArguments().getInt(ARG_STATUS);
        }

        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        loadTasks();
        setupSwipe();

        return binding.getRoot();
    }

    private List<Task> getTasksList() {
//        List<Task> list;
//        if (status == 1) {
//            list = taskDao.getTasksIsChecked(true);
//        } else {
//            list = taskDao.getTasksIsChecked(false);
//        }
        return taskDao.getTasksIsChecked(status == 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTabChanged(TabChangedEvent event) {

        Log.d("EVENT_TEST", "Tab Changed: " + event.tabPosition);

        loadTasks();

        if (event.tabPosition == 0) {
//            list = taskDao.getTasksIsChecked(false);
            loadToDoTasks();
        } else {
//            list = taskDao.getTasksIsChecked(true);
            loadCompletedTasks();
        }
    }

    private void loadToDoTasks() {
        taskList.clear();
        taskList.addAll(taskDao.getPendingTasks());
        adapter.notifyDataSetChanged();
    }

    private void loadCompletedTasks() {
        taskList.clear();
        taskList.addAll(taskDao.getCompletedTasks());
        adapter.notifyDataSetChanged();
    }

    private void loadTasks() {
        tasks = getTasksList();
        // here what type you enter between <> the object in this type
        CallBackListener<Task> callBackListener = object -> {
            // this code this performed when calling the listener inside adapter
            tasks = getTasksList();
//            adapter.taskList = tasks;
//            adapter.notifyDataSetChanged();
            adapter.updateTaskList(tasks);
        };
        adapter = new TaskAdapter(getContext(), tasks, status, callBackListener);
        binding.recyclerView.setAdapter(adapter);
    }

    private void checkTasks() {
        List<Task> tasks = taskDao.getAllTasks();

        if (tasks.isEmpty()) {
            binding.txtEmpty.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            binding.txtEmpty.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkTasks();
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
                            taskDao.delete(task);
                            adapter.notifyItemRemoved(position);
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
                    taskDao.update(task);
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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
