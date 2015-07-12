package me.jathusan.wheelzo.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import me.jathusan.wheelzo.R;
import me.jathusan.wheelzo.framework.Ride;
import me.jathusan.wheelzo.framework.RoundedImageView;
import me.jathusan.wheelzo.util.ImageUtil;

public class RideInfoActivity extends BaseActivity {

    private static final String TAG = "RideInfoActivity";

    private static final String RIDE_PARCELABLE_KEY = "me.jathusan.wheelzo.Ride";
    private TextView mOrigin, mDestination, mDate, mCapacity, mPrice, mDriverName;
    private Ride mRide;
    private RoundedImageView mDriverPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_info);

        Intent intent = getIntent();
        if (intent == null){
            Log.e(TAG, "Failed to create RideInfoActivity -- intent was null");
            finishActivity();
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            Log.e(TAG, "Failed to create RideInfoActivity -- bundle was null");
            finishActivity();
        }

        mRide = bundle.getParcelable(RIDE_PARCELABLE_KEY);
        if (mRide == null) {
            Log.e(TAG, "Failed to create RideInfoActivity -- parcelable was null");
            finishActivity();
        }

        mDriverPicture = (RoundedImageView) findViewById(R.id.driver_picture);
        mDriverName = (TextView) findViewById(R.id.driver_name);
        mOrigin = (TextView) findViewById(R.id.origin_text);
        mDestination = (TextView) findViewById(R.id.dest_text);
        mDate = (TextView) findViewById(R.id.date_text);
        mCapacity = (TextView) findViewById(R.id.cap_text);
        mPrice = (TextView) findViewById(R.id.price_text);

        // Get the back button to show up in the actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageUtil.loadFacebookImageIntoView(this, mRide.getDriverFacebookid(), mDriverPicture);
        setupInterface();
    }

    private void setupInterface() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }
        mDriverName.setText(mRide.getDriverName());
        mOrigin.setText(mRide.getOrigin());
        mDestination.setText(mRide.getDestination());
        mDate.setText(mRide.getStart());
        //mCapacity.setText(mRide.getCapacity());
        mPrice.setText("$" + mRide.getPrice());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void finishActivity() {
        Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
        finish();
    }

}
