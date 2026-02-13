package com.altaelimia.todoapp;

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

import android.app.AlertDialog;
import java.util.ArrayList;

public class TaskFragment extends Fragment {

    private static final String ARG_STATUS = "status";
    int status;
    DatabaseHelper db;
    RecyclerView recyclerView;
    TextView txtEmpty;
    ArrayList<String> ids, titles;
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
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        txtEmpty = view.findViewById(R.id.txtEmpty);

        db = new DatabaseHelper(getContext());

        if (getArguments() != null) {
            status = getArguments().getInt(ARG_STATUS);
        }

        loadData();
        setupSwipe();

        return view;
    }

    public void loadData() {
        ids = new ArrayList<>();
        titles = new ArrayList<>();

        Cursor cursor = db.getTasksByStatus(status);
        while(cursor.moveToNext()) {
            ids.add(cursor.getString(0));
            titles.add(cursor.getString(1));
        }

        if(titles.isEmpty()) {
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
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            Paint paint = new Paint();
            {
                paint.setTextSize(30);
                paint.setFakeBoldText(true);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                String id = ids.get(position);

                if(direction == ItemTouchHelper.RIGHT){
                    db.deleteData(id);
                    ids.remove(position);
                    titles.remove(position);
                    adapter.notifyItemRemoved(position);
                } else if(direction == ItemTouchHelper.LEFT){
                    showEditDialog(position);
                    adapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {

                View itemView = viewHolder.itemView;
                float textMargin = 40;

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if(dX > 0) {
                        paint.setColor(Color.RED);
                        c.drawRect((float)itemView.getLeft(), (float)itemView.getTop(),
                                dX, (float)itemView.getBottom(), paint);

                        paint.setColor(Color.WHITE);
                        c.drawText("حذف",
                                itemView.getLeft() + textMargin,
                                itemView.getTop() + itemView.getHeight()/2 + 20,
                                paint);
                    } else {
                        paint.setColor(Color.GREEN);
                        c.drawRect((float)itemView.getRight() + dX, (float)itemView.getTop(),
                                (float)itemView.getRight(), (float)itemView.getBottom(), paint);

                        paint.setColor(Color.WHITE);
                        float textWidth = paint.measureText("تعديل");
                        c.drawText("تعديل",
                                itemView.getRight() - textWidth - textMargin,
                                itemView.getTop() + itemView.getHeight()/2 + 20,
                                paint);
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

    private void showEditDialog(int position) {
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
