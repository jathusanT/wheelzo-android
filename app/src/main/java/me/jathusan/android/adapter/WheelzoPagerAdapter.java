package me.jathusan.android.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import java.util.ArrayList;

public class WheelzoPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    private static final String TAG = "WheelzoPagerAdapter";

    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    private ArrayList<String> mFragmentTitles = new ArrayList<String>();
    private ArrayList<Integer> mFragmentResources = new ArrayList<Integer>();

    public WheelzoPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment, String title, int resId) {
        mFragmentList.add(fragment);
        mFragmentTitles.add(title);
        mFragmentResources.add(resId);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = mContext.getResources().getDrawable(mFragmentResources.get(position));
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
