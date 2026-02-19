package com.altaelimia.todoapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.altaelimia.todoapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        db = new DatabaseHelper(this);
        db = RoomAppDatabase.getInstance(this);
        taskDao = db.taskDao();

//        viewPager = findViewById(R.id.viewPager);
//        tabLayout = findViewById(R.id.tabLayout);
//        fab = findViewById(R.id.fab);

        binding.viewPager.setAdapter(new ViewPagerAdapter(this));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                EventBus.getDefault().post(new TabChangedEvent(tab.getPosition()));
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
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

    private void showAddDialog() {
        EditText editText = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("إضافة مهمة")
                .setView(editText)
                .setPositiveButton("إضافة", (dialog, which) -> {

                    String title = editText.getText().toString().trim();
                    task = new Task(title, false);


                    if (!title.isEmpty()) {
//                        db.insertTask(title);
                        taskDao.insert(task);
                        refreshFragments();
                    }
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }

    public void refreshFragments() {
        binding.viewPager.setAdapter(new ViewPagerAdapter(this));
    }
}
