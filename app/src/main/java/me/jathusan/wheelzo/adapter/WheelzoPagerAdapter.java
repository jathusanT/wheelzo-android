package me.jathusan.wheelzo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import java.util.ArrayList;

public class WheelzoPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "WheelzoPagerAdapter";
    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    private ArrayList<String> mFragmentTitles = new ArrayList<String>();

    public WheelzoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitles.add(title);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
