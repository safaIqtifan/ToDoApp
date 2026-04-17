package com.altaelimia.todoapp.Class;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.altaelimia.todoapp.Adapter.ViewPagerAdapter;
import com.altaelimia.todoapp.ViewModel.TaskViewModel;
import com.altaelimia.todoapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;

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
        // 1. Create EditText with better styling/padding
        final AppCompatEditText editText = new AppCompatEditText(this);
        editText.setHint("اكتب المهمة هنا...");

        // Add some padding so the text isn't touching the dialog edges
        int paddingPx = (int) (16 * getResources().getDisplayMetrics().density);
        editText.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);

        // 2. Build the Dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("إضافة مهمة جديدة")
                .setView(editText)
                .setPositiveButton("حفظ", null) // Set to null first to override closing behavior
                .setNegativeButton("إلغاء", (d, w) -> d.dismiss())
                .create();

        // 3. Override the Positive Button click to prevent closing if input is invalid
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String title = editText.getText().toString().trim();

                if (title.isEmpty()) {
                    editText.setError("لا يمكن أن يكون العنوان فارغاً");
                } else {
                    saveTask(title);
                    dialog.dismiss();
                }
            });
        });

        dialog.show();
    }

    // Best Practice: Separate logic from UI presentation
    private void saveTask(String title) {
        Task task = new Task();
        task.title = title;
        task.isChecked = false;
        viewModel.insert(task);
    }

//    private void showAddDialog() {
//        EditText editText = new EditText(this);
//
//        new AlertDialog.Builder(this)
//                .setTitle("إضافة مهمة")
//                .setView(editText)
//                .setPositiveButton("حفظ", (dialog, which) -> {
//
//                    String title = editText.getText().toString().trim();
//                    if (title.isEmpty()) return;
//
//                    Task task = new Task();
//                    task.title = title;
//                    task.isChecked = false;
//
//                    viewModel.insert(task);
//
//                })
//                .setNegativeButton("إلغاء", null)
//                .show();
//    }

}
