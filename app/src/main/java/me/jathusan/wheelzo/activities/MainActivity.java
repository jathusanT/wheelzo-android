package me.jathusan.wheelzo.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.AppEventsLogger;
import com.facebook.Session;

import me.jathusan.wheelzo.R;
import me.jathusan.wheelzo.adapter.WheelzoPagerAdapter;
import me.jathusan.wheelzo.fragments.AllRidesFragment;
import me.jathusan.wheelzo.fragments.MyAccountFragment;
import me.jathusan.wheelzo.views.SlidingTabLayout;

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
            public void onPageScrollStateChanged(int state) { }
        });
        initializeActionBar();
    }

    private void setupViewPager() {
        mViewPager.setOffscreenPageLimit(2);
        WheelzoPagerAdapter adapter = new WheelzoPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllRidesFragment(), "All Rides");
        adapter.addFragment(new MyAccountFragment(), "My Account");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            Session.getActiveSession().closeAndClearTokenInformation();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
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
