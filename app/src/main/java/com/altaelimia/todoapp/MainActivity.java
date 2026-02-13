package com.altaelimia.todoapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    FloatingActionButton fabAdd;
    ViewPagerAdapter adapter;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        fabAdd = findViewById(R.id.fabAdd);

        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "Todo" : "Done")
        ).attach();

        fabAdd.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        EditText editText = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("إضافة مهمة")
                .setView(editText)
                .setPositiveButton("إضافة", (dialog, which) -> {
                    String title = editText.getText().toString().trim();
                    if (!title.isEmpty()) {
                        db.insertData(title);

                        // إعادة تحميل التبويب الحالي
                        int currentTab = viewPager.getCurrentItem();
                        TaskFragment fragment = adapter.getFragment(currentTab);
                        if(fragment != null) fragment.loadData();
                    }
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }
}
