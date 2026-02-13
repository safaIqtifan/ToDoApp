package com.altaelimia.todoapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.HashMap;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final HashMap<Integer, TaskFragment> fragmentMap = new HashMap<>();

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        TaskFragment fragment = TaskFragment.newInstance(position);
        fragmentMap.put(position, fragment);
        return fragment;
    }

    public TaskFragment getFragment(int position) {
        return fragmentMap.get(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}


