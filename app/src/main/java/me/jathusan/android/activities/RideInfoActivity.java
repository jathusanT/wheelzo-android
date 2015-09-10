package me.jathusan.android.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import me.jathusan.android.R;
import me.jathusan.android.framework.Ride;
import me.jathusan.android.framework.RoundedImageView;
import me.jathusan.android.util.ImageUtil;

public class RideInfoActivity extends BaseActivity {

    private static final String TAG = "RideInfoActivity";

    /* Keys */
    public static final String RIDE_PARCELABLE_KEY = "me.jathusan.wheelzo.Ride";
    public static final String MY_RIDE_KEY = "wheelzo_my_ride_key";

    private TextView mOrigin, mDestination, mDate, mCapacity, mPrice, mDriverName;
    private Ride mRide;
    private RoundedImageView mDriverPicture;
    private Button mCommentsButton;
    private Button mProfileButton;
    private Button mDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_info);

        Intent intent = getIntent();
        if (intent == null) {
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

        mCommentsButton = (Button) findViewById(R.id.view_comments_button);
        mCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start comments activity
            }
        });

        mProfileButton = (Button) findViewById(R.id.view_profile_button);
        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    mDriverPicture.callOnClick();
                } else {
                    mDriverPicture.performClick();
                }
            }
        });

        mDeleteButton = (Button) findViewById(R.id.delete_ride_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: delete ride
                finish();
            }
        });

        mDriverPicture = (RoundedImageView) findViewById(R.id.driver_picture);
        mDriverName = (TextView) findViewById(R.id.driver_name);
        mOrigin = (TextView) findViewById(R.id.origin_text);
        mDestination = (TextView) findViewById(R.id.dest_text);
        mDate = (TextView) findViewById(R.id.date_text);
        mCapacity = (TextView) findViewById(R.id.cap_text);
        mPrice = (TextView) findViewById(R.id.price_text);

        boolean isAdmin = bundle.getBoolean(MY_RIDE_KEY, false);
        if (isAdmin) {
            mProfileButton.setVisibility(View.GONE);
            mDeleteButton.setVisibility(View.VISIBLE);
        }

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
        Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
        finish();
    }

}
