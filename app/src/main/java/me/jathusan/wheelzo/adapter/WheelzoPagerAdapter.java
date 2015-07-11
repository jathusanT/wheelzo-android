package me.jathusan.wheelzo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import me.jathusan.wheelzo.fragments.AllRidesFragment;
import me.jathusan.wheelzo.fragments.MyAccountFragment;
import me.jathusan.wheelzo.fragments.SearchFragment;

public class WheelzoPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "WheelzoPagerAdapter";
    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    private ArrayList<String> mFragmentTitleList = new ArrayList<String>();

    public WheelzoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
<<<<<<< Updated upstream
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new AllRidesFragment();
            case 1:
                return new MyAccountFragment();
            default:
                return new SearchFragment();
        }
=======
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
>>>>>>> Stashed changes
    }

    @Override
    public int getCount() {
<<<<<<< Updated upstream
        return 3;
=======
        return mFragmentList.size();
>>>>>>> Stashed changes
    }

}
