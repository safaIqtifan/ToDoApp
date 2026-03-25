package com.altaelimia.todoapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.altaelimia.todoapp.fragment.TaskFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0)
            return TaskFragment.newInstance(0);
        else
            return TaskFragment.newInstance(1);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
