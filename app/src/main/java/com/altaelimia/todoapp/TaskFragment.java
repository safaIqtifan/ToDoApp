package com.altaelimia.todoapp;

import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.altaelimia.todoapp.databinding.FragmentTaskBinding;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {

    private static final String ARG_STATUS = "status";
    private int status;
    private FragmentTaskBinding binding;
    List<Task> tasks;

//    private DatabaseHelper dbs;
//    DatabaseHelper db;
//    RecyclerView recyclerView;
//    TextView txtEmpty;

    List<Task> taskList;
    List<Task> comTaskList;

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

//        View view = inflater.inflate(R.layout.fragment_task, container, false);

//        recyclerView = view.findViewById(R.id.recyclerView);
//        txtEmpty = view.findViewById(R.id.txtEmpty);

//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding = FragmentTaskBinding.inflate(inflater, container, false);
//        dbs = new DatabaseHelper(requireContext());
//        db = new DatabaseHelper(getContext());

        if (getArguments() != null) {
            status = getArguments().getInt(ARG_STATUS);
        }


        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        db = RoomAppDatabase.getInstance(getContext());
        taskDao = db.taskDao();
        tasks = new ArrayList<>();

        loadTasks();
        setupSwipe();

//        taskDao.getTasksByStatus(status)
//                .observe(getViewLifecycleOwner(), tasks -> {
//
//                    if (tasks.isEmpty()) {
//                        binding.txtEmpty.setVisibility(View.VISIBLE);
//                        binding.recyclerView.setVisibility(View.GONE);
//                    } else {
//                        binding.txtEmpty.setVisibility(View.GONE);
//                        binding.recyclerView.setVisibility(View.VISIBLE);
//
//                        adapter = new TaskAdapter(requireContext(), tasks);
//                        binding.recyclerView.setAdapter(adapter);
//                    }
//                });

//        loadData();


        return binding.getRoot();
    }

    private void loadTasks() {

        if (status == 1) {
            tasks = taskDao.getTasksIsChecked(true);
        } else {
            tasks = taskDao.getTasksIsChecked(false);
        }
        checkTasks();
        adapter = new TaskAdapter(getContext(), tasks, status,view -> {
            tasks = taskDao.getAllTasks();
        });
        binding.recyclerView.setAdapter(adapter);
    }

    private void checkTasks() {
//        List<Task> tasks = database.taskDao().getAllTasks();

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


//    private void loadData() {
//
//        taskList = new ArrayList<>();
//        comTaskList = new ArrayList<>();
//
//        Cursor cursor = db.getTasksByStatus(status);
//
//        while(cursor.moveToNext()){
//            taskList.add(cursor.getString(0));
//            comTaskList.add(cursor.getString(1));
//        }
//
//        if(comTaskList.isEmpty()){
//            binding.txtEmpty.setVisibility(View.VISIBLE);
//            binding.recyclerView.setVisibility(View.GONE);
//        } else {
//            binding.txtEmpty.setVisibility(View.GONE);
//            binding.recyclerView.setVisibility(View.VISIBLE);
//
//            adapter = new TaskAdapter(getContext(), taskList, comTaskList, status);
//            binding.recyclerView.setAdapter(adapter);
//        }
//    }

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
//                        String id = ids.get(position);
                        Task task = adapter.getTaskAt(position);

                        if (direction == ItemTouchHelper.RIGHT) {
                            taskDao.delete(task);
//                            db.deleteTask(id);
//                            ids.remove(position);
//                            titles.remove(position);
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

//                        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

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
//                        }

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

//                    if(!newTitle.isEmpty()){
//                        db.updateTitle(ids.get(position), newTitle);
//                        titles.set(position, newTitle);
//                        adapter.notifyItemChanged(position);
//                    }
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
