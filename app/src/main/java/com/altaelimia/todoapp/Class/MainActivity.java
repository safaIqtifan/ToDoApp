package com.altaelimia.todoapp.Class;

import android.os.Bundle;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.altaelimia.todoapp.Adapter.ViewPagerAdapter;
import com.altaelimia.todoapp.R;
import com.altaelimia.todoapp.ViewModel.TaskViewModel;
import com.altaelimia.todoapp.databinding.ActivityMainBinding;
import com.altaelimia.todoapp.databinding.DialogAddTaskBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ViewPagerAdapter viewPagerAdapter;
    private TaskViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        initializeUI();
    }

    private void initializeUI() {
        viewPagerAdapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    tab.setText(position == 0 ? R.string.todo_tab : R.string.done_tab);
                }).attach();

        binding.fab.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        DialogAddTaskBinding dialogBinding = DialogAddTaskBinding.inflate(LayoutInflater.from(this));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.add_task_title)
                .setView(dialogBinding.getRoot())
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, (d, w) -> d.dismiss())
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String title = Objects.requireNonNull(dialogBinding.etTaskTitle.getText()).toString().trim();

                if (title.isEmpty()) {
                    dialogBinding.etTaskTitle.setError(getString(R.string.error_empty_title));
                } else {
                    saveTask(title);
                    dialog.dismiss();
                }
            });
        });

        dialog.show();
    }

    private void saveTask(String title) {
        Task task = new Task(title, false);
        viewModel.insert(task);
    }

}
