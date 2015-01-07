package me.jathusan.wheelzo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import me.jathusan.wheelzo.fragments.AllRidesFragment;
import me.jathusan.wheelzo.fragments.MyAccountFragment;
import me.jathusan.wheelzo.fragments.SearchFragment;

public class WheelzoPagerAdapter extends FragmentStatePagerAdapter {

    public WheelzoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new AllRidesFragment();
            default:
                return new MyAccountFragment();
//            default:
//                return new SearchFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

}