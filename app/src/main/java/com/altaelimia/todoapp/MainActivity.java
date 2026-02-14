package com.altaelimia.todoapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.app.AlertDialog;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;
    FloatingActionButton fab;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        fab = findViewById(R.id.fab);

        viewPager.setAdapter(new ViewPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if(position == 0)
                        tab.setText("Todo");
                    else
                        tab.setText("Done");
                }).attach();

        fab.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog(){
        EditText editText = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("إضافة مهمة")
                .setView(editText)
                .setPositiveButton("إضافة", (dialog, which) -> {
                    String title = editText.getText().toString().trim();
                    if(!title.isEmpty()){
                        db.insertTask(title);
                        refreshFragments();
                    }
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }

    public void refreshFragments(){
        viewPager.setAdapter(new ViewPagerAdapter(this));
    }
}
