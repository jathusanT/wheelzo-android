package me.jathusan.android.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;

import com.facebook.AppEventsLogger;

import me.jathusan.android.R;
import me.jathusan.android.adapter.WheelzoPagerAdapter;
import me.jathusan.android.fragments.AllRidesFragment;
import me.jathusan.android.fragments.MyAccountFragment;
import me.jathusan.android.fragments.MyRidesFragment;
import me.jathusan.android.views.SlidingTabLayout;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Facebook Analytics
        AppEventsLogger.activateApp(this);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager();

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.custom_tab_layout, 0);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.white));
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.default_purple));
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSlidingTabLayout.setElevation(getResources().getDimension(R.dimen.actionbar_elevation));
        }
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        initializeActionBar();
    }

    private void setupViewPager() {
        mViewPager.setOffscreenPageLimit(2);
        WheelzoPagerAdapter adapter = new WheelzoPagerAdapter(this, getSupportFragmentManager());
        adapter.addFragment(new AllRidesFragment(), getString(R.string.all_rides_tab), R.drawable.ic_rides);
        adapter.addFragment(new MyRidesFragment(), getString(R.string.my_rides_tab), R.drawable.ic_my_rides);
        adapter.addFragment(new MyAccountFragment(), getString(R.string.my_account_tab), R.drawable.ic_account);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        getSupportActionBar().setSelectedNavigationItem(position);
                    }
                });
    }

    @Override
    protected void onPause() {
        // Facebook analytics
        AppEventsLogger.deactivateApp(this);
        super.onPause();
    }

    private void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Log.e(TAG, "Failed to initialize actionbar -- actionbar was null");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBar.setElevation(0);
        }
    }

}
