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

import java.util.ArrayList;

public class TaskFragment extends Fragment {

    private static final String ARG_STATUS = "status";
    int status;

    DatabaseHelper db;
    RecyclerView recyclerView;
    TextView txtEmpty;

    ArrayList<String> ids;
    ArrayList<String> titles;

    TaskAdapter adapter;

    public TaskFragment() { }

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

        View view = inflater.inflate(R.layout.fragment_task, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        txtEmpty = view.findViewById(R.id.txtEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = new DatabaseHelper(getContext());

        if (getArguments() != null) {
            status = getArguments().getInt(ARG_STATUS);
        }

        loadData();
        setupSwipe();

        return view;
    }

    private void loadData() {

        ids = new ArrayList<>();
        titles = new ArrayList<>();

        Cursor cursor = db.getTasksByStatus(status);

        while(cursor.moveToNext()){
            ids.add(cursor.getString(0));
            titles.add(cursor.getString(1));
        }

        if(titles.isEmpty()){
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            adapter = new TaskAdapter(getContext(), ids, titles, status);
            recyclerView.setAdapter(adapter);
        }
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
                        String id = ids.get(position);

                        if(direction == ItemTouchHelper.RIGHT){

                            db.deleteTask(id);
                            ids.remove(position);
                            titles.remove(position);
                            adapter.notifyItemRemoved(position);
                        } else if(direction == ItemTouchHelper.LEFT){

                            showEditDialog(position);
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

                        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                            if(dX > 0){
                                paint.setColor(Color.RED);
                                c.drawRect(itemView.getLeft(),
                                        itemView.getTop(),
                                        dX,
                                        itemView.getBottom(),
                                        paint);

                                paint.setColor(Color.WHITE);
                                c.drawText("حذف",
                                        itemView.getLeft() + margin,
                                        itemView.getTop() + itemView.getHeight()/2 + 20,
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
                                        itemView.getTop() + itemView.getHeight()/2 + 20,
                                        paint);
                            }
                        }

                        super.onChildDraw(c, recyclerView, viewHolder,
                                dX, dY, actionState, isCurrentlyActive);
                    }
                };

        new ItemTouchHelper(simpleCallback)
                .attachToRecyclerView(recyclerView);
    }

    private void showEditDialog(int position){

        EditText editText = new EditText(getContext());
        editText.setText(titles.get(position));

        new AlertDialog.Builder(getContext())
                .setTitle("تعديل المهمة")
                .setView(editText)
                .setPositiveButton("حفظ", (dialog, which) -> {

                    String newTitle = editText.getText().toString().trim();

                    if(!newTitle.isEmpty()){
                        db.updateTitle(ids.get(position), newTitle);
                        titles.set(position, newTitle);
                        adapter.notifyItemChanged(position);
                    }
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }
}
