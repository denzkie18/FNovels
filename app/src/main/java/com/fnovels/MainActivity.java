package com.fnovels;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.fnovels.adapter.viewpager.ViewpagerAdapter;

import com.google.android.material.color.MaterialColors;

public class MainActivity extends AppCompatActivity {

    private ImageView[] tabs;
    private int[] outline;
    private int[] filled;
    private int activeColor;
    private int inactiveColor;

    private ViewPager2 pager;
    private View indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = findViewById(R.id.viewpager);
        indicator = findViewById(R.id.indicator);

        tabs = new ImageView[] { findViewById(R.id.tabHome), findViewById(R.id.tabExplore) };
        outline = new int[] { R.drawable.ic_home_outline, R.drawable.ic_explore_outline };
        filled = new int[] { R.drawable.ic_home_filled, R.drawable.ic_explore_filled };

        activeColor = MaterialColors.getColor(this,
                com.google.android.material.R.attr.colorOnPrimaryContainer, Color.BLACK);
        inactiveColor = MaterialColors.getColor(this,
                com.google.android.material.R.attr.colorOnSurfaceVariant, Color.GRAY);

        final float tabW = 84 * getResources().getDisplayMetrics().density;

        pager.setAdapter(new ViewpagerAdapter(this));
        pager.setUserInputEnabled(false);

        for (int i = 0; i < tabs.length; i++) {
            final int index = i;
            tabs[i].setOnClickListener(v -> pager.setCurrentItem(index));
        }

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float offset, int px) {
                indicator.setTranslationX((position + offset) * tabW);
            }

            @Override
            public void onPageSelected(int position) {
                selectTab(position);
            }
        });

        selectTab(0);
    }

    private void selectTab(int position) {
        for (int i = 0; i < tabs.length; i++) {
            boolean on = i == position;
            tabs[i].setImageResource(on ? filled[i] : outline[i]);
            tabs[i].setColorFilter(on ? activeColor : inactiveColor);
        }
    }
}