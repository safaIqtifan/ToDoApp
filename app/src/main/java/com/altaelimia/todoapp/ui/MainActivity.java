package com.altaelimia.todoapp.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.altaelimia.todoapp.TabChangedEvent;
import com.altaelimia.todoapp.Task;
import com.altaelimia.todoapp.ViewPagerAdapter;
import com.altaelimia.todoapp.database.RoomAppDatabase;
import com.altaelimia.todoapp.database.TaskDao;
import com.altaelimia.todoapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import android.app.AlertDialog;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    //    ViewPager2 viewPager;
//    TabLayout tabLayout;
//    FloatingActionButton fab;
//    DatabaseHelper db;
    RoomAppDatabase db;
    TaskDao taskDao;
    Task task;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeObjects();
        viewsActions();
    }

    private void viewsActions() {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                EventBus.getDefault().post(new TabChangedEvent(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    if (position == 0)
                        tab.setText("Todo");
                    else
                        tab.setText("Done");
                }).attach();

        binding.fab.setOnClickListener(v -> showAddDialog());
    }

    private void initializeObjects() {
        db = RoomAppDatabase.getInstance(this);
        taskDao = db.taskDao();

//        viewPager = findViewById(R.id.viewPager);
//        tabLayout = findViewById(R.id.tabLayout);
//        fab = findViewById(R.id.fab);
        viewPagerAdapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(viewPagerAdapter);

    }

    private void showAddDialog() {
        EditText editText = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("إضافة مهمة")
                .setView(editText)
                .setPositiveButton("إضافة", (dialog, which) -> {
                    addNewTask(editText.getText().toString().trim());
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }

    private void addNewTask(String trim) {
        task = new Task(trim, false);
        if (!trim.isEmpty()) {
            taskDao.insert(task);
            refreshFragments();
        }
    }

    public void refreshFragments() {
        viewPagerAdapter.notifyItemChanged(0);
    }
}
