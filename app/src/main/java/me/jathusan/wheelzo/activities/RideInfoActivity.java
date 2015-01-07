package me.jathusan.wheelzo.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import me.jathusan.wheelzo.R;

public class RideInfoActivity extends BaseActivity {

    private TextView mOrigin, mDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_info);

        mOrigin = (TextView) findViewById(R.id.ride_origin);
        mDestination = (TextView) findViewById(R.id.ride_destination);

        // Get the back button to show up in the actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
