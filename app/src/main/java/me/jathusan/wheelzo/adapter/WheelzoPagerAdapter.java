package me.jathusan.wheelzo.adapter;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import me.jathusan.wheelzo.fragments.AllRidesFragment;
import me.jathusan.wheelzo.fragments.MyRidesFragment;
import me.jathusan.wheelzo.fragments.SearchFragment;

public class WheelzoPagerAdapter extends FragmentStatePagerAdapter {
    private ActionBar mActionBar;

    public WheelzoPagerAdapter(FragmentManager fm, ActionBar actionbar) {
        super(fm);
        mActionBar = actionbar;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new AllRidesFragment();
            case 1:
                return new MyRidesFragment();
            default:
                return new SearchFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}