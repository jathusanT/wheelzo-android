package me.jathusan.wheelzo.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import me.jathusan.wheelzo.R;

public class RideInfoActivity extends BaseActivity {

    private TextView mTitle, mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_info);

        mTitle = (TextView) findViewById(R.id.ride_title);
        mInfo = (TextView) findViewById(R.id.ride_description);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
