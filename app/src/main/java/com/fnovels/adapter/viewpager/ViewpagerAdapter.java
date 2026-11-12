package com.fnovels.adapter.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.fnovels.fragment.ExploreFragmentActivity;
import com.fnovels.fragment.HomeFragmentActivity;

public class ViewpagerAdapter extends FragmentStateAdapter {

    public ViewpagerAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragmentActivity();

            case 1:
                return new ExploreFragmentActivity();

            default:
                return new HomeFragmentActivity();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}