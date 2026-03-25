package com.altaelimia.todoapp.Class;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.altaelimia.todoapp.CallBack.TaskDao;
import com.altaelimia.todoapp.Database.RoomAppDatabase;
import com.altaelimia.todoapp.Adapter.ViewPagerAdapter;
import com.altaelimia.todoapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import android.app.AlertDialog;
import android.widget.EditText;
import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    RoomAppDatabase db;
    TaskDao taskDao;
    Task task;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        db = RoomAppDatabase.getInstance(this);
//        taskDao = db.taskDao();
//        binding.viewPager.setAdapter(new ViewPagerAdapter(this));

        initializeObjects();
        viewsActions();
    }

    private void viewsActions() {

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

    private void initializeObjects() {

        db = RoomAppDatabase.getInstance(this);
        taskDao = db.taskDao();
        viewPagerAdapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(viewPagerAdapter);

    }

    private void showAddDialog() {
        EditText editText = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("إضافة مهمة")
                .setView(editText)
                .setPositiveButton("إضافة", (dialog, which) -> {

//                    String title = editText.getText().toString().trim();
//                    task = new Task(title, false);
//                    if (!title.isEmpty()) {
//                        taskDao.insert(task);
//                        refreshFragments();
//                    }
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
//        binding.viewPager.setAdapter(new ViewPagerAdapter(this));
        viewPagerAdapter.notifyItemChanged(0);
    }
}
