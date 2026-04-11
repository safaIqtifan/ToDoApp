package com.altaelimia.todoapp.Class;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.altaelimia.todoapp.Adapter.ViewPagerAdapter;
import com.altaelimia.todoapp.ViewModel.TaskViewModel;
import com.altaelimia.todoapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import android.app.AlertDialog;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ViewPagerAdapter viewPagerAdapter;
    TaskViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        initializeObjects();
        viewsActions();
    }

    private void viewsActions() {

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

        viewPagerAdapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(viewPagerAdapter);

    }

    private void showAddDialog() {
        EditText editText = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("إضافة مهمة")
                .setView(editText)
                .setPositiveButton("حفظ", (dialog, which) -> {

                    String title = editText.getText().toString().trim();
                    if (title.isEmpty()) return;

                    Task task = new Task();
                    task.title = title;
                    task.isChecked = false;

                    viewModel.insert(task);

                })
                .setNegativeButton("إلغاء", null)
                .show();
    }

}
