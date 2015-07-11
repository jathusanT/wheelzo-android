package me.jathusan.wheelzo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class WheelzoPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "WheelzoPagerAdapter";
    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();

    public WheelzoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

}
