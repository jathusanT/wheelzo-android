package me.jathusan.wheelzo.activities;

import android.os.Bundle;
import android.view.MenuItem;

import me.jathusan.wheelzo.R;
import me.jathusan.wheelzo.http.WheelzoHttpClient;

public class CreateRideActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);
        WheelzoHttpClient.createRide(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
