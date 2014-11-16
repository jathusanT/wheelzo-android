package me.jathusan.wheelzo.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.AppEventsLogger;

import me.jathusan.wheelzo.R;
import me.jathusan.wheelzo.adapter.WheelzoPagerAdapter;

public class MainActivity extends BaseActivity {

    private String[] mDrawerItemTitles;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerItems;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Facebook Analytics
        AppEventsLogger.activateApp(this);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);
        WheelzoPagerAdapter adapter = new WheelzoPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        getSupportActionBar().setSelectedNavigationItem(position);
                    }
                });
        mDrawerItemTitles = getResources().getStringArray(R.array.drawer_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerItems = (ListView) findViewById(R.id.left_drawer);
        mDrawerItems.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_item, mDrawerItemTitles));
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                null,
                R.string.drawer_opened,
                R.string.drawer_closed) {
        };

        // set te drawer listener here so the toggle will animate when the drawer is
        // slided out or in
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        initializeActionBar();
    }

    @Override
    protected void onPause() {
        // Facebook analytics
        AppEventsLogger.deactivateApp(this);
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
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
            Log.e("initializeActionBar()", "ActionBar was null");
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        addTabsToActionBar(actionBar);
    }


    private void addTabsToActionBar(ActionBar actionBar) {
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        };
        actionBar.addTab(
                actionBar.newTab()
                        .setText("All Rides")
                        .setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("My Account")
                        .setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Reviews")
                        .setTabListener(tabListener));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.default_purple)));
    }
}